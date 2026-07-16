package com.toucan.shopping.modules.caffeine.manager;

import com.toucan.shopping.modules.caffeine.service.CaffeineCacheService;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * CaffeineCacheManager 单元测试
 */
public class CaffeineCacheManagerTest {

    @After
    public void tearDown() {
        CaffeineCacheManager.getInstance().clearAll();
    }

    @Test
    public void testGetCache() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        CaffeineCacheService cache1 = manager.getCache("test1");
        assertNotNull(cache1);
        CaffeineCacheService cache2 = manager.getCache("test1");
        assertSame(cache1, cache2);
    }

    @Test
    public void testDifferentCaches() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        CaffeineCacheService cache1 = manager.getCache("cacheA");
        CaffeineCacheService cache2 = manager.getCache("cacheB");
        assertNotSame(cache1, cache2);

        cache1.put("key", "fromA");
        cache2.put("key", "fromB");
        assertEquals("fromA", cache1.get("key"));
        assertEquals("fromB", cache2.get("key"));
    }

    @Test
    public void testGetCacheCustomConfig() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        CaffeineCacheService cache = manager.getCache("custom", 10, TimeUnit.SECONDS, 200);
        assertNotNull(cache);
        cache.put("k", "v");
        assertEquals("v", cache.get("k"));
    }

    @Test
    public void testGetCacheIfPresent() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        assertNull(manager.getCacheIfPresent("notExist"));
        manager.getCache("exist");
        assertNotNull(manager.getCacheIfPresent("exist"));
    }

    @Test
    public void testRemoveCache() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        manager.getCache("toRemove");
        assertNotNull(manager.getCacheIfPresent("toRemove"));
        manager.removeCache("toRemove");
        assertNull(manager.getCacheIfPresent("toRemove"));
    }

    @Test
    public void testClearAll() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        manager.getCache("c1");
        manager.getCache("c2");
        assertFalse(manager.cacheNames().isEmpty());
        manager.clearAll();
        assertTrue(manager.cacheNames().isEmpty());
    }

    @Test
    public void testDefaultConfig() {
        CaffeineCacheManager manager = CaffeineCacheManager.getInstance();
        manager.setDefaultConfig(5, TimeUnit.SECONDS, 10);
        CaffeineCacheService cache = manager.getCache("defaulted");
        cache.put("k", "v");
        assertEquals("v", cache.get("k"));
    }
}
