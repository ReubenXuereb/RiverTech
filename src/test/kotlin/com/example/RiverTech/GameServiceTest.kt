package com.example.RiverTech

import com.example.RiverTech.entities.Bet
import com.example.RiverTech.entities.Player
import com.example.RiverTech.entities.Transaction
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
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals

@SpringBootTest
class GameServiceTest {

    private lateinit var gameService: GameService
    private lateinit var playerRepository: PlayerRepository
    private lateinit var betRepository: BetRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var random: Random


    @BeforeEach
    fun setUp() {
        playerRepository = mock(PlayerRepository::class.java)
        betRepository = mock(BetRepository::class.java)
        transactionRepository = mock(TransactionRepository::class.java)
        random = mock(Random::class.java)
        gameService = GameService(playerRepository, betRepository, transactionRepository, random)

        gameService = GameService(
            playerRepository = playerRepository,
            betRepository = betRepository,
            transactionRepository = transactionRepository,
            random = random
        )
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

    @Test
    fun `placing a bet should reward 10x the bet amount when exactly matched`() {
        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 5
        val winnings = 1000.00
        val player = Player(1, "reu117", "reu", "xue", balance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "WIN", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "WIN", winnings)

        whenever(playerRepository.findByUsername(playerSurname)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)
        gameService.registerPlayer(playerName,playerSurname,playerUsername)

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.of(player))
        whenever(random.nextInt(1, 11)).thenReturn(generatedNumber)
        whenever(playerRepository.save(any())).thenReturn(player)
        whenever(betRepository.save(any())).thenReturn(bet)
        whenever(transactionRepository.save(any())).thenReturn(transaction)
        val result = gameService.play(player.playerId, betNumber, betAmount)

        assertEquals(1000.00, result.winnings)
        assertEquals("WIN", result.outcome)
        assertEquals(1, result.player.playerId)
        assertEquals("reu117", result.player.username)
        assertEquals(1900.00, player.balance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(3)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should reward 5x the bet amount when difference is 1`() {
        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 4
        val winnings = 500.00
        val player = Player(1, "reu117", "reu", "xue", balance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "WIN", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "WIN", winnings)

        whenever(playerRepository.findByUsername(playerSurname)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)
        gameService.registerPlayer(playerName,playerSurname,playerUsername)

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.of(player))
        whenever(random.nextInt(1, 11)).thenReturn(generatedNumber)
        whenever(playerRepository.save(any())).thenReturn(player)
        whenever(betRepository.save(any())).thenReturn(bet)
        whenever(transactionRepository.save(any())).thenReturn(transaction)
        val result = gameService.play(player.playerId, betNumber, betAmount)

        assertEquals(500.00, result.winnings)
        assertEquals("WIN", result.outcome)
        assertEquals(1, result.player.playerId)
        assertEquals("reu117", result.player.username)
        assertEquals(1400.00, player.balance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(3)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should reward half the bet amount when difference is 2`() {
        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 3
        val winnings = 50.00
        val player = Player(1, "reu117", "reu", "xue", balance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "WIN", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "WIN", winnings)

        whenever(playerRepository.findByUsername(playerSurname)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)
        gameService.registerPlayer(playerName,playerSurname,playerUsername)

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.of(player))
        whenever(random.nextInt(1, 11)).thenReturn(generatedNumber)
        whenever(playerRepository.save(any())).thenReturn(player)
        whenever(betRepository.save(any())).thenReturn(bet)
        whenever(transactionRepository.save(any())).thenReturn(transaction)
        val result = gameService.play(player.playerId, betNumber, betAmount)

        assertEquals(50.00, result.winnings)
        assertEquals("WIN", result.outcome)
        assertEquals(1, result.player.playerId)
        assertEquals("reu117", result.player.username)
        assertEquals(950.00, player.balance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(3)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should not reward any money when difference is 3 or more`() {
        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 2
        val winnings = 0.00
        val player = Player(1, "reu117", "reu", "xue", balance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "LOSS", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "LOSS", winnings)

        whenever(playerRepository.findByUsername(playerSurname)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)
        gameService.registerPlayer(playerName,playerSurname,playerUsername)

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.of(player))
        whenever(random.nextInt(1, 11)).thenReturn(generatedNumber)
        whenever(playerRepository.save(any())).thenReturn(player)
        whenever(betRepository.save(any())).thenReturn(bet)
        whenever(transactionRepository.save(any())).thenReturn(transaction)
        val result = gameService.play(player.playerId, betNumber, betAmount)

        assertEquals(0.00, result.winnings)
        assertEquals("LOSS", result.outcome)
        assertEquals(1, result.player.playerId)
        assertEquals("reu117", result.player.username)
        assertEquals(900.00, player.balance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(2)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should not allow player to bet when having insufficient funds`() {
        val playerName = "reu"
        val playerSurname = "xue"
        val playerUsername = "reu117"
        val betAmount = 100.0
        val betNumber = 5
        val player = Player(1, "reu117", "reu", "xue", balance = 50.00)

        whenever(playerRepository.findByUsername(playerSurname)).thenReturn(null)
        whenever(playerRepository.save(any())).thenReturn(player)
        gameService.registerPlayer(playerName,playerSurname,playerUsername)

        assertThrows<IllegalStateException> {
            gameService.play(player.playerId, betNumber, betAmount)
        }.message

        assertEquals(50.00, player.balance)

        verify(playerRepository, times(1)).save(any())
        verify(betRepository, times(0)).save(any())
        verify(transactionRepository, times(0)).save(any())
    }

}