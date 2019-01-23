## Deploy

```java
mvn package && java -jar target/cache-1.0.jar server config.yml
```

## API Documentation

localhost:8080/swagger

## Config

You have the choice to use either an in-memory cache, which is only recommended for local usage due to horizontal scaling limitations and volatility, or Redis. 

To use the in-memory cache set the following in `config.yml`: 
```yaml
useInMemoryCache: true
```

To use Redis, the following properties need to be defined in `config.yml`:
```yaml
useInMemoryCache: false

redis:
  endpoint: localhost:6379
  password: null
  minIdle: 0
  maxIdle: 0
  maxTotal: 1
  ssl: false
  timeout: 2000
```

## Test
```json
mvn verify
```
