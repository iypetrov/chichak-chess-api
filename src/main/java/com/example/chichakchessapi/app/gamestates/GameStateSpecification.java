package com.example.chichakchessapi.app.gamestates;

import com.example.chichakchessapi.app.gamestates.entities.GameStateEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GameStateSpecification {
    public Specification<GameStateEntity> gameEquals(String gameID) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(gameID)) {
                return criteriaBuilder.and();
            }
            return criteriaBuilder.equal(root.get("game").get("id"), gameID);
        };
    }
}