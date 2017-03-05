package org.currs.model;

/**
 * Data repository
 */
public class Repository implements IRepository {

    @Override
    public String[] getAvailableCurrencies() {
        return new String[0];
    }
}
