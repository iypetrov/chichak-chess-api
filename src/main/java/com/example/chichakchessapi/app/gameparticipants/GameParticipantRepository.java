package com.example.chichakchessapi.app.gameparticipants;

import com.example.chichakchessapi.app.gameparticipants.entities.GameParticipantEntity;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@NonNullApi
@Repository
public interface GameParticipantRepository extends JpaRepository<GameParticipantEntity, String>, PagingAndSortingRepository<GameParticipantEntity, String>, JpaSpecificationExecutor<GameParticipantEntity> {
    @Override
    @EntityGraph(attributePaths = {
            "game",
            "player",
    }, type = EntityGraph.EntityGraphType.LOAD)
    Page<GameParticipantEntity> findAll(
            @Nullable Specification<GameParticipantEntity> spec,
            Pageable pageable
    );
}