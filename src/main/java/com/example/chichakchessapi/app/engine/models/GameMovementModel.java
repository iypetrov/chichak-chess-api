package com.example.chichakchessapi.app.engine.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameMovementModel {
    private String gameID;
    private String playerID;
    private String movement;
}