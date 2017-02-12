package com.java.example;

/**
 * Created by Stefan Hungerbuehler on 08.02.2017.
 */
public interface Util {
    /**
     * Returns the number of cores from the actual system.
     * @return the number of cores
     */
    public static int numberOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }
}
