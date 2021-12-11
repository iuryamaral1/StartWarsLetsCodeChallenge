package com.letscode.starwarsresistence.repositories;

import com.letscode.starwarsresistence.domain.RebelSoldier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RebelSoldierRepository extends PagingAndSortingRepository<RebelSoldier, UUID> {
}
