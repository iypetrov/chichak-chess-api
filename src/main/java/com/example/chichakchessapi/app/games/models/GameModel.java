package com.example.chichakchessapi.app.games.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameModel {
    private String id;
    private Integer fullmoveNumber;
    private Timestamp createdOn;
    private Timestamp finishedOn;
}