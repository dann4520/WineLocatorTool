package winelocator.domain;

import java.util.List;

public class LatLngResponse {
    private String zip_code;
    private double lat;
    private double lng;
    private String city;
    private String state;
    private Timezone timezone;
    private List<CityState> acceptable_city_names;

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    public List<CityState> getAcceptable_city_names() {
        return acceptable_city_names;
    }

    public void setAcceptable_city_names(List<CityState> acceptable_city_names) {
        this.acceptable_city_names = acceptable_city_names;
    }
}
