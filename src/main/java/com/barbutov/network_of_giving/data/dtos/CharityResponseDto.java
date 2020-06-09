package com.barbutov.network_of_giving.data.dtos;

public class CharityResponseDto {
    private long id;
    private String name;
    private String description;
    private int desiredParticipants;
    private double budgetRequired;
    private int volunteersCount;
    private double collectedAmount;
    private String creatorName;

    public CharityResponseDto() {
    }

    public CharityResponseDto(long id, String name, String description, int desiredParticipants,
                              double budgetRequired, int volunteersCount, double collectedAmount, String creatorName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.desiredParticipants = desiredParticipants;
        this.budgetRequired = budgetRequired;
        this.volunteersCount = volunteersCount;
        this.collectedAmount = collectedAmount;
        this.creatorName = creatorName;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVolunteersCount() {
        return volunteersCount;
    }

    public void setVolunteersCount(int volunteersCount) {
        this.volunteersCount = volunteersCount;
    }

    public double getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(double collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
