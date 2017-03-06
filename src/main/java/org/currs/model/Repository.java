package org.currs.model;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;

/**
 * Data repository
 */
public class Repository implements IRepository {

    private final int DB_PORT = 27017;
    private final String DB_ADDRESS = "localhost";
    private final String DB_NAME = "currs";

    private MongoClient dbClient;
    private MongoDatabase db;

    public Repository() {
    }

    public void Connect() {
        if (dbClient == null)
            Disconnect();

        dbClient = new MongoClient(DB_ADDRESS, DB_PORT);
        db = dbClient.getDatabase(DB_NAME);
    }

    public void Disconnect() {
        if (dbClient != null) {
            dbClient.close();
            dbClient = null;
            db = null;
        }
    }

    @Override
    public String[] getAvailableCurrencies() {
        if (db == null)
            return new String[0];

        ArrayList<String> result = new ArrayList<String>();
        db.listCollectionNames().forEach((Block<String>) e -> result.add(e));

        return result.toArray(new String[0]);
    }

    @Override
    public String[] getCurrencyData(String name, int days, int year, int month, int day) {
        return new String[0];
    }
}
