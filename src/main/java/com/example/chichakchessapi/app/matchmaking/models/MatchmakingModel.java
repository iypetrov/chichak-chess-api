package com.example.chichakchessapi.app.matchmaking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingModel {
    private String id;
    private String nickname;
    private String email;
    private Integer points;
}