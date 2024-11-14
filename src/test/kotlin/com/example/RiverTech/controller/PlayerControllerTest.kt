package com.example.RiverTech.controller

import com.example.RiverTech.entities.Player
import com.example.RiverTech.entities.Transaction
import com.example.RiverTech.repositories.PlayerRepository
import com.example.RiverTech.service.PlayerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest(PlayerController::class)
class PlayerControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var playerService: PlayerService
    @MockBean
    private lateinit var playerRepository: PlayerRepository


    @Test
    fun `should register a new player`() {
        val playerRequest = mapOf("name" to "reu", "surname" to "xue", "username" to "reu117")
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1000.00)
        whenever(playerService.registerPlayer("reu", "xue", "reu117")).thenReturn(player)

        mockMvc.perform(
            post("/players/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(playerRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("reu"))
            .andExpect(jsonPath("$.surname").value("xue"))
            .andExpect(jsonPath("$.username").value("reu117"))
            .andExpect(jsonPath("$.currentBalance").value("1000.0"))
    }

    @Test
    fun `should not register a new player`() {
        val playerRequest = mapOf("name" to "reu", "surname" to "xue", "username" to "reu117")
        val existingPlayer = Player(1, "reu117", "reu", "xue", currentBalance = 1000.00)

        whenever(playerRepository.findByUsername("reu117")).thenReturn(existingPlayer)

        whenever(playerService.registerPlayer("reu", "xue", "reu117")).thenThrow(IllegalStateException("Username already exists !"))

        mockMvc.perform(
            post("/players/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(playerRequest))
        )
            .andExpect(status().is5xxServerError)
            .andExpect(jsonPath("$.message").value("Username already exists !"))
    }

    @Test
    fun `should return player's wallet transaction history`() {
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1800.00)
        val transactions = listOf(
            Transaction(1, player, -100.0, "BET", "WIN", 500.00, 1900.00),
            Transaction(2, player, -100.0, "BET", "LOSS", 0.00, 1800.00)
        )

        whenever(playerService.getTransactionsForPlayer(1)).thenReturn(transactions)

        mockMvc.perform(get("/players/1/wallet"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].transactionId").value(1))
            .andExpect(jsonPath("$[0].player.playerId").value(1))
            .andExpect(jsonPath("$[0].player.username").value("reu117"))
            .andExpect(jsonPath("$[0].player.name").value("reu"))
            .andExpect(jsonPath("$[0].player.surname").value("xue"))
            .andExpect(jsonPath("$[0].amount").value(-100.0))
            .andExpect(jsonPath("$[0].type").value("BET"))
            .andExpect(jsonPath("$[0].outcome").value("WIN"))
            .andExpect(jsonPath("$[0].winnings").value(500.0))
            .andExpect(jsonPath("$[0].balanceAfterBet").value(1900.0))
            .andExpect(jsonPath("$[1].transactionId").value(2))
            .andExpect(jsonPath("$[1].player.playerId").value(1))
            .andExpect(jsonPath("$[1].player.username").value("reu117"))
            .andExpect(jsonPath("$[1].player.name").value("reu"))
            .andExpect(jsonPath("$[1].player.surname").value("xue"))
            .andExpect(jsonPath("$[1].player.currentBalance").value(1800.0))
            .andExpect(jsonPath("$[1].amount").value(-100.0))
            .andExpect(jsonPath("$[1].type").value("BET"))
            .andExpect(jsonPath("$[1].outcome").value("LOSS"))
            .andExpect(jsonPath("$[1].winnings").value(0.0))
            .andExpect(jsonPath("$[1].balanceAfterBet").value(1800.0))
    }
}