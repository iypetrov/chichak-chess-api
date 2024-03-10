package com.example.chichakchessapi.app.players;

import com.example.chichakchessapi.app.auth.models.RegisterModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class PlayerServiceTest {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerService playerService;

    @Test
    void testIfOriginalPlayersPasswordIsNotDirectlySavedInDatabase() {
        String originalPassword = "human123";
        String playerID = playerService.createPlayer(
                new RegisterModel("human", "someone@gmail.com", originalPassword)
        ).getId();

        String encryptedPassword = playerRepository.findById(playerID).orElseThrow().getPassword();
        assertNotEquals(originalPassword, encryptedPassword);
    }
}