package com.letscode.starwarsresistence.domain;

import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.EnumSet.allOf;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private UUID id;

    @OneToOne
    private RebelSoldier rebelSoldier;

    @NotNull
    @Column(name = "is_negotiable", nullable = false, columnDefinition = "boolean default true")
    private Boolean isNegotiable;

    @OneToMany(mappedBy = "inventory")
    private Map<ItemType, Integer> items = new HashMap();

    public Inventory() {
        this.id = UUID.randomUUID();
        this.isNegotiable = true;
        allOf(ItemType.class).forEach(itemType -> this.items.put(itemType, 0));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RebelSoldier getRebelSoldier() {
        return rebelSoldier;
    }

    public void setRebelSoldier(RebelSoldier rebelSoldier) {
        this.rebelSoldier = rebelSoldier;
    }

    public Boolean getNegotiable() {
        return isNegotiable;
    }

    public void setNegotiable(Boolean negotiable) {
        isNegotiable = negotiable;
    }

    public Map<ItemType, Integer> getItems() {
        return items;
    }

    public void setItems(Map<ItemType, Integer> items) {
        this.items = items;
    }

    public void addItems(ItemType itemType, Integer amount) {
        Integer amountOfThisItem = this.items.getOrDefault(itemType, 0);
        this.items.put(itemType, (amountOfThisItem + amount));
    }

    public void removeItems(ItemType itemType, Integer amountToRemove) throws ApplicationBusinessException {
        Integer amountOfThisItem = this.items.getOrDefault(itemType, 0);
        if (amountToRemove > amountOfThisItem) throw new ApplicationBusinessException("Can't remove this amount for this item type!");
        else {
            this.items.put(itemType, (amountOfThisItem - amountToRemove));
        }
    }

    public Integer totalPoints() {
        return this.items.values().stream().reduce(0, Integer::sum);
    }

    public static class InventoryRequest {

        private Map<ItemType, Integer> items;

        public Map<ItemType, Integer> getItems() {
            return items;
        }

        public void setItems(Map<ItemType, Integer> items) {
            this.items = items;
        }

        public Inventory toInventory() {
            Inventory inventory = new Inventory();
            inventory.setItems(this.items);
            return inventory;
        }
    }

    public enum ItemType {
        WATER,
        FOOD,
        MUNITION,
        WEAPON
    }
}
