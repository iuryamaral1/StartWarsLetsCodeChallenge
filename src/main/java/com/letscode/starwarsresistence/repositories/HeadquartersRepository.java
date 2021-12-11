package com.letscode.starwarsresistence.repositories;

import com.letscode.starwarsresistence.domain.Headquarters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HeadquartersRepository extends CrudRepository<Headquarters, UUID> {
}
