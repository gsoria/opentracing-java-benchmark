package org.sample;

import com.mashape.unirest.http.Unirest;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;

import io.undertow.servlet.api.*;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Level;
import org.sample.filters.PostMetricsFilter;
import org.sample.filters.PreMetricsFilter;
import org.sample.listeners.TracingServletContextListener;
import org.sample.servlets.ServletExample;

import javax.servlet.DispatcherType;
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
                        // Servlet example
                        .addServlet(Servlets.servlet("ServletExample", ServletExample.class)
                                .setAsyncSupported(true)
                                .addMapping("/*"));

                DeploymentInfo deploymentInfo = addFilters(servletBuilder);

                DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);

                manager.deploy();
                PathHandler path = Handlers.path(
                        Handlers.resource(new ClassPathResourceManager(BenchmarkSimpleServlet.class.getClassLoader(), "webapp")))
                        .addExactPath("/", Handlers.redirect("/jmh-examples-java-servlet-filter"))
                        .addPrefixPath("/jmh-examples-java-servlet-filter", manager.start());

                server = Undertow.builder()
                        .addHttpListener(8080, "localhost")
                        .setHandler(path)
                        .build();

                server.start();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

        private DeploymentInfo addFilters(DeploymentInfo deploymentInfo) {

            // Filter before tracing
            FilterInfo preMetricsFilter = new FilterInfo(
                    "PreMetricsFilter", PreMetricsFilter.class);
            preMetricsFilter.setAsyncSupported(true);

            FilterInfo tracingFilter = new FilterInfo("TracingFilter",
                    TracingFilter.class);
            tracingFilter.setAsyncSupported(true);

            FilterInfo postMetricsFilter = new FilterInfo(
                    "PostMetricsFilter", PostMetricsFilter.class);
            postMetricsFilter.setAsyncSupported(true);

            DeploymentInfo deploymentInfo1=  deploymentInfo.addFilterUrlMapping(
            "PreMetricsFilter", "/*",
            DispatcherType.REQUEST)
                    .addFilter(preMetricsFilter)
            .addFilterUrlMapping("TracingFilter", "/*",
            DispatcherType.REQUEST)
                    .addFilter(tracingFilter)
            .addFilterUrlMapping("PostMetricsFilter", "/*",
            DispatcherType.REQUEST)
                    .addFilter(postMetricsFilter);

            return deploymentInfo;
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            server.stop();
        }
    }

    @State(Scope.Thread)
    public static class StateVariablesWithoutFilters {

        private Undertow server;

        @Setup(Level.Trial)
        public void setup() {

            try {
                DeploymentInfo servletBuilder = Servlets.deployment()
                        .setClassLoader(BenchmarkSimpleServlet.class.getClassLoader())
                        .setContextPath("/ui")
                        .setDeploymentName("jmh-examples-java-servlet-filter" +
                                ".war")
                        // Servlet example
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
                        .build();

                server.start();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            server.stop();
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

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testSimpleRequest(StateVariables state)
            throws Exception {
        String r = Unirest.get("http://localhost:8080/").asString().getBody();
        return r;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testSimpleRequestWithoutFilters(StateVariablesWithoutFilters state)
            throws Exception {
        String r = Unirest.get("http://localhost:8080/").asString().getBody();
        return r;
    }
}