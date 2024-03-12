package com.example.chichakchessapi.app.players.entities;

import com.example.chichakchessapi.app.auth.PlayerRole;
import com.example.chichakchessapi.app.playerpreferences.entities.PlayerPreferenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "player",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class PlayerEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_preference_id", referencedColumnName = "id")
    private PlayerPreferenceEntity playerPreference;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private PlayerRole role;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "image_url", length = 2000)
    private String imageURL;

    @Column(name = "points", nullable = false)
    @Min(value = 0)
    private Integer points;
}