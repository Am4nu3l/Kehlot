package com.example.examlab.Objects;

public class User {
    private String firstName;
    private String middleName;
    private String email;
    private String department;
    private String phoneNumber;
    private String password;
    public User(String firstName, String middleName, String email, String department, String phoneNumber, String password) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
