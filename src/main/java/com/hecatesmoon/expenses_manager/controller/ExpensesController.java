package com.hecatesmoon.expenses_manager.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/public")
public class ExpensesController {

    @GetMapping("/hello/{name}")
    public String getMethodName(@PathVariable String name) {
        return "hello " + name;
    }
    
}