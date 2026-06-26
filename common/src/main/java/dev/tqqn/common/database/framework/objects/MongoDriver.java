package dev.tqqn.common.database.framework.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.database.framework.adapters.LocationAdapter;
import dev.tqqn.common.database.framework.adapters.UUIDAdapter;
import dev.tqqn.common.database.framework.mongo.MongoItem;
import dev.tqqn.common.database.framework.mongo.MongoObject;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bukkit.Location;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static java.util.Objects.requireNonNull;

public class MongoDriver implements IDataBaseDriver {

    private MongoDatabase mongoDatabase;

    private final Gson gson;
    private final JsonWriterSettings WRITER_SETTINGS = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

    private final Executor executors = Executors.newCachedThreadPool();

    private final AbstractDatabaseModule databaseModule;

    public MongoDriver(AbstractDatabaseModule databaseModule) {
        this.databaseModule = databaseModule;
        this.gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDAdapter()).registerTypeAdapter(Location.class, new LocationAdapter()).enableComplexMapKeySerialization().create();
    }

    @Override
    public boolean connect(String database, String host, int port) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        this.mongoDatabase = mongoClient.getDatabase(database);
        return validateDatabase();
    }

    @Override
    public boolean connect(String database, String host, String userName, String password) {
        if (validateDatabase()) return false;
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://" + userName + ":" + password + "@" + host + "/?retryWrites=true&w=majority&appName=" + database));
        this.mongoDatabase = mongoClient.getDatabase(database);
        return validateDatabase();
    }

    private boolean validateDatabase() {
        try {
            mongoDatabase.getName();
        } catch (MongoException e) {
            return false;
        }
        return true;
    }

    public <O extends MongoObject<?>> void upsertFields(O object) {
        Document document = Document.parse(gson.toJson(object));
        Object id = document.remove("_id"); // _id is immutable, must not appear in $set
        getCollection(object.getClass()).updateOne(
                eq("_id", id),
                new Document("$set", document),
                new UpdateOptions().upsert(true)
        );
    }

    public <O extends MongoObject<?>> void upsertFieldsAsync(O object) {
        CompletableFuture.runAsync(() -> upsertFields(object), executors)
                .exceptionally(exception -> {
                    databaseModule.getLogger().log(Level.SEVERE, "Could not save " + object.getClass(), exception);
                    return null;
                });
    }

    public <O extends MongoObject<?>> void save(O object) {
        Document document = Document.parse(gson.toJson(object));
        if (getCollection(object.getClass()).find(eq("_id", document.get("_id"))).first() == null) {
            getCollection(object.getClass()).insertOne(document);
            return;
        }
        getCollection(object.getClass()).replaceOne(eq("_id", document.get("_id")), document, new ReplaceOptions().upsert(true));
    }

    public <O extends MongoObject<?>> List<O> readAll(Class<O> clazz) {
        MongoCollection<Document> mongoCollection = this.getCollection(clazz);
        return (List) StreamSupport.stream(mongoCollection.find().spliterator(), false).map((document) ->  (MongoObject) gson.fromJson(document.toJson(WRITER_SETTINGS), clazz)).collect(Collectors.toList());
    }

    public <O extends MongoObject<?>> CompletableFuture<List<O>> readAllAsync(Class<O> clazz) {
        return CompletableFuture.supplyAsync(() -> this.readAll(clazz), this.executors);
    }

    public <O extends MongoObject<?>, K> O read(Class<O> clazz, K key) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);
        Document document = mongoCollection.find(eq("_id", key)).first();

        if (document == null) return null;

        return gson.fromJson(document.toJson(WRITER_SETTINGS), clazz);
    }

    public <O extends MongoObject<?>> void saveAsync(O object) {
        CompletableFuture.runAsync(() -> {
            save(object);
        }, executors).exceptionally((exception) -> {
            databaseModule.getLogger().log(Level.SEVERE, "Could not save " + object.getClass(), exception);
            return null;
        });
    }

    public <O extends MongoObject<?>, K> CompletableFuture<O> readAsync(Class<O> clazz, K key) {
        return CompletableFuture.supplyAsync(() -> read(clazz, key));
    }

    public <O extends MongoObject<?>> void updateAsync(O object, String field, Object value) {
        CompletableFuture.runAsync(() -> getCollection(object.getClass()).updateOne(eq("_id", object.getKey().toString()), set(field, value)));
    }

    private MongoCollection<Document> getCollection(Class<?> clazz) {
        MongoItem item = clazz.getAnnotation(MongoItem.class);
        requireNonNull(item, String.format("Class '%s' does not have the MongoEntity annotation.", clazz.getSimpleName()));

        return mongoDatabase.getCollection(item.value());
    }

    public <O extends MongoObject<?>> void delete(O object) {
        Document document = (new Document()).append("_id", object.getKey().toString());
        this.getCollection(object.getClass()).deleteOne(document);
    }

    public <O extends MongoObject<?>> CompletableFuture<Void> deleteAsync(O object) {
        return CompletableFuture.runAsync(() -> this.delete(object), this.executors);
    }
}
