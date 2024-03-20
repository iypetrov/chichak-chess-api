package com.example.chichakchessapi.app.gameparticipants;

import com.example.chichakchessapi.app.common.PaginationInfo;
import com.example.chichakchessapi.app.gameparticipants.dtos.GameParticipantResponseDTO;

import com.example.chichakchessapi.app.gameparticipants.models.GameParticipantModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game-participants")
public class GameParticipantController {
    private final GameParticipantService gameParticipantService;

    public GameParticipantController(GameParticipantService gameParticipantService) {
        this.gameParticipantService = gameParticipantService;
    }

    @GetMapping("/player")
    public ResponseEntity<GameParticipantResponseDTO> getPlayerParticipantByGameIDAndPlayerID(
            @Nullable @RequestParam() String playerID,
            @Nullable @RequestParam() String gameID
    ) {
        PaginationInfo<GameParticipantModel> gameParticipants = gameParticipantService.getGameParticipantsByPlayerAndGameID(
                playerID,
                gameID
        );

        List<GameParticipantResponseDTO> gameParticipantResponses =  GameParticipantMapper.convertGameParticipantModelsToGameParticipantResponseDTOs(
                gameParticipants.getPage()
        );

        if (gameParticipantResponses.isEmpty()) {
            return ResponseEntity.ok().body(new GameParticipantResponseDTO(null, null, null, null, null, null, null));
        }

        return ResponseEntity.ok().body(gameParticipantResponses.get(0));
    }


    @GetMapping
    public ResponseEntity<List<GameParticipantResponseDTO>> getAllByCriteria(
            @Nullable @RequestParam(required = false) String playerID,
            @Nullable @RequestParam(required = false) String gameID,
            @NotNull @RequestParam Integer pageNumber,
            @NotNull @RequestParam Integer pageSize
    ) {
        PaginationInfo<GameParticipantModel> gameParticipants = gameParticipantService.getAllGameParticipantsByCriteria(
                playerID,
                gameID,
                pageNumber,
                pageSize
        );

        return ResponseEntity.ok()
                .body(
                        GameParticipantMapper.convertGameParticipantModelsToGameParticipantResponseDTOs(
                                gameParticipants.getPage()
                        )
                );
    }
}