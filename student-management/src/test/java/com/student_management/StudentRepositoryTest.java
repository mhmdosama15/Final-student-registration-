package com.student_management.repository;

import com.student_management.models.Course;
import com.student_management.models.Instructor;
import com.student_management.models.Student;
import com.student_management.repositories.CourseRepository;
import com.student_management.repositories.InstructorRepository;
import com.student_management.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    public void setup() {
        instructor = new Instructor();
        instructor.setName("Dr. Smith");
        instructor.setDepartment("Computer Science");
        instructor = instructorRepository.save(instructor);

        course = new Course();
        course.setName("Spring Boot");
        course.setCode("SB101");
        course.setCredits(3);
        course.setInstructor(instructor);
        course = courseRepository.save(course);
    }

    @Test
    public void testSaveStudent() {
        // Given
        Student student = new Student();
        student.setName("John Doe");
        student.setMajor("Computer Science");
        student.setYear(2);
        student.setInstructor(instructor);
        student.setCourses(List.of(course));

        // When
        Student savedStudent = studentRepository.save(student);

        // Then
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("John Doe");
    }

    @Test
    public void testFindStudentById() {
        // Given
        Student student = new Student();
        student.setName("Jane Doe");
        student.setMajor("Information Technology");
        student.setYear(3);
        student.setInstructor(instructor);
        student.setCourses(List.of(course));

        student = studentRepository.save(student);

        // When
        Optional<Student> foundStudentOptional = studentRepository.findById(student.getId());

        // Then
        assertThat(foundStudentOptional).isPresent();
        Student foundStudent = foundStudentOptional.get();
        assertThat(foundStudent.getName()).isEqualTo("Jane Doe");
    }

    @Test
    public void testDeleteStudent() {
        // Given
        Student student = new Student();
        student.setName("Mark Smith");
        student.setMajor("Mechanical Engineering");
        student.setYear(1);
        student.setInstructor(instructor);
        student.setCourses(List.of(course));

        student = studentRepository.save(student);
        Integer studentId = student.getId();

        // When
        studentRepository.deleteById(studentId);

        // Then
        Optional<Student> deletedStudentOptional = studentRepository.findById(studentId);
        assertThat(deletedStudentOptional).isEmpty();
    }

    @Test
    public void testFindAllStudents() {
        // Given
        Student student1 = new Student();
        student1.setName("Alice");
        student1.setMajor("Electrical Engineering");
        student1.setYear(4);
        student1.setInstructor(instructor);
        student1.setCourses(List.of(course));

        Student student2 = new Student();
        student2.setName("Bob");
        student2.setMajor("Civil Engineering");
        student2.setYear(2);
        student2.setInstructor(instructor);
        student2.setCourses(List.of(course));

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<Student> students = studentRepository.findAll();


        assertThat(students).hasSize(2);
    }
}
