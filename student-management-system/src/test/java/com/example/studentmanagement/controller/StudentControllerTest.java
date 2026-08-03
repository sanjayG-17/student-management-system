package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        studentRepository.deleteAll();
    }

    @Test
    public void testCreateStudent_ValidDetails() throws Exception {
        StudentRequest request = StudentRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("successfully")))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.name", is("John Doe")))
                .andExpect(jsonPath("$.data.email", is("john.doe@example.com")));
    }

    @Test
    public void testCreateStudent_DuplicateEmail() throws Exception {
        // Save first student
        Student student1 = Student.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();
        studentRepository.save(student1);

        // Attempt second student with same email
        StudentRequest request = StudentRequest.builder()
                .name("Jane Doe")
                .email("john.doe@example.com")
                .phoneNumber("0987654321")
                .department("Information Technology")
                .yearOfStudy(2)
                .cgpa(9.0)
                .build();

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    public void testCreateStudent_DuplicatePhoneNumber() throws Exception {
        // Save first student
        Student student1 = Student.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();
        studentRepository.save(student1);

        // Attempt second student with same phone number
        StudentRequest request = StudentRequest.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .phoneNumber("1234567890")
                .department("Information Technology")
                .yearOfStudy(2)
                .cgpa(9.0)
                .build();

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    public void testCreateStudent_MissingMandatoryFields() throws Exception {
        // Missing name and invalid phone/email
        StudentRequest request = StudentRequest.builder()
                .email("invalid-email")
                .phoneNumber("123") // Should be 10 digits
                .yearOfStudy(5) // Max is 4
                .cgpa(11.0) // Max is 10.0
                .build();

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors.name", is("Name is mandatory")))
                .andExpect(jsonPath("$.errors.email", is("Email should be valid")))
                .andExpect(jsonPath("$.errors.phoneNumber", is("Phone number must be exactly 10 digits")))
                .andExpect(jsonPath("$.errors.department", is("Department is mandatory")))
                .andExpect(jsonPath("$.errors.yearOfStudy", is("Year of study must be at most 4")))
                .andExpect(jsonPath("$.errors.cgpa", is("CGPA must be at most 10.0")));
    }

    @Test
    public void testUpdateStudent_Existing() throws Exception {
        Student student = Student.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();
        student = studentRepository.save(student);

        StudentRequest request = StudentRequest.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phoneNumber("1234567890")
                .department("Data Science")
                .yearOfStudy(4)
                .cgpa(9.2)
                .build();

        mockMvc.perform(put("/api/students/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("John Updated")))
                .andExpect(jsonPath("$.data.email", is("john.updated@example.com")))
                .andExpect(jsonPath("$.data.department", is("Data Science")))
                .andExpect(jsonPath("$.data.yearOfStudy", is(4)))
                .andExpect(jsonPath("$.data.cgpa", is(9.2)));
    }

    @Test
    public void testUpdateStudent_NonExisting() throws Exception {
        StudentRequest request = StudentRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();

        mockMvc.perform(put("/api/students/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Student not found")));
    }

    @Test
    public void testUpdateStudent_DuplicatePhoneNumber() throws Exception {
        // Save first student
        Student student1 = Student.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();
        studentRepository.save(student1);

        // Save second student
        Student student2 = Student.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .phoneNumber("0987654321")
                .department("Information Technology")
                .yearOfStudy(2)
                .cgpa(9.0)
                .build();
        student2 = studentRepository.save(student2);

        // Try to update student2 to use student1's phone number
        StudentRequest request = StudentRequest.builder()
                .name("Jane Updated")
                .email("jane.doe@example.com")
                .phoneNumber("1234567890")
                .department("Information Technology")
                .yearOfStudy(2)
                .cgpa(9.0)
                .build();

        mockMvc.perform(put("/api/students/" + student2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    public void testDeleteStudent_Existing() throws Exception {
        Student student = Student.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();
        student = studentRepository.save(student);

        mockMvc.perform(delete("/api/students/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));
    }

    @Test
    public void testDeleteStudent_NonExisting() throws Exception {
        mockMvc.perform(delete("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void testSearchByDepartment_Existing() throws Exception {
        Student student = Student.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .department("Computer Science")
                .yearOfStudy(3)
                .cgpa(8.5)
                .build();
        studentRepository.save(student);

        mockMvc.perform(get("/api/students/search/department?dept=computer science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].department", is("Computer Science")));
    }

    @Test
    public void testSearchByDepartment_NoStudents() throws Exception {
        mockMvc.perform(get("/api/students/search/department?dept=Mathematics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void testGetStudentsByCgpa_GreaterThan() throws Exception {
        Student s1 = Student.builder().name("Alice").email("a@e.com").phoneNumber("1234567891").department("CS").yearOfStudy(1).cgpa(7.5).build();
        Student s2 = Student.builder().name("Bob").email("b@e.com").phoneNumber("1234567892").department("CS").yearOfStudy(2).cgpa(8.5).build();
        Student s3 = Student.builder().name("Charlie").email("c@e.com").phoneNumber("1234567893").department("CS").yearOfStudy(3).cgpa(9.5).build();
        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);

        mockMvc.perform(get("/api/students/search/cgpa?minCgpa=8.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[*].name", containsInAnyOrder("Bob", "Charlie")));
    }

    @Test
    public void testInvalidStudentId_TypeMismatch() throws Exception {
        mockMvc.perform(get("/api/students/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Parameter 'id' should be of type 'Long'")));
    }
}
