package com.java.example;

/**
 * Created by Stefan Hungerbuehler on 12.02.2017.
 */
public class Person {
    String firstName;
    String lastName;
    static int cnt = 0;

    Person(){
        this.cnt++;
        this.firstName = "default firstName " + cnt;
        this.lastName = "default lastName " + cnt;
    }

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
