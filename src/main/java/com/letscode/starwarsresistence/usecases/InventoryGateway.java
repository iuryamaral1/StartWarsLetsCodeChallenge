package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Inventory;

public interface InventoryGateway {

    public Inventory createInventory(Inventory.InventoryRequest request);
}
