package org.sample;

import com.mashape.unirest.http.Unirest;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.ServletInfo;
import org.openjdk.jmh.annotations.*;
import org.sample.handlers.MetricsHandler;
import org.sample.listeners.TracingServletContextListener;
import org.sample.servlets.ServletExample;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

public class BenchmarkSimpleServlet {

    @State(Scope.Thread)
    public static class StateVariables {
        private Undertow server;

        @Setup(Level.Trial)
        public void setup() {

            try {
                DeploymentInfo servletBuilder = Servlets.deployment()
                        .setClassLoader(BenchmarkSimpleServlet.class.getClassLoader())
                        .setContextPath("/ui")
                        .setDeploymentName("jmh-examples-java-servlet-filter" +
                                ".war")
                        .addListener(new ListenerInfo(TracingServletContextListener.class))
                        .addServlet(Servlets.servlet("ServletExample", ServletExample.class)
                                .setAsyncSupported(true)
                                .addMapping("/*"));

                DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
                manager.deploy();
                PathHandler path = Handlers.path(
                        Handlers.resource(new ClassPathResourceManager(BenchmarkSimpleServlet.class.getClassLoader(), "webapp")))
                        .addExactPath("/", Handlers.redirect("/jmh-examples-java-servlet-filter"))
                        .addPrefixPath("/jmh-examples-java-servlet-filter", manager.start());

                server = Undertow.builder()
                        .addHttpListener(8080, "localhost")
                        .setHandler(path)
                        .setHandler(new MetricsHandler(null))
                        .build();
                server.start();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

        private static ServletInfo createServletInfo(String mapping, String servletName, Class<? extends Servlet> servlet) {
            ServletInfo servletInfo = Servlets
                    .servlet(servletName, servlet)
                    .setAsyncSupported(true)
                    .setLoadOnStartup(1)
                    .addMapping(mapping);

            return servletInfo;
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            server.stop();
        }
    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testClosingConnectionWhenParsingHeadersForTooLong(StateVariables state)
            throws Exception {
        String r = Unirest.get("http://localhost:8080/").asString().getBody();
        return r;
    }
}