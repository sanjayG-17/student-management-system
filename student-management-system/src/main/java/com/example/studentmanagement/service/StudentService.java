package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.DuplicatePhoneNumberException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("A student with email '" + request.getEmail() + "' already exists");
        }

        if (studentRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicatePhoneNumberException("A student with phone number '" + request.getPhoneNumber() + "' already exists");
        }

        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .yearOfStudy(request.getYearOfStudy())
                .cgpa(request.getCgpa())
                .build();

        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Transactional
    public Student updateStudent(Long id, StudentRequest request) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        if (studentRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateEmailException("Another student with email '" + request.getEmail() + "' already exists");
        }

        if (studentRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), id)) {
            throw new DuplicatePhoneNumberException("Another student with phone number '" + request.getPhoneNumber() + "' already exists");
        }

        existingStudent.setName(request.getName());
        existingStudent.setEmail(request.getEmail());
        existingStudent.setPhoneNumber(request.getPhoneNumber());
        existingStudent.setDepartment(request.getDepartment());
        existingStudent.setYearOfStudy(request.getYearOfStudy());
        existingStudent.setCgpa(request.getCgpa());

        return studentRepository.save(existingStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Student> searchByDepartment(String department) {
        return studentRepository.findByDepartmentIgnoreCase(department);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsWithCgpaGreaterThan(double cgpa) {
        return studentRepository.findByCgpaGreaterThan(cgpa);
    }
}
