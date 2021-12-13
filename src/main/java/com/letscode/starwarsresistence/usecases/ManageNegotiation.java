package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.domain.Item;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.letscode.starwarsresistence.domain.Inventory.NegotiationOperationResponse.NegotiationStatus.FAILED;

@Component
public class ManageNegotiation {

    private static List<String> CONFIGS_TO_BE_LOADED = List.of(
            "WEAPON", "WATER", "FOOD", "MUNITION"
    );

    private ManageRebelSoldier manageRebelSoldier;
    private ManageBusinessConfiguration manageBusinessConfiguration;

    public ManageNegotiation(ManageRebelSoldier manageRebelSoldier, ManageBusinessConfiguration manageBusinessConfiguration) {
        this.manageRebelSoldier = manageRebelSoldier;
        this.manageBusinessConfiguration = manageBusinessConfiguration;
    }

    public Inventory.NegotiationOperationResponse negotiateItems(Inventory.NegotiationRequest negotiation) {
        Optional<RebelSoldier> optionalBuyer = this.manageRebelSoldier.findById(negotiation.getBuyerId());
        Optional<RebelSoldier> optionalSeller = this.manageRebelSoldier.findById(negotiation.getSellerId());

        if (!bothSoldierOfNegotiationExist(optionalBuyer, optionalSeller)) {
            return new Inventory.NegotiationOperationResponse("Both traders need to be registered!", FAILED);
        }

        if (!bothTradersHaveSamePunctuationForNegotiation(negotiation)) {
            return new Inventory.NegotiationOperationResponse("You both need to have the same punctuation for negotiation", FAILED);
        }

        RebelSoldier buyer = optionalBuyer.get();
        RebelSoldier seller = optionalSeller.get();
        if (!isNegotiable(buyer, seller, negotiation)) {
            return new Inventory.NegotiationOperationResponse("Forbidden negotiation!", FAILED);
        }

        var soldiersWithUpdatedItems = transferItems(buyer, seller, negotiation);
        var response = new Inventory.NegotiationOperationResponse("Your negotiation was successful!", Inventory.NegotiationOperationResponse.NegotiationStatus.SUCCESS);
        response.setSoldiersWithUpdatedItems(soldiersWithUpdatedItems);
        return response;
    }

    private List<RebelSoldier> transferItems(RebelSoldier buyer, RebelSoldier seller, Inventory.NegotiationRequest negotiation) {
       negotiation.getBuyerItems().forEach(buyerItem -> {
           buyer.removeItem(buyerItem.getType(), buyerItem.getAmount());
           seller.addItem(buyerItem.getType(), buyerItem.getAmount());
       });
       negotiation.getSellerItems().forEach(sellerItem -> {
           seller.removeItem(sellerItem.getType(), sellerItem.getAmount());
           buyer.addItem(sellerItem.getType(), sellerItem.getAmount());
       });
       return List.of(buyer, seller).stream().map(rebelSoldier ->
               this.manageRebelSoldier.updateSoldierItems(rebelSoldier)
       ).collect(Collectors.toList());
    }

    private boolean isNegotiable(RebelSoldier buyer, RebelSoldier seller, Inventory.NegotiationRequest negotiation) {
        return (
            !hasTraitorInNegotiation(buyer, seller) &&
            !hasInactiveSoldierOrBlockedInventoryInNegotiation(buyer, seller) &&
            tradersHaveEnoughtItemsForNegotiation(buyer, seller, negotiation)
        );
    }

    private boolean bothSoldierOfNegotiationExist(Optional<RebelSoldier> buyer, Optional<RebelSoldier> seller) {
        return buyer.isPresent() && seller.isPresent();
    }

    public boolean bothTradersHaveSamePunctuationForNegotiation(Inventory.NegotiationRequest negotiation) {
        Map<String, Integer> itemValues = this.manageBusinessConfiguration.loadItemValues(CONFIGS_TO_BE_LOADED);
        Integer totalPointsBuyer = calculatePoints(negotiation.getBuyerItems(), itemValues);
        Integer totalPointsSeller = calculatePoints(negotiation.getSellerItems(), itemValues);

        return totalPointsSeller == totalPointsBuyer;
    }

    private boolean hasInactiveSoldierOrBlockedInventoryInNegotiation(RebelSoldier buyer, RebelSoldier seller) {
        return (List.of(buyer, seller).stream().anyMatch(rebelSoldier -> (!rebelSoldier.isActive() || !rebelSoldier.getInventory().isNegotiable())));
    }

    public boolean tradersHaveEnoughtItemsForNegotiation(RebelSoldier buyer, RebelSoldier seller, Inventory.NegotiationRequest negotiation) {
        boolean buyerHasItemsForNegotiation = hasItemsAvailableToNegotiate(negotiation.getBuyerItems(), buyer.getInventory().getItems());
        boolean sellerHasItemsForNegotiation = hasItemsAvailableToNegotiate(negotiation.getSellerItems(), seller.getInventory().getItems());

        return buyerHasItemsForNegotiation && sellerHasItemsForNegotiation;
    }

    private boolean hasItemsAvailableToNegotiate(Set<Item> itemsToNegotiate, Set<Item> rebelItems) {
        return itemsToNegotiate.stream().allMatch(negotiableItem -> {
            var foundItems = rebelItems.stream().filter(rebelItem -> rebelItem.getType().equals(negotiableItem.getType())).collect(Collectors.toList());
            Optional<Item> optionalFoundItem = foundItems.stream().findFirst();
            return (
                    (optionalFoundItem.isPresent()) &&
                    (optionalFoundItem.get().getAmount() >= negotiableItem.getAmount())
            );
        });
    }

    private Integer calculatePoints(Set<Item> items, Map<String, Integer> itemValues) {
        return items.stream()
                .map(item -> itemValues.get(item.getType().name()) * item.getAmount())
                .reduce(0, Integer::sum);
    }

    private boolean hasTraitorInNegotiation(RebelSoldier buyer, RebelSoldier seller) {
        return (List.of(buyer, seller).stream().anyMatch(rebelSoldier -> rebelSoldier.isTraitor()));
    }
}
