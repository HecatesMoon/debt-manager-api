package com.hecatesmoon.expenses_manager.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hecatesmoon.expenses_manager.dto.DebtEntryRequest;
import com.hecatesmoon.expenses_manager.dto.DebtEntryResponse;
import com.hecatesmoon.expenses_manager.exception.AccessDeniedException;
import com.hecatesmoon.expenses_manager.exception.BusinessException;
import com.hecatesmoon.expenses_manager.exception.ResourceNotFoundException;
import com.hecatesmoon.expenses_manager.model.DebtEntry;
import com.hecatesmoon.expenses_manager.repository.DebtEntriesRepository;
import com.hecatesmoon.expenses_manager.repository.UsersRepository;

@Service
public class DebtEntriesService {

    private final DebtEntriesRepository debtRepository;
    private final UsersRepository usersRepository;

    public DebtEntriesService(DebtEntriesRepository debtRepository, UsersRepository usersRepository){
        this.debtRepository = debtRepository;
        this.usersRepository = usersRepository;
    }

    public List<DebtEntry> getAll(){
        return this.debtRepository.findAll();
    }

    public Page<DebtEntryResponse> getAllUserEntries(Long id, Boolean isPaid, Boolean isActive, Pageable pageable){

        if(pageable.getPageSize() > 50) {
            throw new BusinessException("Max page size is 50");
        }

        return this.debtRepository.findByUserIdWithFilters(id, isPaid, isActive, pageable).map(DebtEntryResponse::from);
    }
    
    //todo: manage null or use exception

    public DebtEntryResponse getById(Long id, Long userId){
        //todo: consider make a standard method for exception
        DebtEntry entry = this.debtRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This debt entry does not exist: " + id));

        if (!entry.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You do not have access to this entry.");
        }

        return DebtEntryResponse.from(entry);
    }

    public DebtEntryResponse saveEntry(DebtEntryRequest debtEntry, Long userId) {
        DebtEntry newEntry = DebtEntryRequest.toEntity(debtEntry);
        newEntry.setUser(this.usersRepository.findById(userId)
                                              .orElseThrow(() -> new ResourceNotFoundException("User does not exist: " + userId)));
        newEntry = this.debtRepository.save(newEntry);
        return DebtEntryResponse.from(newEntry);
    }

    public DebtEntryResponse updateEntry(DebtEntryRequest debtEntry, Long id, Long userId) {
        DebtEntry original = debtRepository.findById(id)
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found by id: " + id));

        if (!original.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You do not have access to this entry.");
        }

        DebtEntry updatedEntry = DebtEntryRequest.toEntity(debtEntry);

        updatedEntry.setId(id);
        updatedEntry.setCreatedAt(original.getCreatedAt());
        updatedEntry.setUser(original.getUser());

        updatedEntry = this.debtRepository.save(updatedEntry);

        return DebtEntryResponse.from(updatedEntry);
    }

    public void deleteEntry(Long id, Long userId) {
        //todo: apply DRY
        DebtEntry entry = this.debtRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This debt entry does not exist: " + id));

        if (!entry.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You do not have access to this entry.");
        }

        this.debtRepository.deleteById(id);
    }

    public void deleteEntry(DebtEntry entry, Long userId) {
        this.deleteEntry(entry.getId(), userId);
    }

    public BigDecimal getTotalRemainingDebt(Long id){
        return debtRepository.sumByUserId(id);
    }

}