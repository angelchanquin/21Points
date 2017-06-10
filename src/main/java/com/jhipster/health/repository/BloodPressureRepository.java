package com.jhipster.health.repository;

import com.jhipster.health.domain.BloodPressure;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BloodPressure entity.
 */
@SuppressWarnings("unused")
public interface BloodPressureRepository extends JpaRepository<BloodPressure,Long> {

    @Query("select blood_pressure from BloodPressure blood_pressure where blood_pressure.user.login = ?#{principal.username}")
    List<BloodPressure> findByUserIsCurrentUser();

}