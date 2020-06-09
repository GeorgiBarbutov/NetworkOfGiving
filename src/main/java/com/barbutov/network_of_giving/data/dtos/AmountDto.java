package com.barbutov.network_of_giving.data.dtos;

public class AmountDto {
    private double amount;

    private AmountDto() {
    }

    public AmountDto(double amount) {
        this();
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
