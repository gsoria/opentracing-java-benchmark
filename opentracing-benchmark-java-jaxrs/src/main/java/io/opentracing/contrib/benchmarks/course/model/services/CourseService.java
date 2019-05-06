package io.opentracing.contrib.benchmarks.course.model.services;

import io.opentracing.contrib.benchmarks.course.model.entities.Course;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CourseService {

    Course create(Course course);

    Course update(Long id, Course course) throws EntityNotFoundException;

    void delete(Long id) throws EntityNotFoundException;

    List<Course> findAll();

    Course findOne(Long id) throws EntityNotFoundException;
}
