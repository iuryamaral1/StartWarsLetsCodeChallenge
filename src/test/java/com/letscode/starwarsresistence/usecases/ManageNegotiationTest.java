package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.domain.Item;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.fixtures.RebelSoldierFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ManageNegotiationTest {

    @Mock
    private ManageRebelSoldier manageRebelSoldier;

    @Mock
    private ManageBusinessConfiguration manageBusinessConfiguration;

    @InjectMocks
    private ManageNegotiation manageNegotiation;

    @Test
    public void should_not_negotiate_when_one_or_both_soldiers_does_not_exist() {
        RebelSoldier buyer = new RebelSoldier();
        RebelSoldier seller = null;
        var sellerId = UUID.randomUUID();

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setBuyerId(buyer.getId());
        negotiation.setBuyerItems(Set.of());
        negotiation.setSellerId(sellerId);
        negotiation.setSellerItems(Set.of());

        var expectedAnswer = new Inventory.NegotiationOperationResponse("Both traders need to be registered!", Inventory.NegotiationOperationResponse.NegotiationStatus.FAILED);

        Mockito.when(manageRebelSoldier.findById(buyer.getId())).thenReturn(Optional.of(buyer));
        Mockito.when(manageRebelSoldier.findById(sellerId)).thenReturn(Optional.ofNullable(seller));

        var result = manageNegotiation.negotiateItems(negotiation);

        Assertions.assertEquals(result.getMessage(), expectedAnswer.getMessage());
        Assertions.assertEquals(result.getNegotiationStatus(), expectedAnswer.getNegotiationStatus());
    }

    @Test
    public void should_not_negotiate_when_punctuation_is_not_equal() {
        var buyer = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setSellerItems(seller.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.WATER)).collect(Collectors.toSet()));
        negotiation.setSellerId(seller.getId());
        negotiation.setBuyerItems(buyer.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.FOOD)).collect(Collectors.toSet()));
        negotiation.setBuyerId(buyer.getId());

        var expectedAnswer = new Inventory.NegotiationOperationResponse("You both need to have the same punctuation for negotiation", Inventory.NegotiationOperationResponse.NegotiationStatus.FAILED);

        Mockito.when(manageRebelSoldier.findById(buyer.getId())).thenReturn(Optional.of(buyer));
        Mockito.when(manageRebelSoldier.findById(seller.getId())).thenReturn(Optional.of(seller));
        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                    "WEAPON", 4,
                    "MUNITION", 3,
                    "WATER", 2,
                    "FOOD", 1
                )
        );

        var result = manageNegotiation.negotiateItems(negotiation);

        Assertions.assertEquals(result.getMessage(), expectedAnswer.getMessage());
        Assertions.assertEquals(result.getNegotiationStatus(), expectedAnswer.getNegotiationStatus());
    }

    @Test
    public void bothTradersHaveSamePunctuationForNegotiation() {
        var buyer = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        Item water = new Item();
        water.setType(Item.ItemType.WATER);
        water.setAmount(1);
        buyer.addItem(water.getType(), water.getAmount());

        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        Item food = new Item();
        food.setType(Item.ItemType.FOOD);
        food.setAmount(1);
        seller.addItem(food.getType(), food.getAmount());

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setBuyerId(buyer.getId());
        negotiation.setBuyerItems(
                buyer.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.WATER)).collect(Collectors.toSet())
        );
        negotiation.setSellerId(seller.getId());
        negotiation.setSellerItems(seller.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.WEAPON)).collect(Collectors.toSet()));

        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                        "WEAPON", 4,
                        "MUNITION", 3,
                        "WATER", 2,
                        "FOOD", 1
                )
        );

        var samePunctuation = manageNegotiation.bothTradersHaveSamePunctuationForNegotiation(negotiation);
        Assertions.assertTrue(samePunctuation);
    }

    @Test
    public void should_not_negotiate_when_there_is_traitor() {
        var buyerTraitor = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        buyerTraitor.setTraitor(true);

        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setSellerItems(seller.getInventory().getItems());
        negotiation.setSellerId(seller.getId());
        negotiation.setBuyerItems(buyerTraitor.getInventory().getItems());
        negotiation.setBuyerId(buyerTraitor.getId());

        var expectedAnswer = new Inventory.NegotiationOperationResponse("Forbidden negotiation!", Inventory.NegotiationOperationResponse.NegotiationStatus.FAILED);


        Mockito.when(manageRebelSoldier.findById(buyerTraitor.getId())).thenReturn(Optional.of(buyerTraitor));
        Mockito.when(manageRebelSoldier.findById(seller.getId())).thenReturn(Optional.of(seller));
        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                        "WEAPON", 4,
                        "MUNITION", 3,
                        "WATER", 2,
                        "FOOD", 1
                )
        );

        var result = manageNegotiation.negotiateItems(negotiation);
        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
        Assertions.assertEquals(expectedAnswer.getNegotiationStatus(), result.getNegotiationStatus());
    }

    @Test
    public void should_not_negotiate_when_soldier_is_not_active() {
        var inactiveSoldier = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        inactiveSoldier.setTraitor(false);
        inactiveSoldier.setActive(false);

        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setSellerItems(seller.getInventory().getItems());
        negotiation.setSellerId(seller.getId());
        negotiation.setBuyerItems(inactiveSoldier.getInventory().getItems());
        negotiation.setBuyerId(inactiveSoldier.getId());

        var expectedAnswer = new Inventory.NegotiationOperationResponse("Forbidden negotiation!", Inventory.NegotiationOperationResponse.NegotiationStatus.FAILED);


        Mockito.when(manageRebelSoldier.findById(inactiveSoldier.getId())).thenReturn(Optional.of(inactiveSoldier));
        Mockito.when(manageRebelSoldier.findById(seller.getId())).thenReturn(Optional.of(seller));
        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                        "WEAPON", 4,
                        "MUNITION", 3,
                        "WATER", 2,
                        "FOOD", 1
                )
        );

        var result = manageNegotiation.negotiateItems(negotiation);
        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
        Assertions.assertEquals(expectedAnswer.getNegotiationStatus(), result.getNegotiationStatus());
    }

    @Test
    public void should_not_negotiate_when_inventory_is_blocked() {
        var buyer = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        buyer.getInventory().setNegotiable(false);

        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setSellerItems(seller.getInventory().getItems());
        negotiation.setSellerId(seller.getId());
        negotiation.setBuyerItems(buyer.getInventory().getItems());
        negotiation.setBuyerId(buyer.getId());

        var expectedAnswer = new Inventory.NegotiationOperationResponse("Forbidden negotiation!", Inventory.NegotiationOperationResponse.NegotiationStatus.FAILED);

        Mockito.when(manageRebelSoldier.findById(buyer.getId())).thenReturn(Optional.of(buyer));
        Mockito.when(manageRebelSoldier.findById(seller.getId())).thenReturn(Optional.of(seller));
        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                        "WEAPON", 4,
                        "MUNITION", 3,
                        "WATER", 2,
                        "FOOD", 1
                )
        );

        var result = manageNegotiation.negotiateItems(negotiation);
        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
        Assertions.assertEquals(expectedAnswer.getNegotiationStatus(), result.getNegotiationStatus());
    }

    @Test
    public void tradersHaveEnoughItemsForNegotiation() {
        var buyer = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();

        Item food = new Item();
        food.setAmount(2);
        food.setType(Item.ItemType.FOOD);
        var buyerItemsForNegotiation = Set.of(food);

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setBuyerId(buyer.getId());
        negotiation.setBuyerItems(buyerItemsForNegotiation);
        negotiation.setSellerId(seller.getId());
        negotiation.setSellerItems(seller.getInventory().getItems());

        var result = manageNegotiation.tradersHaveEnoughtItemsForNegotiation(buyer, seller, negotiation);
        Assertions.assertFalse(result);
    }

    @Test
    public void should_negotiate_items() {
        var buyer = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var seller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        Item food = new Item();
        food.setType(Item.ItemType.FOOD);
        food.setAmount(2);
        seller.addItem(food.getType(), food.getAmount());

        Item sellerFoodNegotiation = new Item();
        sellerFoodNegotiation.setAmount(3);
        sellerFoodNegotiation.setType(Item.ItemType.FOOD);
        var sellerItems = Set.of(sellerFoodNegotiation);

        Item buyerMunitionNegotiation = new Item();
        buyerMunitionNegotiation.setType(Item.ItemType.MUNITION);
        buyerMunitionNegotiation.setAmount(1);
        var buyerItems = Set.of(buyerMunitionNegotiation);

        var negotiation = new Inventory.NegotiationRequest();
        negotiation.setSellerItems(sellerItems);
        negotiation.setSellerId(seller.getId());
        negotiation.setBuyerItems(buyerItems);
        negotiation.setBuyerId(buyer.getId());

        // Mount Expected Buyer
        var expectedUpdatedBuyer = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        expectedUpdatedBuyer.setId(buyer.getId());
        expectedUpdatedBuyer.getInventory().getItems().forEach(item -> {
            if (item.getType().equals(Item.ItemType.MUNITION)){ item.setAmount(0); }
            if (item.getType().equals(Item.ItemType.FOOD)) { item.setAmount(4); }
        });

        // Mount Expected Seller
        var expectedSeller = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        expectedSeller.setId(seller.getId());
        expectedSeller.getInventory().getItems().forEach(item -> {
            if (item.getType().equals(Item.ItemType.FOOD)) { item.setAmount(0); }
            if (item.getType().equals(Item.ItemType.MUNITION)) { item.setAmount(2); }
        });

        var expectedAnswer = new Inventory.NegotiationOperationResponse("Your negotiation was successful!", Inventory.NegotiationOperationResponse.NegotiationStatus.SUCCESS);
        expectedAnswer.setSoldiersWithUpdatedItems(List.of(expectedUpdatedBuyer, expectedSeller));

        Mockito.when(manageRebelSoldier.findById(buyer.getId())).thenReturn(Optional.of(buyer));
        Mockito.when(manageRebelSoldier.findById(seller.getId())).thenReturn(Optional.of(seller));
        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                        "WEAPON", 4,
                        "MUNITION", 3,
                        "WATER", 2,
                        "FOOD", 1
                )
        );
        Mockito.when(manageRebelSoldier.updateSoldierItems(buyer)).thenReturn(expectedUpdatedBuyer);
        Mockito.when(manageRebelSoldier.updateSoldierItems(seller)).thenReturn(expectedSeller);

        var result = manageNegotiation.negotiateItems(negotiation);

        var possibleBuyer = result.getSoldiersWithUpdatedItems().stream().findFirst().get();
        var buyerMunition = possibleBuyer.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.MUNITION)).collect(Collectors.toList()).stream().findFirst().get();
        var buyerFood = possibleBuyer.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.FOOD)).collect(Collectors.toList()).stream().findFirst().get();

        var possibleSeller = result.getSoldiersWithUpdatedItems().get(result.getSoldiersWithUpdatedItems().size() - 1);
        var sellerMunitions = possibleSeller.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.MUNITION)).collect(Collectors.toList()).stream().findFirst().get();
        var sellerFood = possibleSeller.getInventory().getItems().stream().filter(item -> item.getType().equals(Item.ItemType.FOOD)).collect(Collectors.toList()).stream().findFirst().get();

        Assertions.assertEquals(expectedAnswer.getNegotiationStatus(), result.getNegotiationStatus());
        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
        Assertions.assertEquals(buyerMunition.getAmount(), 0);
        Assertions.assertEquals(buyerFood.getAmount(), 4);
        Assertions.assertEquals(sellerMunitions.getAmount(), 2);
        Assertions.assertEquals(sellerFood.getAmount(), 0);
    }
}
