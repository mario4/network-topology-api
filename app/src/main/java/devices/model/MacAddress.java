package devices.model;

public record MacAddress(String value) {

    public MacAddress {
//        if (value == null) {
//            throw new IllegalArgumentException("Invalid MAC Address format");
//        }
//        else if (!value.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
//            throw new IllegalArgumentException("Invalid MAC Address format");
//        }
    }


}
