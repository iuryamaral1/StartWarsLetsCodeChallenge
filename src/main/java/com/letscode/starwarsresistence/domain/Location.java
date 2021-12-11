package com.letscode.starwarsresistence.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Location {

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double lat;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double lng;

    public Location() { }

    public Location(Double latitude, Double longitude) {
        this.lat = latitude;
        this.lng = longitude;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public static class LocationRequest {

        private Double latitude;

        private Double longitude;

        public LocationRequest(Double lat, Double lng) {
            this.latitude = lat;
            this.longitude = lng;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
}
