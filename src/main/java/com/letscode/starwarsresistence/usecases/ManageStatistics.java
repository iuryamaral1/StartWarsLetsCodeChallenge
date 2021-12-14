package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Item;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.StatisticsContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ManageStatistics {

    private static List<String> CONFIGS_TO_BE_LOADED = List.of(
            "WEAPON", "WATER", "FOOD", "MUNITION"
    );
    private RebelSoldierGateway rebelSoldierGateway;
    private ManageBusinessConfiguration manageBusinessConfiguration;

    public ManageStatistics(RebelSoldierGateway rebelSoldierGateway, ManageBusinessConfiguration manageBusinessConfiguration) {
        this.rebelSoldierGateway = rebelSoldierGateway;
        this.manageBusinessConfiguration = manageBusinessConfiguration;
    }

    public StatisticsContext.EntityPercentageResponse getTraitorPercentage() {
        var allRebels = this.rebelSoldierGateway.findAll();
        var traitors = allRebels.stream().filter(RebelSoldier::isTraitor).collect(Collectors.toList());
        var percentage = Double.valueOf(traitors.size()) / Double.valueOf(allRebels.size()) * 100.0;
        return new StatisticsContext.EntityPercentageResponse(percentage);
    }

    public StatisticsContext.EntityPercentageResponse getRebelsPercentage() {
        var allRebels = this.rebelSoldierGateway.findAll();
        var traitors = allRebels.stream().filter(RebelSoldier::isTraitor).collect(Collectors.toList());
        var percentage = Double.valueOf(allRebels.size() - traitors.size()) / Double.valueOf(allRebels.size()) * 100.0;
        return new StatisticsContext.EntityPercentageResponse(percentage);
    }

    public StatisticsContext.ItemsAveragePerRebelResponse getItemsAveragePerRebel() {
        var allRebels = this.rebelSoldierGateway.findAll();
        double waterAverage = getAllAmountOfItem(allRebels, Item.ItemType.WATER) / (double) allRebels.size();
        double munitionAverage = getAllAmountOfItem(allRebels, Item.ItemType.MUNITION) / (double) allRebels.size();
        double foodAverage = getAllAmountOfItem(allRebels, Item.ItemType.FOOD) / (double) allRebels.size();
        double weaponAverage = getAllAmountOfItem(allRebels, Item.ItemType.WEAPON) / (double) allRebels.size();

        var averageItems = Map.of(
                Item.ItemType.FOOD, foodAverage,
                Item.ItemType.MUNITION, munitionAverage,
                Item.ItemType.WATER, waterAverage,
                Item.ItemType.WEAPON, weaponAverage
        );

        return new StatisticsContext.ItemsAveragePerRebelResponse(averageItems);
    }

    private double getAllAmountOfItem(List<RebelSoldier> allRebels, Item.ItemType type) {
        return allRebels.stream()
                .map(rebelSoldier -> rebelSoldier.getInventory().getItems())
                .flatMap(items -> items.stream().filter(item -> item.getType().equals(type)))
                .flatMapToInt(item -> IntStream.of(item.getAmount()))
                .reduce(0, Integer::sum);
    }

    public StatisticsContext.LostPointsResponse getLostPointsBecauseOfTraitors() {
        var itemValues = this.manageBusinessConfiguration.loadItemValues(CONFIGS_TO_BE_LOADED);

        var allRebels = this.rebelSoldierGateway.findAll();

        var result = allRebels.stream()
                .filter(RebelSoldier::isTraitor)
                .map(rebel -> rebel.getInventory().getItems())
                .flatMap(Set::stream)
                .mapToInt(item -> (itemValues.get(item.getType().name()) * item.getAmount()))
                .sum();

        return new StatisticsContext.LostPointsResponse(result);
    }
}
