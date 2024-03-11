package com.example.chichakchessapi.app.players.dtos;

public record PlayerResponseDTO(String id, String nickname, String email, String role, String imageURL, Integer points) {
}