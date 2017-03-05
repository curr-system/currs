package org.currs.model;

/**
 * Data repository
 */
public class Repository implements IRepository {

    @Override
    public String[] getAvailableCurrencies() {
        return new String[0];
    }

    @Override
    public String[] getCurrencyData(String name, int days, int year, int month, int day) {
        return new String[0];
    }
}
