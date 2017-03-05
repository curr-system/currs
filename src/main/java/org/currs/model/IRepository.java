package org.currs.model;

/**
 * Repository interface
 */
public interface IRepository {
    String[] getAvailableCurrencies();
    String[] getCurrencyData(String name, int days, int year, int month, int day);
}
