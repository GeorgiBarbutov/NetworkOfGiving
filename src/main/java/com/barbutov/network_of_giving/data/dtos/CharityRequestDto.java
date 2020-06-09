package com.barbutov.network_of_giving.data.dtos;

public class CharityRequestDto {
    private String name;
    private String description;
    private int desiredParticipants;
    private double budgetRequired;

    public CharityRequestDto() {
    }

    public CharityRequestDto(String name, String description, int desiredParticipants, double budgetRequired) {
        this.name = name;
        this.description = description;
        this.desiredParticipants = desiredParticipants;
        this.budgetRequired = budgetRequired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDesiredParticipants() {
        return desiredParticipants;
    }

    public void setDesiredParticipants(int desiredParticipants) {
        this.desiredParticipants = desiredParticipants;
    }

    public double getBudgetRequired() {
        return budgetRequired;
    }

    public void setBudgetRequired(double budgetRequired) {
        this.budgetRequired = budgetRequired;
    }
}
