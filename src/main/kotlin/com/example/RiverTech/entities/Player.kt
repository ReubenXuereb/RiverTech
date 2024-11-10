package com.example.RiverTech.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "players")
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val playerId: Long = 0,

    @NotNull
    val username: String,

    val name: String,
    val surname: String,
    var balance: Double = 1000.00
)





