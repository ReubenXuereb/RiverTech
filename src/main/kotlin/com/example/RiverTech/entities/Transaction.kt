package com.example.RiverTech.entities

import jakarta.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val transactionId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "player_id")
    val player: Player,

    val amount: Double,
    val type: String,
    val outcome: String,
    val winnings: Double,
    val balanceAfterBet: Double
)