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
    fun `placing a bet should reward 10x the bet amount when exactly matched`() {
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 5
        val winnings = 1000.00
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "WIN", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "WIN", winnings, 1900.00)

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
        assertEquals(1900.00, player.currentBalance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(2)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should reward 5x the bet amount when difference is 1`() {
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 4
        val winnings = 500.00
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "WIN", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "WIN", winnings, 1400.00)

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
        assertEquals(1400.00, player.currentBalance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(2)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should reward half the bet amount when difference is 2`() {
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 3
        val winnings = 50.00
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "WIN", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "WIN", winnings, 950.00)


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
        assertEquals(950.00, player.currentBalance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(2)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should not reward any money when difference is 3 or more`() {
        val betAmount = 100.0
        val betNumber = 5
        val generatedNumber = 2
        val winnings = 0.00
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 1000.00)
        val bet = Bet(1, player, betNumber, betAmount, generatedNumber, "LOSS", winnings)
        val transaction = Transaction(1, player, -betAmount, "BET", "LOSS", winnings, 900.00)

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
        assertEquals(900.00, player.currentBalance)
        assertEquals(-100.00, transaction.amount)

        verify(playerRepository, times(1)).save(any())
        verify(betRepository, times(1)).save(any())
        verify(transactionRepository, times(1)).save(any())
    }

    @Test
    fun `placing a bet should not allow player to bet when having insufficient funds`() {
        val betAmount = 100.0
        val betNumber = 5
        val player = Player(1, "reu117", "reu", "xue", currentBalance = 50.00)

        assertThrows<IllegalStateException> {
            gameService.play(player.playerId, betNumber, betAmount)
        }.message

        assertEquals(50.00, player.currentBalance)

        verify(playerRepository, times(0)).save(any())
        verify(betRepository, times(0)).save(any())
        verify(transactionRepository, times(0)).save(any())
    }

}