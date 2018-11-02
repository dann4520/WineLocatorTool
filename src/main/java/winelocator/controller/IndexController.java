package winelocator.controller;

import winelocator.dao.MongoDao;
import winelocator.domain.Data;
import winelocator.util.LocationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    MongoDao mongoDao;

    @Autowired
    LocationUtil locationUtil;

    @Value("${app.google.geocache.api}")
    String geocache_api;

    //TODO line graph with num item rows in all spreadsheets for every product over time
    @GetMapping("/admin")
    String dashboard(){
        return "dashboard";
    }

    @RequestMapping("/login")
    String login(){
        return "login";
    }

    @RequestMapping("/find_wine")
    @ResponseBody
    public Map<String, Object> find_wine(@RequestParam("address")String address, @RequestParam("maxDistance")String maxDistance){
        Map<String, Object> response = new HashMap<>();
        Map<String, Double> location;
        List<Data> dataList;
//        logger.info("address: " + address + ", maxDistance " + maxDistance);
        location = locationUtil.getLatLngFromAddress(address);
        dataList = mongoDao.getProductsWithinRadius(new ArrayList<>(Arrays.asList(location.get("lng"), location.get("lat"))),
                Double.valueOf(maxDistance));
        response.put("results", dataList);
        response.put("lng", location.get("lng"));
        response.put("lat", location.get("lat"));
        return response;
    }

    //TODO checkboxes for products and all
    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("maps_js", "https://maps.googleapis.com/maps/api/js?key="+geocache_api+"&libraries=places");
        return mav;
    }
}
