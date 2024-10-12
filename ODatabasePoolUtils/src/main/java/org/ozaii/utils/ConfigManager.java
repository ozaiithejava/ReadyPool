package org.ozaii.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final String CONFIG_FILE_PATH = "config.json"; // Yapılandırma dosyasının yolu
    private static JsonObject config; // JSON yapılandırma nesnesi
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // GSON nesnesi

    // Yapılandırmayı yükle
    public static void loadConfig() {
        File configFile = new File(CONFIG_FILE_PATH);

        // Eğer yapılandırma dosyası yoksa, varsayılan yapılandırmayı oluştur
        if (!configFile.exists()) {
            try {
                createDefaultConfig(configFile);
                System.out.println("Default configuration created.");
            } catch (IOException e) {
                System.err.println("Error creating default configuration: " + e.getMessage());
                return;
            }
        }

        // Yapılandırma dosyasını oku
        try (FileReader reader = new FileReader(configFile)) {
            config = gson.fromJson(reader, JsonObject.class);
            validateConfig(); // Yapılandırmayı doğrula
            System.out.println("Configuration loaded successfully.");
        } catch (IOException | JsonParseException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            // Opsiyonel olarak varsayılan yapılandırmayı yeniden oluşturabilirsiniz
        }
    }

    // Varsayılan yapılandırmayı oluştur
    private static void createDefaultConfig(File configFile) throws IOException {
        config = new JsonObject();

        // Veritabanı yapılandırması
        JsonObject databaseConfig = new JsonObject();
        databaseConfig.addProperty("url", "jdbc:mysql://localhost:3306/"); // Veritabanı URL'si
        databaseConfig.addProperty("name", "test"); // Veritabanı adı
        databaseConfig.addProperty("user", "root"); // Veritabanı kullanıcı adı
        databaseConfig.addProperty("password", ""); // Veritabanı şifresi

        // Pool yapılandırması
        JsonObject poolConfig = new JsonObject();
        poolConfig.addProperty("maximumPoolSize", 10); // Maksimum bağlantı havuzu boyutu
        poolConfig.addProperty("minimumIdle", 2); // Minimum boş bağlantı sayısı
        poolConfig.addProperty("connectionTimeout", 30000); // Bağlantı zaman aşımı (ms)
        poolConfig.addProperty("idleTimeout", 60000); // Boş bağlantı zaman aşımı (ms)
        poolConfig.addProperty("maxLifetime", 1800000); // Maksimum bağlantı ömrü (ms)

        // Konfigürasyon nesnelerini ekle
        config.add("database", databaseConfig);
        config.add("pool", poolConfig);

        // Varsayılan yapılandırmayı dosyaya yaz
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
        }
    }

    // Yapılandırmayı doğrula
    private static void validateConfig() {
        if (config == null) {
            throw new JsonParseException("Configuration is null");
        }

        // Veritabanı yapılandırmasını doğrula
        if (!config.has("database")) {
            throw new JsonParseException("Missing 'database' configuration");
        }
        JsonObject databaseConfig = config.getAsJsonObject("database");
        if (!databaseConfig.has("url") || !databaseConfig.has("name") ||
                !databaseConfig.has("user") || !databaseConfig.has("password")) {
            throw new JsonParseException("Missing database configuration properties");
        }

        // Pool yapılandırmasını doğrula
        if (!config.has("pool")) {
            throw new JsonParseException("Missing 'pool' configuration");
        }
        JsonObject poolConfig = config.getAsJsonObject("pool");
        if (!poolConfig.has("maximumPoolSize") || !poolConfig.has("minimumIdle") ||
                !poolConfig.has("connectionTimeout") || !poolConfig.has("idleTimeout") ||
                !poolConfig.has("maxLifetime")) {
            throw new JsonParseException("Missing pool configuration properties");
        }
    }

    public static String getDatabaseUrl() {
       String curl = getStringValue("database","url"); // Veritabanı adını döndür
        String name = getStringValue("database","name"); // Veritabanı adını döndür
        return curl+name;
    }

    public static String getDatabaseName() {
        return getStringValue("database", "name"); // Veritabanı adını döndür
    }

    public static String getDatabaseUser() {
        return getStringValue("database", "user");
    }

    public static String getDatabasePassword() {
        return getStringValue("database", "password");
    }

    public static int getMaximumPoolSize() {
        return getIntValue("pool", "maximumPoolSize");
    }

    public static int getMinimumIdle() {
        return getIntValue("pool", "minimumIdle");
    }

    public static long getConnectionTimeout() {
        return getLongValue("pool", "connectionTimeout");
    }

    public static long getIdleTimeout() {
        return getLongValue("pool", "idleTimeout");
    }

    public static long getMaxLifetime() {
        return getLongValue("pool", "maxLifetime");
    }

    // JSON nesnesinden değerleri döndür
    private static String getStringValue(String objectKey, String propertyKey) {
        return config.getAsJsonObject(objectKey).get(propertyKey).getAsString();
    }

    private static int getIntValue(String objectKey, String propertyKey) {
        return config.getAsJsonObject(objectKey).get(propertyKey).getAsInt();
    }

    private static long getLongValue(String objectKey, String propertyKey) {
        return config.getAsJsonObject(objectKey).get(propertyKey).getAsLong();
    }
}
