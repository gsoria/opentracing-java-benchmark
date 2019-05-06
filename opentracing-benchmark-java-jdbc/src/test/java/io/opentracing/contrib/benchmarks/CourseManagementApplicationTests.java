package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.benchmarks.course.CourseManagementApplication;
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
		loadSpringContext("notInstrumented");
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithNoopTracer() {
		loadSpringContext("noopTracer");
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithMockTracer() {
		loadSpringContext("mockTracer");
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithJaegerTracer() {
		loadSpringContext("jaegerTracer");
		CourseResource course = context.getBean(CourseResource.class);
		assertNotNull(course);
	}

	@Test
	public void loadSpringContextWithHaystackTracer() {
		loadSpringContext("haystackTracer");
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
