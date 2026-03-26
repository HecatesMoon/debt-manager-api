package com.hecatesmoon.expenses_manager.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hecatesmoon.expenses_manager.dto.DebtEntryRequest;
import com.hecatesmoon.expenses_manager.dto.DebtEntryResponse;
import com.hecatesmoon.expenses_manager.dto.TypeResponse;
import com.hecatesmoon.expenses_manager.exception.UnauthorizedException;
import com.hecatesmoon.expenses_manager.model.DebtType;
import com.hecatesmoon.expenses_manager.service.DebtEntriesService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class DebtEntriesController {
    
    @Autowired
    private final DebtEntriesService debtService;

    public DebtEntriesController (DebtEntriesService debtService){
        this.debtService = debtService;
    }

    //User based endpoints
    @GetMapping("/api/debt/entries")
    public ResponseEntity<Page<DebtEntryResponse>> getAllEntries(
        @PageableDefault(size=10,sort="createdAt",direction=Sort.Direction.DESC) 
        Pageable pageable, @RequestParam(required = false) Boolean isPaid, @RequestParam(required = false) Boolean isActive, HttpSession session) {

        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) throw new UnauthorizedException("You need to login first");

        Page<DebtEntryResponse> page = this.debtService.getAllUserEntries(userId, isPaid, isActive, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/api/debt/entries")
    public ResponseEntity<DebtEntryResponse> addDebtEntry(@Valid @RequestBody DebtEntryRequest debtEntry, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) throw new UnauthorizedException("You need to login first");
        
        DebtEntryResponse saved = this.debtService.saveEntry(debtEntry, userId);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/api/debt/entry/{id}")
    public ResponseEntity<DebtEntryResponse> getEntryById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) throw new UnauthorizedException("You need to login first");

        DebtEntryResponse entry = this.debtService.getById(id, userId);

        return ResponseEntity.ok(entry);
    }

    @DeleteMapping("/api/debt/entry/{id}")
    public ResponseEntity<Void> deleteEntryById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) throw new UnauthorizedException("You need to login first");

        this.debtService.deleteEntry(id, userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/debt/entry/{id}")
    public ResponseEntity<DebtEntryResponse> updateEntry(@Valid @RequestBody DebtEntryRequest debtEntry, @PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) throw new UnauthorizedException("You need to login first");

        DebtEntryResponse updated = debtService.updateEntry(debtEntry, id, userId);

        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/api/debt/total-remaining")
    public ResponseEntity<Map<String,BigDecimal>> getTotalAmount(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) throw new UnauthorizedException("You need to login first");

        return ResponseEntity.ok(Map.of("total", debtService.getTotalRemainingDebt(userId)));
    }

    //general endpoints

    @GetMapping("/api/public/debt/types")
    public ResponseEntity<List<TypeResponse>> getTypeList() {
        List<TypeResponse> types = new ArrayList<>();

        for (DebtType type : DebtType.values()){
            types.add(TypeResponse.from(type));
        }

        return ResponseEntity.ok(types);
    }
}