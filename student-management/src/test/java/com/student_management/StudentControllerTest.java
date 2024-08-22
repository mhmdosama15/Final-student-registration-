package com.student_management;

import com.student_management.controller.StudentController;
import com.student_management.models.Student;
import com.student_management.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private Student student;

    @BeforeEach
    public void setup() {
        student = new Student();
        student.setId(1);
        student.setName("John Doe");
        student.setMajor("Computer Science");
        student.setYear(2);
    }

    @Test
    public void testViewHomePage() throws Exception {
        // Given
        Mockito.when(studentService.getAllStudents()).thenReturn(Arrays.asList(student));

        // When & Then
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("name", is("John Doe")),
                                hasProperty("major", is("Computer Science")),
                                hasProperty("year", is(2))
                        )
                )));
    }

    @Test
    public void testAddStudentPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/students/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_student"))
                .andExpect(model().attributeExists("student"));
    }



    @Test
    public void testFindStudentPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/students/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("find_student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    public void testFindStudent() throws Exception {
        // Given
        Mockito.when(studentService.findStudentById(Mockito.anyInt())).thenReturn(student);

        // When & Then
        mockMvc.perform(post("/students/find")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("find_student"))
                .andExpect(model().attribute("student", hasProperty("id", is(1))))
                .andExpect(model().attribute("student", hasProperty("name", is("John Doe"))));
    }

    @Test
    public void testDeleteStudentPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/students/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete_student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        // Given
        Mockito.when(studentService.findStudentById(Mockito.anyInt())).thenReturn(student);

        // When & Then
        mockMvc.perform(post("/students/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete_student"))
                .andExpect(model().attribute("message", is("Student deleted successfully")));
    }

    @Test
    public void testUpdateStudentPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/students/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_student"))
                .andExpect(model().attributeExists("student"));
    }

}
