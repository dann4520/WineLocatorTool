package winelocator.domain;

public class Data {
    private String spreadsheet;
    private String customer;
    private String address;
    private String city;
    private String zip;
    private String product;
    public GoogleLocation location;
    private int version;

    @Override
    public String toString() {
        return "Data{" +
                "spreadsheet='" + spreadsheet + '\'' +
                ", customer='" + customer + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", product='" + product + '\'' +
                ", location=" + location +
                ", version=" + version +
                '}';
    }

    public GoogleLocation getLocation() {
        return location;
    }

    public void setLocation(GoogleLocation location) {
        this.location = location;
    }

    public String getSpreadsheet() {
        return spreadsheet;
    }

    public void setSpreadsheet(String spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getProduct() { return product; }

    public void setProduct(String product) { this.product = product; }

}
