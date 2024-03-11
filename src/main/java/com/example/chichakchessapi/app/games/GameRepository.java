package com.example.chichakchessapi.app.games;

import com.example.chichakchessapi.app.games.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, String> {
}