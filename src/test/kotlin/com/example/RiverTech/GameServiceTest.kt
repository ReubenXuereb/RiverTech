package com.example.RiverTech

import com.example.RiverTech.entities.Player
import com.example.RiverTech.repositories.BetRepository
import com.example.RiverTech.repositories.PlayerRepository
import com.example.RiverTech.repositories.TransactionRepository
import com.example.RiverTech.service.GameService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@SpringBootTest
class GameServiceTest {

    private lateinit var gameService: GameService
    private lateinit var playerRepository: PlayerRepository
    private lateinit var betRepository: BetRepository
    private lateinit var transactionRepository: TransactionRepository


    @BeforeEach
    fun setUp() {
        playerRepository = mock(PlayerRepository::class.java)
        betRepository = mock(BetRepository::class.java)
        transactionRepository = mock(TransactionRepository::class.java)
        gameService = GameService(playerRepository, betRepository, transactionRepository)
    }

    @Test
    fun `registerPlayer should save and return a new player when username is unique`() {

        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val player = Player(1, playerUsername, playerName, playerSurname, 1000.00)

        whenever(playerRepository.findByUsername(playerSurname)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)

        val playerRegistered = gameService.registerPlayer(playerName,playerSurname,playerUsername)

        assertEquals(1, playerRegistered.playerId)
        assertEquals(playerName, playerRegistered.name)
        assertEquals(playerSurname, playerRegistered.surname)
        assertEquals(playerUsername, playerRegistered.username)
        assertEquals(1000.00, playerRegistered.balance)

        verify(playerRepository, times(1)).save(any())
    }

    @Test
    fun `registerPlayer should throw DuplicateUsernameException when username already exists`() {
        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val existingPlayer = Player(1, playerUsername, playerName, playerSurname, 1000.00)

        whenever(playerRepository.findByUsername(playerUsername)).thenReturn(existingPlayer)

        assertThrows<IllegalStateException> {
            gameService.registerPlayer(playerName,playerSurname, playerUsername)
        }

        verify(playerRepository, times(0)).save(any())
    }
}