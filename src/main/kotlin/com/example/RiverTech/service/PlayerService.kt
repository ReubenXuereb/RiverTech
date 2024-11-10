package com.example.RiverTech.service

import com.example.RiverTech.entities.Player
import com.example.RiverTech.entities.Transaction
import com.example.RiverTech.repositories.PlayerRepository
import com.example.RiverTech.repositories.TransactionRepository
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
    private val transactionRepository: TransactionRepository
) {

    fun registerPlayer(name: String, surname: String, username: String): Player {

        playerRepository.findByUsername(username)?.let {
            throw IllegalStateException("Username already exists")
        }

        val playerToRegister = Player(username = username, name = name, surname = surname)
        return playerRepository.save(playerToRegister)
    }
    fun getAllPlayers(): List<Player> {
        return playerRepository.findAll()
    }

    fun getTransactionsForPlayer(playerId: Long): List<Transaction> {
        val player = playerRepository.findById(playerId).orElseThrow { IllegalStateException("Player not found ! Please register before placing any bets.") }
        return transactionRepository.findByPlayer(player)
    }
}