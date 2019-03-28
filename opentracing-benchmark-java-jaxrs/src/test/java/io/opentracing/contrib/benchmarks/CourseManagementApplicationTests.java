package io.opentracing.contrib.benchmarks;

import com.autentia.training.course.CourseManagementApplication;
import com.autentia.training.course.config.TracerImplementation;
import com.autentia.training.course.resources.CourseResource;
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
		loadSpringContext(TracerImplementation.NOTINSTRUMENTED);
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

	@After
	public void closeContext() {
		context.close();
	}

	private void loadSpringContext(String profile) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
		context = SpringApplication.run(CourseManagementApplication.class);
	}
}
