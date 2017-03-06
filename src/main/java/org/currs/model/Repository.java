package org.currs.model;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Data repository
 */
public class Repository implements IRepository {

    private final int DB_PORT = 27017;
    private final String DB_ADDRESS = "localhost";
    private final String DB_NAME = "currs";

    public static final int DATA_PER_DAY = 8645;

    private static final BasicDBObject FIELDS = new BasicDBObject()
                                                 .append("_id",   0)
                                                 .append("date",  1)
                                                 .append("open",  1)
                                                 .append("close", 1)
                                                 .append("min",   1)
                                                 .append("max",   1);

    private static final BasicDBObject SORT = new BasicDBObject("date", -1);


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
        if(Arrays.binarySearch(getAvailableCurrencies(), name) < 0) {
            return new String[0];
        }

        // create query filter
        String dateString = String.format("%d-%02d-%02d 00:00:00", year, month, day + 1);
        BasicDBObject query = new BasicDBObject();
        query.put("date", new BasicDBObject("$lt", dateString));

        // query
        MongoCollection collection = db.getCollection(name);
        MongoCursor queryResult = collection
                .find(query)
                .projection(FIELDS)
                .sort(SORT)
                .limit(days * DATA_PER_DAY)
                .iterator();

        String json = null;
        ArrayList<String> result = new ArrayList<>();
        while (queryResult.hasNext()) {
            json = queryResult.next().toString();
            json = json.substring(9, json.length() - 1);
            result.add(json);
        }

        return result.toArray(new String[0]);
    }
}
