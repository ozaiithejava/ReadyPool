# ReadyPool

ReadyPool, Java ile yazılmış bir kütüphanedir. MySQL veritabanı ile HikariCP kullanarak hızlı ve verimli bağlantılar yapmanızı sağlar. Ayrıca, yapılandırma dosyası ile özelleştirilmiş ayarlar kullanmanıza olanak tanır.

## Özellikler

- **Hızlı Bağlantı**: HikariCP'nin sunduğu bağlantı havuzu ile yüksek performans.
- **Yapılandırma Yönetimi**: JSON tabanlı yapılandırma dosyası ile kolay özelleştirme.
- **Veritabanı Desteği**: MySQL veritabanı ile sorunsuz entegrasyon.
- **Kullanım Kolaylığı**: Basit ve anlaşılır API ile hızlı entegrasyon.

## Kurulum

1. `config.json` dosyasını aşağıdaki gibi yapılandırın:
    ```json
    {
      "database": {
        "url": "jdbc:mysql://localhost:3306/",
        "name": "veritabani_adiniz",
        "user": "kullanici_adiniz",
        "password": "sifreniz"
      },
      "pool": {
        "maximumPoolSize": 10,
        "minimumIdle": 2,
        "connectionTimeout": 30000,
        "idleTimeout": 60000,
        "maxLifetime": 1800000
      }
    }
    ```

## Kullanım

### 1. Örnek Class

`ReadyPool` kütüphanesini kullanarak basit bir veritabanı bağlantısı oluşturabilirsiniz. Aşağıdaki örnekte, veritabanına bağlanarak mevcut tabloları listeleyen bir sınıf bulunmaktadır:

```java
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
                System.out.println("Bağlantı başarılı!");

                String query = "SHOW TABLES"; // Tabloları listele
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        System.out.println("Tablo: " + resultSet.getString(1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Hata ayıklama
            }

        } catch (Exception e) {
            e.printStackTrace(); // Hata ayıklama
        }
    }
}
