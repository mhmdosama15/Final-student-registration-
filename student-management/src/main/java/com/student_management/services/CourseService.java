package com.student_management.services;

import com.student_management.models.Course;
import com.student_management.repositories.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;


    public Page<Course> getAllCourses(Pageable pageable) {
        try {
            return courseRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Failed to retrieve courses", e);
            throw new RuntimeException("Failed to retrieve courses", e);
        }
    }

    public Course findCourseById(Integer id) {
        try {
            Optional<Course> courseOptional = courseRepository.findById(id);
            return courseOptional.orElse(null);
        } catch (Exception e) {
            logger.error("Failed to find course with id: " + id, e);
            throw new RuntimeException("Failed to find course with id: " + id, e);
        }
    }

    public Course saveCourse(Course course) {

        try {
            return courseRepository.save(course);
        } catch (Exception e) {
            logger.error("Failed to save course: " + course, e);
            throw new RuntimeException("Failed to save course", e);
        }
    }

    public Course updateCourse(Course course) {
        Optional<Course> existingCourseOptional = courseRepository.findById(course.getId());
        if (existingCourseOptional.isEmpty()) {
            throw new RuntimeException("Course not found with id: " + course.getId());
        }

        Course existingCourse = existingCourseOptional.get();

        // Perform validation or business logic checks
        if (course.getCode() != null && !course.getCode().isEmpty()) {
            existingCourse.setCode(course.getCode());
        }

        if (course.getCredits() != null) {
            existingCourse.setCredits(course.getCredits());
        }

        if (course.getInstructor() != null) {
            existingCourse.setInstructor(course.getInstructor());
        }

        try {
            return courseRepository.save(existingCourse);
        } catch (Exception e) {
            logger.error("Failed to update course with id: " + course.getId(), e);
            throw new RuntimeException("Failed to update course with id: " + course.getId(), e);
        }
    }

    public void deleteCourseById(Integer id) {
        try {
            courseRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Failed to delete course with id: " + id, e);
            throw new RuntimeException("Failed to delete course with id: " + id, e);
        }
    }

    public List<Course> findCoursesByInstructor(Integer instructorId) {
        try {
            return (List<Course>) courseRepository.findByInstructorId(instructorId);
        } catch (Exception e) {
            logger.error("Failed to retrieve courses for instructor with id: " + instructorId, e);
            throw new RuntimeException("Failed to retrieve courses for instructor with id: " + instructorId, e);
        }
    }

    public List<Course> searchCoursesByName(String keyword) {
        try {
            return (List<Course>) courseRepository.findByName(keyword);
        } catch (Exception e) {
            logger.error("Failed to search courses by name: " + keyword, e);
            throw new RuntimeException("Failed to search courses by name: " + keyword, e);
        }
    }

}
