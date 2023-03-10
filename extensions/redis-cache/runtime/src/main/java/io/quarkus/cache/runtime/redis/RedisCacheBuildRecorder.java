package io.quarkus.cache.runtime.redis;

import java.util.*;
import java.util.function.Supplier;

import org.jboss.logging.Logger;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.runtime.CacheManagerImpl;
import io.quarkus.cache.runtime.caffeine.metrics.MetricsInitializer;
import io.quarkus.cache.runtime.redis.impl.RedisCacheImpl;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RedisCacheBuildRecorder {

    private static final Logger LOGGER = Logger.getLogger(RedisCacheBuildRecorder.class);

    public Supplier<CacheManager> getCacheManagerSupplier(Set<RedisCacheInfo> cacheInfos,
            MetricsInitializer metricsInitializer) {
        Objects.requireNonNull(cacheInfos);
        return new Supplier<CacheManager>() {
            @Override
            public CacheManager get() {
                if (cacheInfos.isEmpty()) {
                    return new CacheManagerImpl(Collections.emptyMap());
                } else {
                    // The number of caches is known at build time so we can use fixed initialCapacity and loadFactor for the caches map.
                    Map<String, Cache> caches = new HashMap<>(cacheInfos.size() + 1, 1.0F);
                    for (RedisCacheInfo cacheInfo : cacheInfos) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debugf(
                                    "Building Redis cache [%s] with [ttl=%s], [prefix=%s], [classOfItems=%s]",
                                    cacheInfo.name, cacheInfo.ttl, cacheInfo.prefix,
                                    cacheInfo.valueType);
                        }

                        RedisCacheImpl cache = new RedisCacheImpl(cacheInfo);
                        caches.put(cacheInfo.name, cache);
                    }
                    return new CacheManagerImpl(caches);
                }
            }
        };
    }
}
