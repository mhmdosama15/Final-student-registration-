package com.student_management.repositories;

import com.student_management.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    Course findByName(String name);
    Course findByInstructorId(Integer instructorId);
}
