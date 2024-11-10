package com.example.RiverTech.repositories

import com.example.RiverTech.entities.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository: JpaRepository<Player, Long> {

    fun findByUsername(username: String): Player?
}