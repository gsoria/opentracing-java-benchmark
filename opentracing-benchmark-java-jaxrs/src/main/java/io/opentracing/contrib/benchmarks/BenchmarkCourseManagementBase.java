package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.benchmarks.course.CourseManagementApplication;
import io.opentracing.contrib.benchmarks.config.TracerImplementation;
import io.opentracing.contrib.benchmarks.course.resources.CourseResource;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

public class BenchmarkCourseManagementBase {

    public static final String HOST = "localhost";
    public static final String PORT = "8080";

    public String getAllCourses(StateVariables state)  {
        String r = null;
        try {
            r = Unirest.get("http://" + HOST + ":" + PORT + "/rest/course/").asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return r;
    }

    @State(Scope.Benchmark)
    public static class StateVariables {
        public CourseResource course;
        public ConfigurableApplicationContext c;

        @TearDown(Level.Iteration)
        public void shutdownContext() {
            c.close();
        }

        public void initApplication() {
            c = SpringApplication.run(CourseManagementApplication.class);
            course = c.getBean(CourseResource.class);
        }
    }

    public static class StateVariablesNotInstrumented extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {
            initApplication();
        }
    }

    public static class StateVariablesJaeger extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.JAEGERTRACER);
            initApplication();
        }
    }

    public static class StateVariablesNoopTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.NOOPTRACER);
            initApplication();
        }
    }

    public static class StateVariablesHaystack extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.HAYSTACKTRACER);
            initApplication();
        }
    }

    public static class StateVariablesMockTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.MOCKTRACER);
            initApplication();
        }
    }
}


