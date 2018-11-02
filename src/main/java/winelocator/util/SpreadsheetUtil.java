package winelocator.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpreadsheetUtil {

    @Value("${app.xlsx_skip_rows}")
    private int XLSX_SKIP_ROWS;

    Logger logger = LoggerFactory.getLogger(SpreadsheetUtil.class);
    DataFormatter dataFormatter = new DataFormatter();

    public List<List<String>> getLinesFromXlsx(InputStream is){
        List<List<String>> lines = new ArrayList<>();
        List<String> line;
        OPCPackage pkg;
        XSSFWorkbook wb = null;
        boolean goodLine;
        try {
            pkg = OPCPackage.open(is);
            wb = new XSSFWorkbook(pkg);
        } catch (IOException | InvalidFormatException e) {
            logger.info(e.getMessage());
        }

        Sheet sheet = wb.getSheetAt(0);
        for(Row row: sheet){
            if(row.getRowNum() >= XLSX_SKIP_ROWS) {
                line = new ArrayList<>();
                goodLine = false;
                for (Cell cell : row) {
                    if(cell.getColumnIndex() == 0 && !dataFormatter.formatCellValue(cell).equals("")) {
                        goodLine = true;
                    }
//                    logger.info("cell #: " + Integer.toString(cell.getColumnIndex()) + ", Value: " + dataFormatter.formatCellValue(cell));
                    line.add(dataFormatter.formatCellValue(cell));
                }
                if(goodLine) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }

}
