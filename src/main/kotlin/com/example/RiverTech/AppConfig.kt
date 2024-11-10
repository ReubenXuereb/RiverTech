package com.example.RiverTech

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.random.Random

@Configuration
class AppConfig {

    @Bean
    fun random(): Random {
        return Random
    }
}