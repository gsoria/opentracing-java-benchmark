package org.sample;

import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import org.openjdk.jmh.annotations.*;
import org.sample.filters.PostMetricsFilter;
import org.sample.filters.PreMetricsFilter;
import org.sample.listeners.TracingServletContextListener;
import org.sample.servlets.ServletExample;

import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * This test uses Apache Benchmark to perform the test in a single shot time.
 * The purpose of this test is to compare with the numbers of BenchmarkSimpleServletThroughput
 */

public class BenchmarkSimpleServletAB {

    @State(Scope.Thread)
    public static class StateVariables {
        private Undertow server;

        @Setup(Level.Trial)
        public void setup() {

            try {
                DeploymentInfo servletBuilder = Servlets.deployment()
                        .setClassLoader(BenchmarkSimpleServletAB.class.getClassLoader())
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
                        Handlers.resource(new ClassPathResourceManager(BenchmarkSimpleServletAB.class.getClassLoader(), "webapp")))
                        .addExactPath("/", Handlers.redirect("/jmh-examples-java-servlet-filter"))
                        .addPrefixPath("/jmh-examples-java-servlet-filter", manager.start());

                server = Undertow.builder()
                        .addHttpListener(9090, "localhost")
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
                        .setClassLoader(BenchmarkSimpleServletAB.class.getClassLoader())
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
                        Handlers.resource(new ClassPathResourceManager(BenchmarkSimpleServletAB.class.getClassLoader(), "webapp")))
                        .addExactPath("/", Handlers.redirect("/jmh-examples-java-servlet-filter"))
                        .addPrefixPath("/jmh-examples-java-servlet-filter", manager.start());

                server = Undertow.builder()
                        .addHttpListener(9090, "localhost")
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
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testSimpleRequest(StateVariables state)
            throws Exception {

        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("ab -e results/results-ab-with-filters" +
                    ".csv -l -r -n 100 -c 10 -k -H \"Accept-Encoding: gzip, deflate\"  http://127.0.0.1:9090/jmh-examples-java-servlet-filter");        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testSimpleRequestWithoutFilters(StateVariablesWithoutFilters state)
            throws Exception {

        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("ab -e results/results-ab-without-filters" +
                    ".csv -l -r -n 100 -c 10 -k -H \"Accept-Encoding: gzip, deflate\"  http://127.0.0.1:9090/jmh-examples-java-servlet-filter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}