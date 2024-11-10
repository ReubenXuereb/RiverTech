package com.example.RiverTech.repositories

import com.example.RiverTech.entities.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TransactionRepository: JpaRepository<Transaction, Long> {
    @Query("SELECT COALESCE(SUM(t.winnings), 0) FROM Transaction t WHERE t.player.id = :playerId AND t.outcome = 'WIN'")
    fun sumWinnings(playerId: Long): Double
}