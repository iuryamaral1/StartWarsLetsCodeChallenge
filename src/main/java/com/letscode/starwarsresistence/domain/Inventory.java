package com.letscode.starwarsresistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

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

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private Set<Item> items;

    public Inventory() {
        this.id = UUID.randomUUID();
        this.isNegotiable = true;
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

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public static class InventoryRequest {
        private RebelSoldier rebelSoldier;
        private Set<Item> items;

        public RebelSoldier getRebelSoldier() {
            return rebelSoldier;
        }

        public void setRebelSoldier(RebelSoldier rebelSoldier) {
            this.rebelSoldier = rebelSoldier;
        }

        public Set<Item> getItems() {
            return items;
        }

        public void setItems(Set<Item> items) {
            this.items = items;
        }

        public Inventory toInventory() {
            Inventory inventory = new Inventory();
            inventory.setItems(this.items);
            inventory.setRebelSoldier(this.rebelSoldier);
            return inventory;
        }
    }
}
