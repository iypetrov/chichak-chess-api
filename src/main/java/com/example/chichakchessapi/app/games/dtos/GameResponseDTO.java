package com.example.chichakchessapi.app.games.dtos;

import java.sql.Timestamp;

public record GameResponseDTO(String id, Integer fullmoveNumber, Timestamp createdOn, Timestamp finishedOn) {
}