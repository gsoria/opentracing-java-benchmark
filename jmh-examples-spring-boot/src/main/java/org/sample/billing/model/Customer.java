package org.sample.billing.model;

public class Customer {

    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Customer(String name, String lastName, String email, String phoneNumber){
        this.name=name;
        this.lastName=lastName;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }
}
