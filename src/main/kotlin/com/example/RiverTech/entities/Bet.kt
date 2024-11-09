package com.example.RiverTech.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.jetbrains.annotations.NotNull

data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne
    val player: Player,
    @NotNull
    val betNumber: Int,
    @NotNull
    val betAmount: Double,
    val generatedNumber: Int,
    val outcome: String,
    val winnings: Double
)