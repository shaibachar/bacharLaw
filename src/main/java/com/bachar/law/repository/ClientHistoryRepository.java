package com.bachar.law.repository;

import com.bachar.law.domain.ClientHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClientHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientHistoryRepository extends JpaRepository<ClientHistory, Long> {}
