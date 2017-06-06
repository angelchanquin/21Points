package com.jhipster.health.repository.search;

import com.jhipster.health.domain.Blood_pressure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Blood_pressure entity.
 */
public interface Blood_pressureSearchRepository extends ElasticsearchRepository<Blood_pressure, Long> {
}
