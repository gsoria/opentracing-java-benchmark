package com.autentia.training.course.model.services;

import com.autentia.training.course.model.entities.Course;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CourseService {

    Course create(Course course);

    Course update(Long id, Course course) throws EntityNotFoundException;

    void delete(Long id) throws EntityNotFoundException;

    List<Course> findAll();

    Course findOne(Long id) throws EntityNotFoundException;
}
