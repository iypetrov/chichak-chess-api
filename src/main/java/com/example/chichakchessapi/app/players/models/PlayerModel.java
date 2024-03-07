package com.example.chichakchessapi.app.players.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerModel {
    String id;
    String nickname;
    String email;
    String imageURL;
    Integer points;
}
