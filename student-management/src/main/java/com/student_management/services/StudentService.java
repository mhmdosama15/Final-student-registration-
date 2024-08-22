package com.student_management.services;

import com.student_management.models.Student;
import com.student_management.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        try {
            return studentRepository.findAll();
        } catch (DataAccessException ex) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Failed to retrieve students", ex);
        }
    }

    @Transactional
    public void saveStudent(Student student) {
        try {
            studentRepository.save(student);
        } catch (DataAccessException ex) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Failed to save student: " + student.getName(), ex);
        }
    }

    @Transactional(readOnly = true)
    public Student findStudentById(Integer id) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            return optionalStudent.orElse(null);
        } catch (DataAccessException ex) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Failed to find student with ID: " + id, ex);
        }
    }

    @Transactional
    public void deleteStudentById(Integer id) {
        try {
            studentRepository.deleteById(id);
        } catch (DataAccessException ex) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Failed to delete student with ID: " + id, ex);
        }
    }

}
