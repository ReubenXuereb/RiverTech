package com.example.RiverTech.repositories

import com.example.RiverTech.entities.Bet
import org.springframework.data.jpa.repository.JpaRepository

interface BetRepository: JpaRepository<Bet, Long>