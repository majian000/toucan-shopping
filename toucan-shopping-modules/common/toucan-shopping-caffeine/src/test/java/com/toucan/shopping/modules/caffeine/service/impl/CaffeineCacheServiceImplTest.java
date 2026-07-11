package com.toucan.shopping.modules.caffeine.service.impl;

import com.toucan.shopping.modules.caffeine.service.CaffeineCacheService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * CaffeineCacheServiceImpl 单元测试
 */
public class CaffeineCacheServiceImplTest {

    private CaffeineCacheService cacheService;

    @Before
    public void setUp() {
        cacheService = new CaffeineCacheServiceImpl(1, TimeUnit.MINUTES, 100);
    }

    @Test
    public void testPutAndGet() {
        cacheService.put("key1", "value1");
        assertEquals("value1", cacheService.get("key1"));
    }

    @Test
    public void testGetNotExist() {
        assertNull(cacheService.get("noSuchKey"));
    }

    @Test
    public void testGetWithLoader() {
        String value = cacheService.get("lazyKey", k -> "loaded-" + k);
        assertEquals("loaded-lazyKey", value);
        assertEquals("loaded-lazyKey", cacheService.get("lazyKey"));
    }

    @Test
    public void testInvalidate() {
        cacheService.put("key", "value");
        assertEquals("value", cacheService.get("key"));
        cacheService.invalidate("key");
        assertNull(cacheService.get("key"));
    }

    @Test
    public void testInvalidateAllBatch() {
        cacheService.put("a", 1);
        cacheService.put("b", 2);
        cacheService.put("c", 3);
        assertEquals(3L, cacheService.size());
        cacheService.invalidateAll(Arrays.asList("a", "b"));
        assertNull(cacheService.get("a"));
        assertNull(cacheService.get("b"));
        assertEquals(Integer.valueOf(3), cacheService.get("c"));
    }

    @Test
    public void testInvalidateAllClear() {
        cacheService.put("a", 1);
        cacheService.put("b", 2);
        cacheService.invalidateAll();
        assertEquals(0L, cacheService.size());
    }

    @Test
    public void testSize() {
        assertEquals(0L, cacheService.size());
        cacheService.put("a", 1);
        cacheService.put("b", 2);
        assertEquals(2L, cacheService.size());
    }

    @Test
    public void testAsMap() {
        cacheService.put("a", "1");
        cacheService.put("b", "2");
        Map<String, String> map = cacheService.asMap();
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
        assertEquals(2, (long) map.size());
    }

    @Test
    public void testGetIfPresent() {
        cacheService.put("k", "v");
        assertEquals("v", cacheService.getIfPresent("k"));
    }

    @Test
    public void testCleanUp() {
        CaffeineCacheServiceImpl impl = (CaffeineCacheServiceImpl) cacheService;
        cacheService.put("k", "v");
        cacheService.invalidate("k");
        impl.cleanUp();
        assertEquals(0L, cacheService.size());
    }
}
