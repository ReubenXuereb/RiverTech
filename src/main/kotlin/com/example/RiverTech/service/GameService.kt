package com.example.RiverTech.service

import com.example.RiverTech.entities.Player
import com.example.RiverTech.repositories.BetRepository
import com.example.RiverTech.repositories.PlayerRepository
import com.example.RiverTech.repositories.TransactionRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class GameService(
    private val playerRepository: PlayerRepository,
    private val betRepository: BetRepository,
    private val transactionRepository: TransactionRepository
) {

    fun registerPlayer(name: String, surname: String, username: String): Player {

        playerRepository.findByUsername(username)?.let {
            throw IllegalStateException("Username already exists")
        }

        val playerToRegister = Player(username = username, name = name, surname = surname)
        return playerRepository.save(playerToRegister)
    }
}