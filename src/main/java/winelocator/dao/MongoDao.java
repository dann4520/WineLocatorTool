package winelocator.dao;

import winelocator.domain.Data;
import winelocator.domain.Product;
import winelocator.util.SpreadsheetUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class MongoDao {
//    @Value("${spring.data.mongodb.uri}")
    String MONGO_URI = "localhost";
    String db = "wine";
    final private String DATA_COLLECTION = "data";
    final private String PRODUCT_COLLECTION = "product";

    Logger logger = LoggerFactory.getLogger(SpreadsheetUtil.class);

    public MongoClient mongoClient = null;

    public void connect() {
        if (mongoClient == null) {
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            mongoClient = new MongoClient(MONGO_URI, MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        }
    }

    public void insertData(List<Data> dataList){
        if(mongoClient == null){
            connect();
        }
        MongoDatabase database = mongoClient.getDatabase(db);
        database.getCollection(DATA_COLLECTION).drop();
        MongoCollection<Data> collection = database.getCollection(DATA_COLLECTION, Data.class);
        //collection.insertMany(dataList);
        for(Data data : dataList) {
            collection.insertOne(data);
        }
        collection.createIndex(Indexes.geo2dsphere("location"));
    }

    //Finds all locations for all products within radius provided
    public List<Data> getProductsWithinRadius(ArrayList<Double> location, double maxDistance){
        if(mongoClient == null){
            connect();
        }
        List<Data> dataList = new ArrayList<>();
        Point refPoint = new Point(new Position(location.get(0), location.get(1)));
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Data> mongoCollection = database.getCollection(DATA_COLLECTION, Data.class);
        FindIterable<Data> findIterable = mongoCollection.find(Filters.near("location", refPoint, maxDistance, 0.0));
        for(Data data : findIterable){
            dataList.add(data);
        }
        return dataList;
    }

    //Finds all locations for the specific product within radius provided
    public List<Data> getSpecificProductWithinRadius(ArrayList<Double> location, double maxDistance, String product){
        if(mongoClient == null){
            connect();
        }
        List<Data> dataList = new ArrayList<>();
        Point refPoint = new Point(new Position(location.get(0), location.get(1)));
        MongoDatabase database = mongoClient.getDatabase(db);
        //MongoCollection<Data> mongoCollection = database.getCollection(DATA_COLLECTION, Data.class);
        MongoCollection<Data> mongoCollection = database.getCollection(DATA_COLLECTION, Data.class);
        //FindIterable<Data> findIterable = mongoCollection.find(Filters.near("location", refPoint, maxDistance, 0.0));
        FindIterable<Data> findIterable = mongoCollection.find(Filters.and(Filters.near("location", refPoint, maxDistance, 0.0), Filters.eq("product", product)) );
        for(Data data : findIterable){
            dataList.add(data);
        }
        return dataList;
    }

    public List<Data> getData(){
        if(mongoClient == null){
            connect();
        }
        List<Data> result = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Data> collection = database.getCollection(DATA_COLLECTION, Data.class);
        FindIterable<Data> findIterable = collection.find();
        for(Data data : findIterable){
            result.add(data);
        }
        return result;
    }

    public void insertProduct(Product product){
        if(mongoClient == null){
            connect();
        }
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Product> collection = database.getCollection(PRODUCT_COLLECTION, Product.class);
        collection.insertOne(product);
    }

    public void removeProduct(Product product){
        if(mongoClient == null){
            connect();
        }
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Product> collection = database.getCollection(PRODUCT_COLLECTION, Product.class);
        collection.deleteOne(eq("name", product.getName()));

        //Remove data from Database
        //MongoCollection<Data> dataCollection = database.getCollection(DATA_COLLECTION, Data.class);
        //dataCollection.deleteMany(eq("product", product.getName()));
    }

    public List<Product> getProducts(){
        if(mongoClient == null){
            connect();
        }
        List<Product> result = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Product> collection = database.getCollection(PRODUCT_COLLECTION, Product.class);
        FindIterable<Product> findIterable = collection.find();
        for(Product product : findIterable){
            result.add(product);
        }
        return result;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
