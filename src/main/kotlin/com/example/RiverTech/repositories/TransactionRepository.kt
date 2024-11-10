package com.example.RiverTech.repositories

import com.example.RiverTech.entities.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository: JpaRepository<Transaction, Long>