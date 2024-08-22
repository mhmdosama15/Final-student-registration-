package com.student_management.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "instructors")
@Data
public class Instructor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Integer id;

    @Column(name = "instructor_department", nullable = false)
    private String department;

    @OneToMany(mappedBy = "instructor")
    private List<Student> students;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

}
