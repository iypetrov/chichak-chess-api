package com.example.chichakchessapi.app.playerpreferences.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player_preference")
public class PlayerPreferenceEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "bright_color", nullable = false, length = 7)
    private String brightColor;

    @Column(name = "dark_color", nullable = false, length = 7)
    private String darkColor;
}