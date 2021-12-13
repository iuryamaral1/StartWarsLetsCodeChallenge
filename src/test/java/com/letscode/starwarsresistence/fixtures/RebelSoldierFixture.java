package com.letscode.starwarsresistence.fixtures;

import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.domain.Item;
import com.letscode.starwarsresistence.domain.RebelSoldier;

import java.util.Set;

public class RebelSoldierFixture {

    public static RebelSoldier buildSoldierWithEachTypeOfItem() {
        Item water = new Item();
        water.setAmount(1);
        water.setType(Item.ItemType.WATER);

        Item food = new Item();
        food.setAmount(1);
        food.setType(Item.ItemType.FOOD);

        Item munition = new Item();
        munition.setAmount(1);
        munition.setType(Item.ItemType.MUNITION);

        Item weapon = new Item();
        weapon.setAmount(1);
        weapon.setType(Item.ItemType.WEAPON);

        Inventory soldierInventory = new Inventory();
        soldierInventory.setNegotiable(true);
        soldierInventory.setItems(Set.of(water, food, munition, weapon));

        RebelSoldier soldier = new RebelSoldier();
        soldier.setInventory(soldierInventory);
        return soldier;
    }
}
