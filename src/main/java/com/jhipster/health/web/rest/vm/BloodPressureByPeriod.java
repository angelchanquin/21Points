package com.jhipster.health.web.rest.vm;

import com.jhipster.health.domain.BloodPressure;

import java.util.List;

/**
 * Created by angelchanquin on 6/7/17.
 */
public class BloodPressureByPeriod {

    private String period;
    private List<BloodPressure> readings;

    public BloodPressureByPeriod(String period, List<BloodPressure> readings) {
        this.period = period;
        this.readings = readings;
    }
}
