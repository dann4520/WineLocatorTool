package winelocator.util;

import winelocator.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class LocationUtilTest {
    @Autowired
    LocationUtil locationUtil;

    Logger logger = LoggerFactory.getLogger(LocationUtilTest.class);

    //@Ignore("zipcodeapi.com api keys cycle daily")

    @Test
    public void testGetLatLongFromZip(){
        Map<String, Double> result = locationUtil.getLatLngFromAddress("65203");
        assertTrue("lat should be 38.9538484", 38.9538484 == result.get("lat"));
        assertTrue("log should be -92.3714428", -92.3714428 == result.get("lng"));

        //result = locationUtil.getLatLngFromAddress("65279");
        //assertEquals("lat should be 39.001408", 39.001408, result.get("lat"));
        //assertEquals("log should be -92.528675", -92.528675, result.get("lng"));
    }


    @Test
    public void testGetLatLongFromZipGeocache(){
        Map<String, Double> result = locationUtil.getLatLngFromAddress("1781 CLARKSON ROAD");
        assertTrue("lat should be 38.6425934", 38.6425934 == result.get("lat"));
        assertTrue("log should be -90.5692072", -90.5692072 == result.get("lng"));
    }
}
