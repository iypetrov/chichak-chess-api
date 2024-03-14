package com.example.chichakchessapi.app.playerpreferences.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPreferenceModel {
    private String id;
    private String brightColor;
    private String darkColor;
}