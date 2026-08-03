package com.example.studentmanagement.dto;

import jakarta.validation.constraints.*;

public class StudentRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Department is mandatory")
    private String department;

    @NotNull(message = "Year of study is mandatory")
    @Min(value = 1, message = "Year of study must be at least 1")
    @Max(value = 4, message = "Year of study must be at most 4")
    private Integer yearOfStudy;

    @NotNull(message = "CGPA is mandatory")
    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA must be at most 10.0")
    private Double cgpa;

    // Default Constructor
    public StudentRequest() {}

    // All-Args Constructor
    public StudentRequest(String name, String email, String phoneNumber, String department, Integer yearOfStudy, Double cgpa) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.cgpa = cgpa;
    }

    // Getters and Setters
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
    public static class StudentRequestBuilder {
        private String name;
        private String email;
        private String phoneNumber;
        private String department;
        private Integer yearOfStudy;
        private Double cgpa;

        public StudentRequestBuilder name(String name) { this.name = name; return this; }
        public StudentRequestBuilder email(String email) { this.email = email; return this; }
        public StudentRequestBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public StudentRequestBuilder department(String department) { this.department = department; return this; }
        public StudentRequestBuilder yearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; return this; }
        public StudentRequestBuilder cgpa(Double cgpa) { this.cgpa = cgpa; return this; }

        public StudentRequest build() {
            return new StudentRequest(name, email, phoneNumber, department, yearOfStudy, cgpa);
        }
    }

    public static StudentRequestBuilder builder() {
        return new StudentRequestBuilder();
    }
}
