package com.example.chichakchessapi.app.players.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @Column(name = "id", nullable = false, columnDefinition = "VARCHAR(36)")
    String id;

    @Column(name = "nickname", nullable = false, columnDefinition = "VARCHAR(20)")
    String nickname;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(256)")
    String email;

    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(10)")
    String role;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(60)")
    String password;

    @Column(name = "image_url", columnDefinition = "VARCHAR(2000)")
    String imageURL;

    @Column(name = "points", nullable = false, columnDefinition = "INT")
    @Min(value = 0)
    Integer points;
}
