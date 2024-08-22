package com.student_management.services;

import com.student_management.models.Instructor;
import com.student_management.repositories.InstructorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@Transactional
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    public Page<Instructor> getAllInstructors(Pageable pageable) {
        try {
            return instructorRepository.findAll(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve instructors", e);
        }
    }

    public Instructor findInstructorById(Integer id) {
        try {
            Optional<Instructor> instructorOptional = instructorRepository.findById(id);
            return instructorOptional.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find instructor with id: " + id, e);
        }
    }

    public Instructor saveInstructor(Instructor instructor) {
        try {
            return instructorRepository.save(instructor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save instructor", e);
        }
    }

    public Instructor updateInstructor(Instructor instructor) {
        try {
            Optional<Instructor> existingInstructorOptional = instructorRepository.findById(instructor.getId());
            if (existingInstructorOptional.isEmpty()) {
                throw new RuntimeException("Instructor not found with id: " + instructor.getId());
            }
            instructor.setId(instructor.getId());
            return instructorRepository.save(instructor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update instructor with id: " + instructor.getId(), e);
        }
    }

    public void deleteInstructorById(Integer id) {
        try {
            instructorRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete instructor with id: " + id, e);
        }
    }
}
