package com.hecatesmoon.expenses_manager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hecatesmoon.expenses_manager.model.DebtEntry;
import com.hecatesmoon.expenses_manager.model.DebtType;

public class DebtEntryResponse {

    private Long id;
    private String description;
    private BigDecimal moneyAmount;
    private String creditor;
    private DebtType type;
    private Boolean isPaid;
    private Boolean isActive;
    private LocalDateTime dateLimit;
    private UserResponse user;

    public static DebtEntryResponse from (DebtEntry entry){
        DebtEntryResponse response = new DebtEntryResponse();
        response.id = entry.getId();
        response.description = entry.getDescription();
        response.moneyAmount = entry.getMoneyAmount();
        response.creditor = entry.getCreditor();
        response.type = entry.getType();
        response.isPaid = entry.getIsPaid();
        response.isActive = entry.getIsActive();
        response.dateLimit = entry.getDateLimit();
        response.user = UserResponse.from(entry.getUser());
        return response;
    }

    public Long getId() {
        return id;
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

    public UserResponse getUser() {
        return user;
    }

}
