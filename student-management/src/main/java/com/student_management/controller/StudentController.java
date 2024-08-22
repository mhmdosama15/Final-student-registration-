package com.student_management.controller;

import com.student_management.models.Student;
import com.student_management.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String viewHomePage(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/students/add")
    public String addStudentPage(Model model) {
        model.addAttribute("student", new Student());
        return "add_student";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/students/find")
    public String findStudentPage(Model model) {
        model.addAttribute("student", new Student());
        return "find_student";
    }

    @PostMapping("/students/find")
    public String findStudent(@RequestParam("id") Integer id, Model model) {
        Student student = studentService.findStudentById(id);
        model.addAttribute("student", student);
        return "find_student";
    }

    @GetMapping("/students/delete")
    public String deleteStudentPage(Model model) {
        model.addAttribute("student", new Student());
        return "delete_student";
    }

    @PostMapping("/students/delete")
    public String deleteStudent(@RequestParam("id") Integer id, Model model) {
        studentService.deleteStudentById(id);
        model.addAttribute("message", "Student deleted successfully");
        return "delete_student";
    }

    @GetMapping("/students/update")
    public String updateStudentPage(Model model) {
        model.addAttribute("student", new Student());
        return "update_student";
    }

    @PostMapping("/students/update")
    public String updateStudent(@RequestParam("id") Integer id, Model model) {
        Student student = studentService.findStudentById(id);
        if (student != null) {
            model.addAttribute("student", student);
            return "update_student_form";
        } else {
            model.addAttribute("message", "Student not found");
            return "update_student";
        }
    }

    @PostMapping("/students/update/submit")
    public String submitUpdatedStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }


}
