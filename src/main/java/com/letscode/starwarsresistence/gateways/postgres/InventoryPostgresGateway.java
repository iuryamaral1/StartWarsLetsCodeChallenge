package com.letscode.starwarsresistence.gateways.postgres;

import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.repositories.InventoryRepository;
import com.letscode.starwarsresistence.usecases.InventoryGateway;
import org.springframework.stereotype.Component;

@Component
public class InventoryPostgresGateway implements InventoryGateway {

    private InventoryRepository repository;

    public InventoryPostgresGateway(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Inventory createInventory(Inventory.InventoryRequest request) {
        return this.repository.save(request.toInventory());
    }
}
