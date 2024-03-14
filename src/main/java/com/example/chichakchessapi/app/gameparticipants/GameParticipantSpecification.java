package com.example.chichakchessapi.app.gameparticipants;

import com.example.chichakchessapi.app.gameparticipants.entities.GameParticipantEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GameParticipantSpecification {
    public Specification<GameParticipantEntity> finishedGames() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.isNotNull(root.get("isWinner")),
                criteriaBuilder.isNotNull(root.get("isDraw"))
        );
    }

    public Specification<GameParticipantEntity> playerEquals(String playerID) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(playerID)) {
                return criteriaBuilder.and();
            }
            return criteriaBuilder.equal(root.get("player").get("id"), playerID);
        };
    }

    public Specification<GameParticipantEntity> gameEquals(String gameID) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(gameID)) {
                return criteriaBuilder.and();
            }
            return criteriaBuilder.equal(root.get("game").get("id"), gameID);
        };
    }
}