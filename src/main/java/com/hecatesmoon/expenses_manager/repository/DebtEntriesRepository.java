package com.hecatesmoon.expenses_manager.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hecatesmoon.expenses_manager.model.DebtEntry;

@Repository
public interface DebtEntriesRepository extends JpaRepository<DebtEntry, Long>{
    List<DebtEntry> findAll();

    Page<DebtEntry> findByUserId(Long userId, Pageable pageable);

    List<DebtEntry> findByUserIdOrderByCreatedAtDesc(Long userId);

    DebtEntry save(DebtEntry debtEntry);

    Optional<DebtEntry> findById(Long id);

    void deleteById(Long id);

    List<DebtEntry> findByIsActiveTrue();

    @Query("SELECT SUM(e.moneyAmount) FROM DebtEntry e WHERE e.user.id = :userId AND e.isPaid = false AND e.isActive = true")
    BigDecimal sumByUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM DebtEntry d WHERE d.user.id = :userId AND (:isPaid IS NULL OR d.isPaid = :isPaid) AND (:isActive IS NULL OR d.isActive = :isActive)")
    Page<DebtEntry> findByUserIdWithFilters(@Param("userId")Long userId, @Param("isPaid")Boolean isPaid, @Param("isActive")Boolean isActive, Pageable pageable);

}