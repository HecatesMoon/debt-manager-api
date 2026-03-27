package com.hecatesmoon.expenses_manager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hecatesmoon.expenses_manager.model.DebtEntry;
import com.hecatesmoon.expenses_manager.model.DebtType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class DebtEntryRequest {

    @Size(max = 150, message = "You can only write up to 150 characters")
    private String description;

    @NotNull(message = "Amount can't be null")
    @Positive(message = "Amount has to be greater than zero")
    private BigDecimal moneyAmount;

    @Size(max = 100, message = "You can only write up to 100 characters")
    private String creditor;

    @NotNull(message = "You have to choose a type")
    private DebtType type;

    private Boolean isPaid = false;

    private Boolean isActive = true;

    @Future(message = "The date has to be in the future")
    private LocalDateTime dateLimit;

    public static DebtEntry toEntity (DebtEntryRequest request){
        DebtEntry entry = new DebtEntry();
        entry.setDescription(request.description);
        entry.setMoneyAmount(request.moneyAmount);
        entry.setCreditor(request.creditor);
        entry.setType(request.type);
        entry.setIsPaid(request.isPaid);
        entry.setIsActive(request.isActive);
        entry.setDateLimit(request.dateLimit);
        return entry;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public String getCreditor(){
        return creditor;
    }

    public DebtType getType() {
        return type;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getDateLimit() {
        return dateLimit;
    }

}
