package com.example.chichakchessapi.app.gameparticipants.dtos;

public record GameParticipantResponseDTO(String id, String gameID, String playerID, String color, Boolean isWinner, Boolean isDraw) {
}