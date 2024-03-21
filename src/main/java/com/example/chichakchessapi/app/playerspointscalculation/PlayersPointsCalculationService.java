package com.example.chichakchessapi.app.playerspointscalculation;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.players.PlayerFindService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayersPointsCalculationService extends BaseService {
    public static final Integer INITIAL_PLAYER_POINTS = 500;

    private final PlayerFindService playerFindService;

    public PlayersPointsCalculationService(PlayerFindService playerFindService) {
        this.playerFindService = playerFindService;
    }

    // Returns always a list of 2 players
    // First is the winner and the second is the loser if there are any
    public List<PlayerModel> updatePointsPlayersAfterGame(String winnerID, String loserID, boolean isDraw) {
        if (!isDraw) {
            PlayerModel winner = playerFindService.getPlayerByID(winnerID);
            PlayerModel loser = playerFindService.getPlayerByID(loserID);
            int winnerPoints = winner.getPoints();
            int loserPoints = loser.getPoints();
            winner.setPoints(winnerPoints + 10);
            loser.setPoints(loserPoints - 10);
            return List.of(winner, loser);
        }
        return List.of();
    }
}