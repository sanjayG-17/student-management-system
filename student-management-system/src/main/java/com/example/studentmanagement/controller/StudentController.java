package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.ApiResponse;
import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for managing student records")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Add a new student", description = "Creates a new student record in the system")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Student created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request details or validation errors"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Student with duplicate email already exists")
    })
    public ResponseEntity<ApiResponse<Student>> createStudent(@Valid @RequestBody StudentRequest request) {
        Student student = studentService.createStudent(request);
        ApiResponse<Student> response = ApiResponse.success("Student created successfully", student);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Retrieve all students", description = "Fetches a list of all registered student records")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        ApiResponse<List<Student>> response = ApiResponse.success("Students retrieved successfully", students);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a student by ID", description = "Fetches details of a student using their unique auto-generated ID")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid student ID format supplied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student with the supplied ID was not found")
    })
    public ResponseEntity<ApiResponse<Student>> getStudentById(
            @Parameter(description = "Unique auto-generated student ID", required = true)
            @PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        ApiResponse<Student> response = ApiResponse.success("Student retrieved successfully", student);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student details", description = "Updates details of an existing student by ID")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request details or validation errors"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student with the supplied ID was not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Another student with the updated email already exists")
    })
    public ResponseEntity<ApiResponse<Student>> updateStudent(
            @Parameter(description = "Unique auto-generated student ID to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        Student student = studentService.updateStudent(id, request);
        ApiResponse<Student> response = ApiResponse.success("Student updated successfully", student);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student", description = "Removes a student record from the database by ID")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid student ID format supplied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student with the supplied ID was not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteStudent(
            @Parameter(description = "Unique auto-generated student ID to delete", required = true)
            @PathVariable Long id) {
        studentService.deleteStudent(id);
        ApiResponse<Void> response = ApiResponse.success("Student with ID " + id + " has been deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/department")
    @Operation(summary = "Search students by department", description = "Retrieves student records matching a department name (case-insensitive)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Student>>> searchByDepartment(
            @Parameter(description = "Department name to filter by", required = true)
            @RequestParam("dept") String department) {
        List<Student> students = studentService.searchByDepartment(department);
        ApiResponse<List<Student>> response = ApiResponse.success("Students retrieved for department: " + department, students);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/cgpa")
    @Operation(summary = "Retrieve students by CGPA threshold", description = "Retrieves students whose CGPA is strictly greater than the threshold value")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Filter results retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid CGPA threshold value supplied")
    })
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByCgpa(
            @Parameter(description = "Minimum CGPA boundary (non-inclusive)", required = true)
            @RequestParam("minCgpa") double minCgpa) {
        if (minCgpa < 0.0 || minCgpa > 10.0) {
            throw new IllegalArgumentException("CGPA filter boundary must be between 0.0 and 10.0");
        }
        List<Student> students = studentService.getStudentsWithCgpaGreaterThan(minCgpa);
        ApiResponse<List<Student>> response = ApiResponse.success("Students retrieved with CGPA greater than: " + minCgpa, students);
        return ResponseEntity.ok(response);
    }
}
