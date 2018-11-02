package winelocator.service;

import winelocator.domain.Data;
import winelocator.domain.GoogleLocation;
import winelocator.util.LocationUtil;
import winelocator.util.SpreadsheetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataService {
    @Autowired
    LocationUtil locationUtil;

    private List<Data> data = new ArrayList<>();
    Logger logger = LoggerFactory.getLogger(SpreadsheetUtil.class);
    int counter;

    public List<Data> getDataFromXlsxLines(List<List<String>> lines, String filename, int version, String product){
        List<Data> dataList = new ArrayList<>();
        Data data;
        counter = 0;
        for(List<String> line: lines){
            logger.info("(" + ++counter + "/" + lines.size() + ") Creating data object for: " + line);
            data = new Data();
            data.setSpreadsheet(filename);
            data.setCustomer(line.get(0));
            data.setAddress(line.get(1));
            data.setCity(line.get(2));
            data.setZip(line.get(3));
            data.setProduct(product);
            data.setVersion(version);
            GoogleLocation googleLocation = new GoogleLocation();
            googleLocation.setType("Point");
            googleLocation.setCoordinates(new ArrayList<>(Arrays.asList(
                    locationUtil.getLatLngFromAddress(line.get(1) + ", " + line.get(2)).get("lng"),
                    locationUtil.getLatLngFromAddress(line.get(1) + ", " + line.get(2)).get("lat")
            )));
            data.setLocation(googleLocation);

            if(googleLocation.getCoordinates().get(0) == null || googleLocation.getCoordinates().get(1) == null){
                logger.info("Cannot add " + data + ", coords contain null");
            } else {
                logger.info("Added " + data + " to dataList");
                dataList.add(data);
            }
        }
        return dataList;
    }


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
