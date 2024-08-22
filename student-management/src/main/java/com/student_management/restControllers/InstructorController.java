package com.student_management.restControllers;

import com.student_management.models.Instructor;
import com.student_management.repositories.InstructorRepository;
import com.student_management.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private static final Logger LOGGER = Logger.getLogger(InstructorController.class.getName());

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping
    public ResponseEntity<?> getAllInstructors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Instructor> instructorsPage = instructorService.getAllInstructors(PageRequest.of(page, size));
            return ResponseEntity.ok(instructorsPage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving instructors: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving instructors.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInstructorById(@PathVariable Integer id) {
        try {
            Optional<Instructor> instructorOptional = Optional.ofNullable(instructorService.findInstructorById(id));
            if (instructorOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found with id: " + id);
            }
            return ResponseEntity.ok(instructorOptional.get());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving instructor with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving instructor.");
        }
    }

    @PostMapping
    public ResponseEntity<?> addInstructor(@RequestBody Instructor instructor) {
        try {
            // Validate unique fields
            if (instructorRepository.findByName(instructor.getName()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Instructor with name " + instructor.getName() + " already exists.");
            }
            Instructor savedInstructor = instructorService.saveInstructor(instructor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInstructor);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding instructor: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding instructor.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInstructor(@PathVariable Integer id, @RequestBody Instructor instructor) {
        try {
            Optional<Instructor> existingInstructorOptional = Optional.ofNullable(instructorService.findInstructorById(id));
            if (existingInstructorOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found with id: " + id);
            }

            Instructor existingInstructor = existingInstructorOptional.get();

            // Check if the name already exists for another instructor
            if (instructor.getName() != null && !instructor.getName().equals(existingInstructor.getName())) {
                Optional<Instructor> instructorWithSameName = Optional.ofNullable(instructorRepository.findByName(instructor.getName()));
                if (instructorWithSameName.isPresent() && !instructorWithSameName.get().getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Instructor with name " + instructor.getName() + " already exists.");
                }
            }

            // Update only the fields that have changed
            if (instructor.getName() != null && !instructor.getName().equals(existingInstructor.getName())) {
                existingInstructor.setName(instructor.getName());
            }
            if (instructor.getDepartment() != null && !instructor.getDepartment().equals(existingInstructor.getDepartment())) {
                existingInstructor.setDepartment(instructor.getDepartment());
            }

            Instructor updatedInstructor = instructorService.updateInstructor(existingInstructor);
            return ResponseEntity.ok(updatedInstructor);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating instructor with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating instructor.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInstructor(@PathVariable Integer id) {
        try {
            Optional<Instructor> instructorOptional = Optional.ofNullable(instructorService.findInstructorById(id));
            if (instructorOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor not found with id: " + id);
            }
            instructorService.deleteInstructorById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Instructor deleted successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting instructor with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting instructor.");
        }
    }
}
