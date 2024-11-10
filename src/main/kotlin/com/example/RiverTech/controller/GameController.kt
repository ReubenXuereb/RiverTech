package com.example.RiverTech.controller

import com.example.RiverTech.entities.Bet
import com.example.RiverTech.entities.Player
import com.example.RiverTech.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.HashSet

@RestController
@RequestMapping("/game")
class GameController(private val gameService: GameService) {

    @PostMapping("/register")
    fun registerPlayer(@RequestBody player: Player): Player {
         return gameService.registerPlayer(player.name, player.surname, player.username)
    }

    @PostMapping("/play")
    fun placeBet(@RequestBody bet: Bet): Bet {
        return gameService.play(bet.player.playerId, bet.betNumber, bet.betAmount)
    }

    @GetMapping("/leaderboard")
    fun getLeaderboard(): List<Player> {
        return gameService.getTopPlayers()
    }
}