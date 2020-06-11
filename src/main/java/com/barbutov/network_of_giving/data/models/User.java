package com.barbutov.network_of_giving.data.models;

import com.barbutov.network_of_giving.data.Gender;
import com.barbutov.network_of_giving.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = Constants.USER_FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Column(nullable = false, length = Constants.USER_LAST_NAME_MAX_LENGTH)
    private String lastName;

    @Column(nullable = false, length = Constants.USER_USERNAME_MAX_LENGTH)
    private String username;

    @Column(nullable = false, length = Constants.USER_PASSWORD_MAX_LENGTH)
    private String password;

    @Transient
    private String passwordConfirm;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    @Min(Constants.USER_AGE_MIN_VALUE)
    @Max(Constants.USER_AGE_MAX_VALUE)
    private int age;

    @Column(nullable = false, length = Constants.USER_LOCATION_MAX_LENGTH)
    private String location;

    @Column(nullable = false)
    private String role;

    @JsonBackReference
    @OneToMany(mappedBy = "creator")
    private List<Charity> charities = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Donation> donations = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Volunteer> volunteers = new ArrayList<>();

    public User(String firstName, String lastName, String username, String password, Gender gender, int age,
                String location, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.role = role;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Charity> getCharities() {
        return charities;
    }

    public void setCharities(List<Charity> charities) {
        this.charities = charities;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }
}
