package com.barbutov.network_of_giving.data.dtos;

public class CharityResponseDto {
    private long id;
    private String name;
    private String description;
    private int desiredParticipants;
    private double budgetRequired;
    private int volunteersCount;
    private double collectedAmount;
    private long creatorId;
    private String thumbnail;

    public CharityResponseDto() {
    }

    public CharityResponseDto(long id, String name, String description, int desiredParticipants,
                              double budgetRequired, int volunteersCount, double collectedAmount, long creatorId,
                              String thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
        this.desiredParticipants = desiredParticipants;
        this.budgetRequired = budgetRequired;
        this.volunteersCount = volunteersCount;
        this.collectedAmount = collectedAmount;
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
