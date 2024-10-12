plugins {
    id("java")
}

group = "org.ozaii"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("mysql:mysql-connector-java:5.1.6")
    implementation("com.google.code.gson:gson:2.7")
    testImplementation("ch.qos.logback:logback-classic:1.4.8")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}