package com.example.RiverTech.controller

import com.example.RiverTech.entities.Bet
import com.example.RiverTech.entities.Player
import com.example.RiverTech.service.GameService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(GameController::class)
class GameControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var gameService: GameService

    @Test
    fun `should place a bet and return bet details`() {
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1900.00)
        val betRequest = mapOf("betAmount" to 100, "betNumber" to 5, "player" to player)
        val betResponse = Bet(
            1L, player, 5, 100.00,
            5, "WIN", 500.00
        )

        whenever(gameService.play(1, 5, 100.00)).thenReturn(betResponse)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/game/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(betRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.betAmount").value("100.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.betNumber").value("5"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.outcome").value("WIN"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.winnings").value("500.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.generatedNumber").value("5"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.player.name").value("reu"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.player.surname").value("xue"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.player.username").value("reu117"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.player.currentBalance").value("1900.0"))

    }
}