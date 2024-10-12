package org.ozaii.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    /**
     * Initializes the HikariCP data source with configuration from ConfigManager.
     */
    public static void initialize() {
        HikariConfig hikariConfig = new HikariConfig();

        // Set database connection properties from ConfigManager
        hikariConfig.setJdbcUrl(ConfigManager.getDatabaseUrl());
        hikariConfig.setUsername(ConfigManager.getDatabaseUser());
        hikariConfig.setPassword(ConfigManager.getDatabasePassword());

        // Set connection pool settings
        hikariConfig.setMaximumPoolSize(ConfigManager.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(ConfigManager.getMinimumIdle());
        hikariConfig.setConnectionTimeout(ConfigManager.getConnectionTimeout());
        hikariConfig.setIdleTimeout(ConfigManager.getIdleTimeout());
        hikariConfig.setMaxLifetime(ConfigManager.getMaxLifetime());

        // Create the HikariCP data source
        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Gets a connection from the data source.
     *
     * @return a Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized. Call initialize() first.");
        }
        return dataSource.getConnection();
    }

    /**
     * Closes the data source and releases all resources.
     */
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null; // Prevent further usage after closing
        }
    }

    /**
     * Checks if the data source is initialized.
     *
     * @return true if the data source is initialized, false otherwise
     */
    public static boolean isInitialized() {
        return dataSource != null;
    }
}
