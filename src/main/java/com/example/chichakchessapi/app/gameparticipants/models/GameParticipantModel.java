package com.example.chichakchessapi.app.gameparticipants.models;

import com.example.chichakchessapi.app.engine.PieceColor;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameParticipantModel {
    private String id;
    private GameModel game;
    private PlayerModel player;
    private PieceColor color;
    private Boolean isWinner;
    private Boolean isDraw;
}