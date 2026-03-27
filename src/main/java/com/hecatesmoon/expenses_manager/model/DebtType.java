package com.hecatesmoon.expenses_manager.model;

import java.util.ArrayList;
import java.util.List;

import com.hecatesmoon.expenses_manager.dto.TypeResponse;

public enum DebtType {
    BANK("Bank loans and financial institution debts"),
    PERSONAL("Loans from individuals (non-family/friends)"),
    CARD("Credit card balances and revolving debt"),
    STORE("Retail store financing and purchase plans"),
    OTHER("Miscellaneous debts that don't fit other categories"),
    MEDICAL("Healthcare expenses, hospital bills, treatments"),
    EDUCATION("Student loans and educational expenses"),
    FAMILY("Loans or financial help from family members"),
    FRIEND("Money borrowed from friends"),
    MORTGAGE("Home loans and property mortgages"),
    CAR_LOAN("Vehicle financing and auto loans"),
    UTILITIES("Outstanding utility bills: electricity, water, gas"),
    TAXES("Tax obligations to government entities");

    private final String description;

    private DebtType(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public static List<TypeResponse> getDebtTypesList(){
         List<TypeResponse> types = new ArrayList<>();

        for (DebtType type : DebtType.values()){
            types.add(TypeResponse.from(type));
        }
        return types;
    }
}