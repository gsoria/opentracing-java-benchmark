package io.opentracing.contrib.benchmarks;

import com.autentia.training.course.CourseManagementApplication;
import com.autentia.training.course.resources.CourseResource;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import javax.ws.rs.core.Response;

public class BenchmarkCourseManagement {

    public Response getAllCourses(StateVariables state) {
        return state.course.getAll();
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

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "jaegerTracer");
            initApplication();
        }
    }

    public static class StateVariablesNoopTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "noopTracer");
            initApplication();
        }
    }

    public static class StateVariablesHaystack extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "haystackTracer");
            initApplication();
        }
    }
}


