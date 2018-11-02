package winelocator.util;

import winelocator.domain.GeocacheResponse;
import winelocator.domain.LatLngResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocationUtil {

    @Value("${app.zipcodeapi}")
    String zipcodeapi;

    @Value("${app.google.geocache.api}")
    String geocache_api;

    Logger logger = LoggerFactory.getLogger(LocationUtil.class);

    //TODO use different api, this one seems to break during tests w big spreadsheets
    public Map<String, Object> getLatLngFromZip(String zip){
        Map<String, Object> latLong = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        String response = "";
        stringBuilder.append("https://www.zipcodeapi.com/rest/");
        //stringBuilder.append(zipcodeapi);
        stringBuilder.append("BNpNquaoVp8jty26CbUF4E4bh9cUBPuEZSqulcXPIKIBsEhHDdsf5g6X4e19blp7");
        stringBuilder.append("/info.json/");
        stringBuilder.append(zip);
        stringBuilder.append("/degrees");
//        logger.info("url: " + stringBuilder.toString());
        try {
            response = OkHttp.get(stringBuilder.toString());
//            logger.info("reponse: " + response);
        } catch (IOException e) {
            logger.error("Error getting Url.\nmessage: " + e.getMessage() + "\nstacktrace: " + e.getStackTrace().toString());
        }

        try{
            LatLngResponse latLngResponse = objectMapper.readValue(response, LatLngResponse.class);
            latLong.put("lat", latLngResponse.getLat());
            latLong.put("lng", latLngResponse.getLng());
        } catch(IOException e){
            logger.error("Error reading value\nmessage: " + e.getMessage() + "\nstacktrace: " + e.getStackTrace().toString());
        }
        return latLong;
    }

    public Map<String, Double> getLatLngFromAddress(String address) {
        Map<String, Double> latLong = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        String response = "";
        stringBuilder.append("https://maps.googleapis.com/maps/api/geocode/json?address=");
        stringBuilder.append(address.replaceAll(" ", "+"));
        stringBuilder.append("&key=");
        stringBuilder.append(geocache_api);
//        logger.info("geocache api: " + geocache_api);
//        logger.info("url: " + stringBuilder.toString());
        try {
            response = OkHttp.get(stringBuilder.toString());
//            logger.info("response: " + response);
        } catch (IOException e) {
            logger.error("Error getting Url.\nmessage: " + e.getMessage() + "\nstacktrace: " + e.getStackTrace().toString());
        }

        try{
            GeocacheResponse geocacheResponse = objectMapper.readValue(response, GeocacheResponse.class);
            if(geocacheResponse.status.equals("OK")) {
                latLong.put("lat", geocacheResponse.results.get(0).geometry.location.lat);
                latLong.put("lng", geocacheResponse.results.get(0).geometry.location.lng);
            } else {
                logger.error("Cannot find lat/lng for " + address);
                latLong.put("lat", null);
                latLong.put("lng", null);
            }
        } catch(IOException e){
            logger.error("Error reading value\nmessage: " + e.getMessage() + "\nstacktrace: " + e.getStackTrace().toString());
        }

        return latLong;
    }
}
