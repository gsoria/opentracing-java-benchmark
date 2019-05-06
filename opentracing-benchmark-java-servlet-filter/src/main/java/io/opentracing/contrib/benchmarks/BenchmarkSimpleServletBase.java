package io.opentracing.contrib.benchmarks;

import com.mashape.unirest.http.Unirest;
import io.opentracing.contrib.benchmarks.config.TracerImplementation;
import io.opentracing.contrib.benchmarks.filters.PostMetricsFilter;
import io.opentracing.contrib.benchmarks.filters.PreMetricsFilter;
import io.opentracing.contrib.benchmarks.listeners.TracingServletContextListener;
import io.opentracing.contrib.benchmarks.servlets.ServletExample;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import org.openjdk.jmh.annotations.*;
import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

public class BenchmarkSimpleServletBase {

    public static final int PORT = 9090;
    public static final String HOST = "localhost";
    public static final String TRACER_IMPLEMENTATION_PROPERTY_NAME = "tracerImplementation";

    private static Undertow buildUndertowServer(Boolean tracerFilter,
                                                Boolean metricFilters) throws ServletException {

        DeploymentInfo deploymentInfo = Servlets.deployment()
                .setClassLoader(BenchmarkSimpleServletBase.class.getClassLoader())
                .setContextPath("/ui").setDeploymentName("jmh-examples-java-servlet-filter.war")
                .addServlet(createServletInfo("/*", "ServletExample",
                        ServletExample.class));

        if (tracerFilter) {
            deploymentInfo.addListener(new ListenerInfo(TracingServletContextListener.class));
        }

        if (metricFilters) {
            deploymentInfo = addFilters(deploymentInfo);
        }

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        manager.deploy();

        PathHandler path = Handlers.path(Handlers.resource(
                new ClassPathResourceManager(BenchmarkSimpleServletBase.class.getClassLoader(), "webapp")))
                .addExactPath("/", Handlers.redirect("/jmh-examples-java-servlet-filter"))
                .addPrefixPath("/jmh-examples-java-servlet-filter", manager.start());

        return Undertow.builder().addHttpListener(PORT, HOST).setHandler(path).build();
    }

    private static DeploymentInfo addFilters(DeploymentInfo deploymentInfo) {
        FilterInfo preMetricsFilter = new FilterInfo("PreMetricsFilter", PreMetricsFilter.class);
        preMetricsFilter.setAsyncSupported(true);

        FilterInfo tracingFilter = new FilterInfo("TracingFilter", TracingFilter.class);
        tracingFilter.setAsyncSupported(true);

        FilterInfo postMetricsFilter = new FilterInfo("PostMetricsFilter", PostMetricsFilter.class);
        postMetricsFilter.setAsyncSupported(true);

        DeploymentInfo deploymentInfo1 = deploymentInfo
                .addFilterUrlMapping("PreMetricsFilter", "/*", DispatcherType.REQUEST)
                .addFilter(preMetricsFilter)
                .addFilterUrlMapping("TracingFilter", "/*", DispatcherType.REQUEST)
                .addFilter(tracingFilter).addFilterUrlMapping("PostMetricsFilter", "/*", DispatcherType.REQUEST)
                .addFilter(postMetricsFilter);
        return deploymentInfo;
    }

    private static ServletInfo createServletInfo(String mapping, String servletName, Class<? extends Servlet> servlet) {
        ServletInfo servletInfo = Servlets.servlet(servletName, servlet)
                .setAsyncSupported(true)
                .setLoadOnStartup(Integer.valueOf(1))
                .addMapping(mapping);

        return servletInfo;
    }

    public String testSimpleRequest(StateVariablesBase state)
            throws Exception {
        String r = Unirest.get("http://" + HOST + ":" + PORT + "/").asString().getBody();
        return r;
    }

    @State(Scope.Thread)
    public static class StateVariablesBase {
        protected Undertow server;

        @TearDown(Level.Trial)
        public void doTearDown() {
            this.server.stop();
        }
    }

    @State(Scope.Thread)
    public static class StateVariablesNoInstrumentation extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                server = buildUndertowServer(Boolean.FALSE, Boolean.FALSE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Jaegertracer
    @State(Scope.Thread)
    public static class StateVariablesJaegerWithoutMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.JAEGERTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.FALSE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @State(Scope.Thread)
    public static class StateVariablesJaegerWithMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.JAEGERTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.TRUE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Haystack
    @State(Scope.Thread)
    public static class StateVariablesHaystackWithoutMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.HAYSTACKTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.FALSE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @State(Scope.Thread)
    public static class StateVariablesHaystackWithMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.HAYSTACKTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.TRUE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //NoopTracer
    @State(Scope.Thread)
    public static class StateVariablesNoopTracerWithoutMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.NOOPTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.FALSE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @State(Scope.Thread)
    public static class StateVariablesNoopTracerWithMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.NOOPTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.TRUE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //MockTracer
    @State(Scope.Thread)
    public static class StateVariablesMockTracerWithoutMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.MOCKTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.FALSE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @State(Scope.Thread)
    public static class StateVariablesMockTracerWithMetricFilters extends StateVariablesBase {

        @Setup(Level.Trial)
        public void setup() {
            try {
                System.setProperty(TRACER_IMPLEMENTATION_PROPERTY_NAME, TracerImplementation.MOCKTRACER.name());
                server = buildUndertowServer(Boolean.TRUE, Boolean.TRUE);
                server.start();

            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
