package com.example.chichakchessapi.app.gamestates.dtos;

import com.example.chichakchessapi.app.games.dtos.GameResponseDTO;

import java.sql.Timestamp;

public record GameStateResponseDTO(String id, GameResponseDTO game, String boardState, String activeColor, String castleAvailability, String enPassantTargetSquare, Integer halfmoveClock, Integer fullmoveNumber, Boolean isFinal, Timestamp createdOn) {
}