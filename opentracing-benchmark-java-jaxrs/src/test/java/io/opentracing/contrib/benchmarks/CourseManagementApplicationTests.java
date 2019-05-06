package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.benchmarks.course.CourseManagementApplication;
import io.opentracing.contrib.benchmarks.config.TracerImplementation;
import io.opentracing.contrib.benchmarks.course.resources.CourseResource;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import static org.junit.Assert.assertNotNull;

public class CourseManagementApplicationTests {

	private ConfigurableApplicationContext context;

	@Test
	public void loadSpringContextWithoutInstrumentation() {
		loadSpringContext(TracerImplementation.NO_INSTRUMENTATION);
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithNoopTracer() {
		loadSpringContext(TracerImplementation.NOOPTRACER);
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithJaegerTracer() {
		loadSpringContext(TracerImplementation.JAEGERTRACER);
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithHaystackTracer() {
		loadSpringContext(TracerImplementation.HAYSTACKTRACER);
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithMockTracer() {
		loadSpringContext(TracerImplementation.MOCKTRACER);
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@After
	public void closeContext() {
		context.close();
	}

	private void loadSpringContext(String profile) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
		context = SpringApplication.run(CourseManagementApplication.class);
	}
}
