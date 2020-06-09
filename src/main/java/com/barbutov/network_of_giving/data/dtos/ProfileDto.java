package com.barbutov.network_of_giving.data.dtos;

import com.barbutov.network_of_giving.data.Gender;

import java.util.List;

public class ProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private Gender gender;
    private int age;
    private List<CharityResponseDto> createdCharities;
    private List<CharityResponseDto> donatedToCharities;
    private List<CharityResponseDto> volunteeredInCharities;

    public ProfileDto() {
    }

    public ProfileDto(String firstName, String lastName, String username, Gender gender, int age,
                      List<CharityResponseDto> createdCharities, List<CharityResponseDto> donatedToCharities,
                      List<CharityResponseDto> volunteeredInCharities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.createdCharities = createdCharities;
        this.donatedToCharities = donatedToCharities;
        this.volunteeredInCharities = volunteeredInCharities;
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

    public List<CharityResponseDto> getCreatedCharities() {
        return createdCharities;
    }

    public void setCreatedCharities(List<CharityResponseDto> createdCharities) {
        this.createdCharities = createdCharities;
    }

    public List<CharityResponseDto> getDonatedToCharities() {
        return donatedToCharities;
    }

    public void setDonatedToCharities(List<CharityResponseDto> donatedToCharities) {
        this.donatedToCharities = donatedToCharities;
    }

    public List<CharityResponseDto> getVolunteeredInCharities() {
        return volunteeredInCharities;
    }

    public void setVolunteeredInCharities(List<CharityResponseDto> volunteeredInCharities) {
        this.volunteeredInCharities = volunteeredInCharities;
    }
}
