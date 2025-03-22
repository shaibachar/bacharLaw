package com.bachar.law.repository;

import com.bachar.law.domain.Fee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FeeRepositoryWithBagRelationshipsImpl implements FeeRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String FEES_PARAMETER = "fees";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Fee> fetchBagRelationships(Optional<Fee> fee) {
        return fee.map(this::fetchLinkedFees);
    }

    @Override
    public Page<Fee> fetchBagRelationships(Page<Fee> fees) {
        return new PageImpl<>(fetchBagRelationships(fees.getContent()), fees.getPageable(), fees.getTotalElements());
    }

    @Override
    public List<Fee> fetchBagRelationships(List<Fee> fees) {
        return Optional.of(fees).map(this::fetchLinkedFees).orElse(Collections.emptyList());
    }

    Fee fetchLinkedFees(Fee result) {
        return entityManager
            .createQuery("select fee from Fee fee left join fetch fee.linkedFees where fee.id = :id", Fee.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Fee> fetchLinkedFees(List<Fee> fees) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, fees.size()).forEach(index -> order.put(fees.get(index).getId(), index));
        List<Fee> result = entityManager
            .createQuery("select fee from Fee fee left join fetch fee.linkedFees where fee in :fees", Fee.class)
            .setParameter(FEES_PARAMETER, fees)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
