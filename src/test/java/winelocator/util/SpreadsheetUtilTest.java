package winelocator.util;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpreadsheetUtil.class)
public class SpreadsheetUtilTest {

    @Autowired
    SpreadsheetUtil spreadsheetUtil;

    @Test
    public void testGetLinesFromXlsx(){
        List<List<String>> result = null;
        File xlsxFile = null;
        try {
            xlsxFile = new ClassPathResource("BRUT-10-17.xlsx").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = spreadsheetUtil.getLinesFromXlsx(new FileInputStream(xlsxFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals("xlsx should have created 36 lines", 36, result.size());
        assertEquals("customer is TOTAL WINE & MORE #1803 -- SLM -- 1000110199", "TOTAL WINE & MORE #1803 -- SLM -- 1000110199", result.get(0).get(0));
        assertEquals("address is 1781 CLARKSON ROAD", "1781 CLARKSON ROAD", result.get(0).get(1));
        assertEquals("city is CHESTERFIELD, MO", "CHESTERFIELD, MO", result.get(0).get(2));
        assertEquals("zip is 63017", "63017", result.get(0).get(3));
    }
}
