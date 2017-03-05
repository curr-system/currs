package org.currs.model;

/**
 * Test repository
 */
public class MockRepository implements  IRepository {

    private final String[] currencies = { "EUR", "USD", "CHF" };

    @Override
    public String[] getAvailableCurrencies() {
        return currencies.clone();
    }
}
