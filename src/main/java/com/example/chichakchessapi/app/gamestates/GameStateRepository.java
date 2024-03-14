package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.gamestates.entities.GameStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends JpaRepository<GameStateEntity, String>, JpaSpecificationExecutor<GameStateEntity> {
}