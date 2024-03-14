package com.example.chichakchessapi.app.playerpreferences;

import com.example.chichakchessapi.app.playerpreferences.entities.PlayerPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerPreferenceRepository extends JpaRepository<PlayerPreferenceEntity, String> {
}