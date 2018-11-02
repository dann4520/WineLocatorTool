package winelocator.domain;

import java.util.Date;
import java.util.List;

public class Product {
    List<Spreadsheet> spreadsheets;
    String name;
    int version;
    Date lastModified;

    public List<Spreadsheet> getSpreadsheets() {
        return spreadsheets;
    }

    public void setSpreadsheets(List<Spreadsheet> spreadsheets) {
        this.spreadsheets = spreadsheets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
