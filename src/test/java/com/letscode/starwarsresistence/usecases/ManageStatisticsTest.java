package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Item;
import com.letscode.starwarsresistence.domain.StatisticsContext;
import com.letscode.starwarsresistence.fixtures.RebelSoldierFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ManageStatisticsTest {

    @Mock
    private ManageBusinessConfiguration manageBusinessConfiguration;

    @Mock
    private RebelSoldierGateway rebelSoldierGateway;

    @InjectMocks
    private ManageStatistics manageStatistics;

    @Test
    public void should_calculate_traitor_percentage() {

        var rebel1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var rebel2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var traitor1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        traitor1.setTraitor(true);

        var traitor2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        traitor2.setTraitor(true);

        var expectedAnswer = new StatisticsContext.EntityPercentageResponse(50.0);

        Mockito.when(this.rebelSoldierGateway.findAll()).thenReturn(List.of(rebel1, rebel2, traitor1, traitor2));

        var result = manageStatistics.getTraitorPercentage();
        Assertions.assertEquals(expectedAnswer.getPercentage(), result.getPercentage());
    }

    @Test
    public void should_calculate_rebels_percentage() {
        var rebel1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var rebel2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var traitor1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        traitor1.setTraitor(true);

        var traitor2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        traitor2.setTraitor(true);

        var expectedAnswer = new StatisticsContext.EntityPercentageResponse(50.0);

        Mockito.when(this.rebelSoldierGateway.findAll()).thenReturn(List.of(rebel1, rebel2, traitor1, traitor2));

        var result = manageStatistics.getRebelsPercentage();
        Assertions.assertEquals(expectedAnswer.getPercentage(), result.getPercentage());
    }

    @Test
    public void should_calculate_items_average_per_rebel() {
        var rebel1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var rebel2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();

        Mockito.when(this.rebelSoldierGateway.findAll()).thenReturn(List.of(rebel1, rebel2));

        Map<Item.ItemType, Double> map = Map.of(
                Item.ItemType.FOOD, 1.0,
                Item.ItemType.MUNITION, 1.0,
                Item.ItemType.WATER, 1.0,
                Item.ItemType.WEAPON, 1.0
        );
        var expectedResult = new StatisticsContext.ItemsAveragePerRebelResponse(map);

        var result = manageStatistics.getItemsAveragePerRebel();

        Assertions.assertEquals(expectedResult.getItemsMap().get(Item.ItemType.WATER), result.getItemsMap().get(Item.ItemType.WATER));
        Assertions.assertEquals(expectedResult.getItemsMap().get(Item.ItemType.MUNITION), result.getItemsMap().get(Item.ItemType.MUNITION));
        Assertions.assertEquals(expectedResult.getItemsMap().get(Item.ItemType.FOOD), result.getItemsMap().get(Item.ItemType.FOOD));
        Assertions.assertEquals(expectedResult.getItemsMap().get(Item.ItemType.WEAPON), result.getItemsMap().get(Item.ItemType.WEAPON));
    }

    @Test
    public void should_calculate_lost_points_because_of_traitors() {
        var rebel1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var rebel2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        var traitor1 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        traitor1.setTraitor(true);

        var traitor2 = RebelSoldierFixture.buildSoldierWithEachTypeOfItem();
        traitor2.setTraitor(true);

        var expectedAnswer = new StatisticsContext.LostPointsResponse(20);

        Mockito.when(this.rebelSoldierGateway.findAll()).thenReturn(List.of(rebel1, rebel2, traitor1, traitor2));
        Mockito.when(manageBusinessConfiguration.loadItemValues(Mockito.any())).thenReturn(
                Map.of(
                        "WEAPON", 4,
                        "MUNITION", 3,
                        "WATER", 2,
                        "FOOD", 1
                )
        );

        var result = manageStatistics.getLostPointsBecauseOfTraitors();

        Assertions.assertEquals(expectedAnswer.getLostPoints(), result.getLostPoints());
    }
}
