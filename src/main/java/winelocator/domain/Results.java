package winelocator.domain;

import java.util.List;

public class Results {
    public List<AddressComponents> address_components;
    public String formatted_address;
    public Geometry geometry;
    public String place_id;
    public boolean partial_match;
    public PlusCode plus_code;
    public List<String> types;
}
