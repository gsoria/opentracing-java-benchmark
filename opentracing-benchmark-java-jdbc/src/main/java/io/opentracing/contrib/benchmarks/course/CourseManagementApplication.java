package io.opentracing.contrib.benchmarks.course;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(scanBasePackages = "io.opentracing.contrib.benchmarks.*")
@MapperScan("io.opentracing.contrib.benchmarks.course.model.mappers")
@EnableAutoConfiguration(exclude= HibernateJpaAutoConfiguration.class)
public class CourseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagementApplication.class, args);
    }
}
