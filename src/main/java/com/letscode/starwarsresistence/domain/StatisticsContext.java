package com.letscode.starwarsresistence.domain;

import java.util.Map;

public class StatisticsContext {

    public static class EntityPercentageResponse {
        private Double percentage;

        public EntityPercentageResponse(Double percentage) {
            this.percentage = percentage;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    public static class ItemsAveragePerRebelResponse {

        private Map<Item.ItemType, Double> itemsMap;

        public ItemsAveragePerRebelResponse(Map<Item.ItemType, Double> itemsMap) {
            this.itemsMap = itemsMap;
        }

        public Map<Item.ItemType, Double> getItemsMap() {
            return itemsMap;
        }

        public void setItemsMap(Map<Item.ItemType, Double> itemsMap) {
            this.itemsMap = itemsMap;
        }
    }

    public static class LostPointsResponse {
        private Integer lostPoints;

        public LostPointsResponse(Integer lostPoints) {
            this.lostPoints = lostPoints;
        }

        public Integer getLostPoints() {
            return lostPoints;
        }

        public void setLostPoints(Integer lostPoints) {
            this.lostPoints = lostPoints;
        }
    }
}
