package com.example.chichakchessapi.app.playerpreferences;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.common.UUIDUtil;
import com.example.chichakchessapi.app.playerpreferences.entities.PlayerPreferenceEntity;
import com.example.chichakchessapi.app.playerpreferences.models.PlayerPreferenceModel;
import com.example.chichakchessapi.app.players.PlayerFindService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerPreferenceService extends BaseService {
    public static final String HEX_COLOR_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    public static final String DEFAULT_BRIGHT_COLOR = "#EBECD0";
    public static final String DEFAULT_DARK_COLOR = "#779556";

    private final PlayerPreferenceRepository playerPreferenceRepository;
    private final PlayerFindService playerFindService;

    public PlayerPreferenceService(PlayerPreferenceRepository playerPreferenceRepository, PlayerFindService playerFindService) {
        this.playerPreferenceRepository = playerPreferenceRepository;
        this.playerFindService = playerFindService;
    }

    public PlayerPreferenceEntity createPlayerPreference() {
        PlayerPreferenceEntity playerPreference = new PlayerPreferenceEntity(
                UUID.randomUUID().toString(),
                DEFAULT_BRIGHT_COLOR,
                DEFAULT_DARK_COLOR
        );
        return playerPreferenceRepository.save(playerPreference);
    }

    public PlayerPreferenceModel updatePlayerPreference(
            String id,
            String brightColor,
            String  darkColor
    ) {
        Optional<String> playerID = UUIDUtil.convertFromStringToUUID(id);
        if (playerID.isEmpty()) {
            throw notSupportedOperation(
                    CustomMessageUtil.GENERAL_NOT_VALID_UUID,
                    CustomMessageUtil.GENERAL_PROVIDED_ID + id
            ).get();
        }

        PlayerModel player = playerFindService.getPlayerByID(id);
        PlayerPreferenceModel playerPreference = validatePlayerPreferences(brightColor, darkColor, player);
        playerPreferenceRepository.save(
                new PlayerPreferenceEntity(
                        player.getPlayerPreference().getId(),
                        playerPreference.getBrightColor(),
                        playerPreference.getDarkColor()
                )
        );
        return playerPreference;
    }

    private PlayerPreferenceModel validatePlayerPreferences(String brightColor, String darkColor, PlayerModel player) {
        PlayerPreferenceModel playerPreference = player.getPlayerPreference();
        if (brightColor != null && brightColor.matches(HEX_COLOR_PATTERN)) {
            playerPreference.setBrightColor(brightColor);
        }
        if (darkColor != null && darkColor.matches(HEX_COLOR_PATTERN)) {
            playerPreference.setDarkColor(darkColor);
        }
        return playerPreference;
    }
}