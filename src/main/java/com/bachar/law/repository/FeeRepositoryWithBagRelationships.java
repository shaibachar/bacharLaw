package com.bachar.law.repository;

import com.bachar.law.domain.Fee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FeeRepositoryWithBagRelationships {
    Optional<Fee> fetchBagRelationships(Optional<Fee> fee);

    List<Fee> fetchBagRelationships(List<Fee> fees);

    Page<Fee> fetchBagRelationships(Page<Fee> fees);
}
