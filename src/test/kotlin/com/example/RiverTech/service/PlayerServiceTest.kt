package com.example.RiverTech.service

import com.example.RiverTech.entities.Player
import com.example.RiverTech.repositories.PlayerRepository
import com.example.RiverTech.repositories.TransactionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class PlayerServiceTest {
    private lateinit var playerService: PlayerService
    private lateinit var playerRepository: PlayerRepository
    private lateinit var transactionRepository: TransactionRepository


    @BeforeEach
    fun setUp() {
        playerRepository = mock(PlayerRepository::class.java)
        transactionRepository = mock(TransactionRepository::class.java)
        playerService = PlayerService(playerRepository, transactionRepository)

        playerService = PlayerService(
            playerRepository = playerRepository,
            transactionRepository = transactionRepository
        )
    }

    @Test
    fun `registerPlayer should save and return a new player when username is unique`() {

        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val player = Player(1, playerUsername, playerName, playerSurname, 1000.00)

        whenever(playerRepository.findByUsername(playerUsername)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)

        val playerRegistered = playerService.registerPlayer(playerName,playerSurname,playerUsername)

        assertEquals(1, playerRegistered.playerId)
        assertEquals(playerName, playerRegistered.name)
        assertEquals(playerSurname, playerRegistered.surname)
        assertEquals(playerUsername, playerRegistered.username)
        assertEquals(1000.00, playerRegistered.currentBalance)

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
            playerService.registerPlayer(playerName,playerSurname, playerUsername)
        }

        verify(playerRepository, times(0)).save(any())
    }
}