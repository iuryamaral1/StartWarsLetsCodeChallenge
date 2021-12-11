package com.letscode.starwarsresistence.repositories;

import com.letscode.starwarsresistence.domain.Inventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, UUID> {
}
