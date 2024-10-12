package org.ozaii;

import org.ozaii.utils.ConfigManager;
import org.ozaii.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {

    public static void main(String[] args) {
        try {
            // ConfigManager ile konfigürasyonu yükle
            ConfigManager.loadConfig();

            // DatabaseManager ile bağlantı havuzunu başlat
            DatabaseManager.initialize();

            // Bağlantıyı al
            try (Connection connection = DatabaseManager.getConnection()) {
                System.out.println("baglanti basarili");


                String query = "SHOW TABLES";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        System.out.println("Tablo: " + resultSet.getString(1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
