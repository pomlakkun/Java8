package com.java.example;

/**
 * Created by Stefan Hungerbuehler on 12.02.2017.
 */
public interface PersonFactory<P extends Person> {
    P create(String firstname, String lastName);
}
