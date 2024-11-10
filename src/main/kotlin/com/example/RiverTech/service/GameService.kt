package com.example.RiverTech.service

import com.example.RiverTech.entities.Bet
import com.example.RiverTech.entities.Player
import com.example.RiverTech.entities.Transaction
import com.example.RiverTech.repositories.BetRepository
import com.example.RiverTech.repositories.PlayerRepository
import com.example.RiverTech.repositories.TransactionRepository
import org.springframework.stereotype.Service
import kotlin.math.abs
import kotlin.random.Random

@Service
class GameService(
    private val playerRepository: PlayerRepository,
    private val betRepository: BetRepository,
    private val transactionRepository: TransactionRepository,
    private val random: Random
) {

    fun registerPlayer(name: String, surname: String, username: String): Player {

        playerRepository.findByUsername(username)?.let {
            throw IllegalStateException("Username already exists")
        }

        val playerToRegister = Player(username = username, name = name, surname = surname)
        return playerRepository.save(playerToRegister)
    }

    fun play(playerId: Long, betNumber: Int, betAmount: Double): Bet {
        val player = playerRepository.findById(playerId).orElseThrow { IllegalStateException("Player not found ! Please register before placing any bets.") }
        if (player.balance < betAmount) throw IllegalStateException("Insufficient funds ! Can no longer place bets.")

        player.balance -= betAmount
        playerRepository.save(player)

        val generateRandomNumber = random.nextInt(1,11)
        val winnings = calculate(betNumber, generateRandomNumber, betAmount)
        val gameOutcome = if (winnings > 0) "WIN" else "LOSS"

        if (winnings > 0) {
            player.balance += winnings
            playerRepository.save(player)
        } else {
            println("Player lost")
        }

        if (winnings > 0) {
            transactionRepository.save(Transaction(player = player, amount = -betAmount, type = "BET", outcome = "WIN", winnings = winnings))
        } else {
            transactionRepository.save(Transaction(player = player, amount = -betAmount, type = "BET", outcome = "LOSS", winnings = winnings))
        }

        return betRepository.save(
            Bet(
                player = player,
                betNumber = betNumber,
                betAmount = betAmount,
                generatedNumber = generateRandomNumber,
                outcome = gameOutcome,
                winnings = winnings
            )
        )
    }

    fun getTopPlayers(): List<Player> {
        return playerRepository.findAll()
            .sortedByDescending { player -> transactionRepository.sumWinnings(player.playerId) }
            .take(10)
    }

    private fun calculate(betNumber: Int, generatedNumber: Int, betAmount: Double): Double {
        val difference = abs(betNumber - generatedNumber)

        return when (difference) {
            0 -> betAmount * 10
            1 -> betAmount * 5
            2 -> betAmount * 0.5
            else -> 0.0
        }
    }

}