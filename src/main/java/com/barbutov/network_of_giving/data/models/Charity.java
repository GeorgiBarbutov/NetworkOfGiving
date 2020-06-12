package com.barbutov.network_of_giving.data.models;

import com.barbutov.network_of_giving.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Charities")
public class Charity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = Constants.CHARITY_NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false, length = Constants.CHARITY_MAX_DESCRIPTION_LENGTH)
    private String description;

    @Min(1)
    private int desiredParticipants;

    @Min(1)
    private double budgetRequired;

    @Min(0)
    private int volunteersCount;

    @Min(0)
    private double collectedAmount;

    @JsonManagedReference
    @ManyToOne
    private User creator;

    @JsonBackReference
    @OneToMany(mappedBy = "charity")
    private List<Donation> donations = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "charity")
    private List<Volunteer> volunteers = new ArrayList<>();

    public Charity() {
    }

    public Charity(String name, String description, int desiredParticipants, double budgetRequired,
                   int volunteersCount, double collectedAmount, User creator) {
        this.name = name;
        this.description = description;
        this.desiredParticipants = desiredParticipants;
        this.budgetRequired = budgetRequired;
        this.volunteersCount = volunteersCount;
        this.collectedAmount = collectedAmount;
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getCreatorId() {
        return creator.getId();
    }

    public String getCreatorUsername() {
        return creator.getUsername();
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
}
