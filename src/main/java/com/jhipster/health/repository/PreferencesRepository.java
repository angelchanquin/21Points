package com.jhipster.health.repository;

import com.jhipster.health.domain.Preferences;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Preferences entity.
 */
@SuppressWarnings("unused")
public interface PreferencesRepository extends JpaRepository<Preferences,Long> {

    Optional<Preferences> findOneByUserLogin(String login);

}
