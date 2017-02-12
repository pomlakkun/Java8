package com.java.example;

/**
 * Created by Stefan Hungerbuehler on 12.02.2017.
 * Works also without the annotation!
 */
@FunctionalInterface
public interface Converter<F, T> {
    T convert(F from);
}
