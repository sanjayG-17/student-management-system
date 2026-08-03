package com.example.studentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "phoneNumber")
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotBlank(message = "Department is mandatory")
    @Column(nullable = false)
    private String department;

    @NotNull(message = "Year of study is mandatory")
    @Min(value = 1, message = "Year of study must be at least 1")
    @Max(value = 4, message = "Year of study must be at most 4")
    @Column(nullable = false)
    private Integer yearOfStudy;

    @NotNull(message = "CGPA is mandatory")
    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA must be at most 10.0")
    @Column(nullable = false)
    private Double cgpa;

    // Default Constructor
    public Student() {}

    // All-Args Constructor
    public Student(Long id, String name, String email, String phoneNumber, String department, Integer yearOfStudy, Double cgpa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.cgpa = cgpa;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    // Manual Builder Pattern
    public static class StudentBuilder {
        private Long id;
        private String name;
        private String email;
        private String phoneNumber;
        private String department;
        private Integer yearOfStudy;
        private Double cgpa;

        public StudentBuilder id(Long id) { this.id = id; return this; }
        public StudentBuilder name(String name) { this.name = name; return this; }
        public StudentBuilder email(String email) { this.email = email; return this; }
        public StudentBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public StudentBuilder department(String department) { this.department = department; return this; }
        public StudentBuilder yearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; return this; }
        public StudentBuilder cgpa(Double cgpa) { this.cgpa = cgpa; return this; }

        public Student build() {
            return new Student(id, name, email, phoneNumber, department, yearOfStudy, cgpa);
        }
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }
}
