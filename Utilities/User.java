
package Utilities;

import java.util.Map;


public class User {
    private String name;
    private String email;
    private String phone;
    private String street;
    private String zip;
    private String city;
    private String state;
    private String cardNumber;
    private String cvv;
    private String month;
    private String year;
    
    public User(Map<String, String> credentials) {
        this.name = credentials.get("name");
        this.email = credentials.get("email");
        this.phone = credentials.get("phone");
        this.street = credentials.get("street");
        this.zip = credentials.get("zip");
        this.city = credentials.get("city");
        this.state = credentials.get("state");
        this.cardNumber = credentials.get("cardNumber");
        this.cvv = credentials.get("cvv");
        this.month = credentials.get("month");
        this.year = credentials.get("year");
    }
    

}
