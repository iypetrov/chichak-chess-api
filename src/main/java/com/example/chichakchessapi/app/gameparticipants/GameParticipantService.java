package com.example.chichakchessapi.app.gameparticipants;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.common.MapperUtil;
import com.example.chichakchessapi.app.common.PaginationInfo;
import com.example.chichakchessapi.app.gameparticipants.entities.GameParticipantEntity;
import com.example.chichakchessapi.app.gameparticipants.models.GameParticipantModel;
import com.example.chichakchessapi.app.games.entities.GameEntity;
import com.example.chichakchessapi.app.games.models.GameModel;
import com.example.chichakchessapi.app.players.entities.PlayerEntity;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameParticipantService extends BaseService {
    private final MapperUtil mapperUtil;
    private final GameParticipantRepository gameParticipantRepository;
    private final GameParticipantSpecification gameParticipantSpecification;

    public GameParticipantService(MapperUtil mapperUtil, GameParticipantRepository gameParticipantRepository, GameParticipantSpecification gameParticipantSpecification) {
        this.mapperUtil = mapperUtil;
        this.gameParticipantRepository = gameParticipantRepository;
        this.gameParticipantSpecification = gameParticipantSpecification;
    }

    public void createGameParticipant(GameModel game, PlayerModel player) {
        gameParticipantRepository.save(
                new GameParticipantEntity(
                        UUID.randomUUID().toString(),
                        mapperUtil.map(game, GameEntity.class),
                        mapperUtil.map(player, PlayerEntity.class),
                        null,
                        null
                )
        );
    }

    public PaginationInfo<GameParticipantModel> getAllGameParticipantsByCriteria(
            String playerID,
            String gameID,
            Integer pageNumber,
            Integer pageSize
    ) {
        Pageable pageable = createPage(pageNumber, pageSize);
        Specification<GameParticipantEntity> spec = Specification
                .where(gameParticipantSpecification.finishedGames())
                .and(gameParticipantSpecification.playerEquals(playerID))
                .and(gameParticipantSpecification.gameEquals(gameID));

        Page<GameParticipantEntity> gameParticipants = gameParticipantRepository.findAll(spec, pageable);
        PaginationInfo<GameParticipantModel> pageInfo = new PaginationInfo<>();
        pageInfo.setPage(
                mapperUtil.map(gameParticipants.getContent(), GameParticipantModel.class)
        );

        return pageInfo;
    }
}