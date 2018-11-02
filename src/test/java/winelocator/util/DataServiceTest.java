package winelocator.util;

import winelocator.App;
import winelocator.domain.Data;
import winelocator.service.DataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class DataServiceTest {
    @Autowired
    DataService dataService;
    @Autowired
    SpreadsheetUtil spreadsheetUtil;

    @Test
    public void testGetDataFromSmallXlsx(){
        List<Data> data = null;
        File xlsxFile = null;
        try {
            xlsxFile = new ClassPathResource("BRUT-10-17-SHORT.xlsx").getFile();
//            xlsxFile = new ClassPathResource("CONCORD 10-17.xlsx").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            data = dataService.getDataFromXlsxLines(spreadsheetUtil.getLinesFromXlsx(new FileInputStream(xlsxFile)), xlsxFile.getName(), 1, "Brut");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals("data should have one element", 1, data.size());
        assertEquals("version is 1", 1, data.get(0).getVersion());
        assertEquals("filename should be BRUT-10-17-SHORT.xlsx", "BRUT-10-17-SHORT.xlsx", data.get(0).getSpreadsheet());
        assertEquals("customer is TOTAL WINE & MORE #1803 -- SLM -- 1000110199",
                "TOTAL WINE & MORE #1803 -- SLM -- 1000110199", data.get(0).getCustomer());
        assertEquals("address is 1781 CLARKSON ROAD", "1781 CLARKSON ROAD", data.get(0).getAddress());
        assertEquals("city is CHESTERFIELD, MO", "CHESTERFIELD, MO", data.get(0).getCity());
        assertEquals("zip is 63017", "63017", data.get(0).getZip());
        assertTrue("lng is 38.6425934", 38.6425934 == data.get(0).getLocation().getCoordinates().get(1));
        assertTrue("lat is -90.5692072", -90.5692072 == data.get(0).getLocation().getCoordinates().get(0));
    }
}
