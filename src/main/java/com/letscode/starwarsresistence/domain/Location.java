package com.letscode.starwarsresistence.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Location {

    @NotNull
    @Column(name = "latitude", nullable = false)
    private String lat;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private String lng;

    public Location() { }

    public Location(String latitude, String longitude) {
        this.lat = latitude;
        this.lng = longitude;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public static class LocationRequest {

        private String latitude;

        private String longitude;

        public LocationRequest() { }

        public LocationRequest(String lat, String lng) {
            this.latitude = lat;
            this.longitude = lng;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
