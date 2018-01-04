package photosports.sainthannaz.com.photosports.models;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class Customers {
    int customer_id;
    String customer_name, customer_lastname, customer_email, customer_phone, customer_runner, customer_origin;

    public Customers() {
    }

    public Customers(int customer_id, String customer_name, String customer_lastname, String customer_email, String customer_phone, String customer_runner, String customer_origin) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_lastname = customer_lastname;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
        this.customer_runner = customer_runner;
        this.customer_origin = customer_origin;
    }


    public int getcustomer_id() {
        return customer_id;
    }
    public void setcustomer_id(int id) {
        this.customer_id = id;
    }

    public String getcustomer_name() {
        return customer_name;
    }
    public void setcustomer_name(String name) {
        this.customer_name = name;
    }

    public String getcustomer_lastname() {
        return customer_lastname;
    }
    public void setcustomer_lastname(String lastname) {
        this.customer_lastname = lastname;
    }

    public String getcustomer_email() {
        return customer_email;
    }
    public void setcustomer_email(String email) {
        this.customer_email = email;
    }

    public String getcustomer_phone() {
        return customer_phone;
    }
    public void setcustomer_phone(String phone) {
        this.customer_phone = phone;
    }

    public String getcustomer_runner() {
        return customer_runner;
    }
    public void setcustomer_runner(String runner) {
        this.customer_runner = runner;
    }

    public String getcustomer_origin() {
        return customer_origin;
    }
    public void setcustomer_origin(String origin) {
        this.customer_origin = origin;
    }

}
