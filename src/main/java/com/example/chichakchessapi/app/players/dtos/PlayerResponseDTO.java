package com.example.chichakchessapi.app.players.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponseDTO {
    String id;
    String nickname;
    String email;
    String role;
    String imageURL;
    Integer points;
}