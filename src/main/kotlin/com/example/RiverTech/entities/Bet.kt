package com.example.RiverTech.entities

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "bets")
data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "player_id")
    val player: Player,

    @NotNull
    val betNumber: Int,
    @NotNull
    val betAmount: Double,

    val generatedNumber: Int,
    val outcome: String?,
    val winnings: Double
)