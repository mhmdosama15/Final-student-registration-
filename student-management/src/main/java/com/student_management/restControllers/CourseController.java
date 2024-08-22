package com.student_management.restControllers;

import com.student_management.models.Course;
import com.student_management.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private static final Logger LOGGER = Logger.getLogger(CourseController.class.getName());

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "false") boolean descending) {
        try {
            PageRequest pageable;
            if (sortBy != null && !sortBy.isEmpty()) {
                pageable = PageRequest.of(page, size, descending ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            } else {
                pageable = PageRequest.of(page, size);
            }

            Page<Course> coursesPage = courseService.getAllCourses(pageable);
            return ResponseEntity.ok(coursesPage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving courses: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving courses.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Integer id) {
        try {
            Optional<Course> courseOptional = Optional.ofNullable(courseService.findCourseById(id));
            if (courseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with id: " + id);
            }
            return ResponseEntity.ok(courseOptional.get());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving course with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving course.");
        }
    }

    @PostMapping
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        try {
            // Add validation if necessary (e.g., check for duplicates)
            Course savedCourse = courseService.saveCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding course: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding course.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, @RequestBody Course course) {
        try {
            Optional<Course> existingCourseOptional = Optional.ofNullable(courseService.findCourseById(id));
            if (existingCourseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with id: " + id);
            }

            Course existingCourse = existingCourseOptional.get();

            // Update only the fields that have changed
            if (course.getName() != null && !course.getName().equals(existingCourse.getName())) {
                existingCourse.setName(course.getName());
            }
            if (course.getCode() != null && !course.getCode().equals(existingCourse.getCode())) {
                existingCourse.setCode(course.getCode());
            }
            if (course.getCredits() != null && !course.getCredits().equals(existingCourse.getCredits())) {
                existingCourse.setCredits(course.getCredits());
            }
            if (course.getInstructor() != null && !course.getInstructor().equals(existingCourse.getInstructor())) {
                existingCourse.setInstructor(course.getInstructor());
            }
            if (course.getStudents() != null && !course.getStudents().equals(existingCourse.getStudents())) {
                existingCourse.setStudents(course.getStudents());
            }

            Course updatedCourse = courseService.updateCourse(existingCourse);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating course with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating course.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        try {
            Optional<Course> courseOptional = Optional.ofNullable(courseService.findCourseById(id));
            if (courseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found with id: " + id);
            }
            courseService.deleteCourseById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course deleted successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting course with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting course.");
        }
    }
}
