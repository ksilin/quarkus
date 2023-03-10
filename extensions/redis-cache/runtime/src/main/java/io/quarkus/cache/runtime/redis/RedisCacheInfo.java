package io.quarkus.cache.runtime.redis;

import java.time.Duration;

public class RedisCacheInfo {

    /**
     * The cache name
     */
    public String name;

    /**
     * The default time to live of the item stored in the cache
     */
    public Duration ttl;

    /**
     * the key prefix allowing to identify the keys belonging to the cache.
     * If not set, use "cache:$cache-name"
     */
    public String prefix;

    /**
     * The default type of the value stored in the cache.
     */
    public String valueType;

    /**
     * The key type, {@code String} by default.
     */
    public String keyType = String.class.getName();

}
