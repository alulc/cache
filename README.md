## Deploy

If using in-memory cache:
```java
mvn package && java -jar target/cache-1.0.jar server
```

If using Redis:
```java
mvn package && java -jar target/cache-1.0.jar server config.yml
```
