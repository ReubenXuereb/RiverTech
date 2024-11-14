package com.example.RiverTech.controller

import com.example.RiverTech.entities.Player
import com.example.RiverTech.entities.Transaction
import com.example.RiverTech.service.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/players")
class PlayerController(private val playerService: PlayerService) {

    @PostMapping("/register")
    fun registerPlayer(@RequestBody player: Player): Player {
        return playerService.registerPlayer(player.name, player.surname, player.username)
    }
    @GetMapping
    fun getAllPlayers(): List<Player> {
        return playerService.getAllPlayers()
    }

    @GetMapping("/{playerId}/wallet")
    fun getPlayerWalletTransactions(@PathVariable playerId: Long): List<Transaction> {
        return playerService.getTransactionsForPlayer(playerId)
    }
}