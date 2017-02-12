package com.java.example;

/**
 * Created by Stefan Hungerbuehler on 12.02.2017.
 * Taken from: http://winterbe.com/posts/2014/03/16/java-8-tutorial/
 */
public interface Formula {
    double calculate(int a);

    default double sqrt(int a){
        return Math.sqrt(a);
    }
}
