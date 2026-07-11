package com.toucan.shopping.modules.caffeine.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * CaffeineUtils 单元测试
 */
public class CaffeineUtilsTest {

    @Test
    public void testNewCache() {
        Cache<String, Integer> cache = CaffeineUtils.newCache(10, TimeUnit.SECONDS, 50);
        cache.put("a", 1);
        assertEquals(Integer.valueOf(1), cache.getIfPresent("a"));
    }

    @Test
    public void testNewLoadingCache() {
        LoadingCache<String, String> cache = CaffeineUtils.newLoadingCache(10, TimeUnit.SECONDS, 50, key -> "val-" + key);
        assertEquals("val-foo", cache.get("foo"));
        assertEquals("val-bar", cache.get("bar"));
    }

    @Test
    public void testNewCacheAfterAccess() {
        Cache<String, String> cache = CaffeineUtils.newCacheAfterAccess(10, TimeUnit.SECONDS, 50);
        cache.put("k", "v");
        assertEquals("v", cache.getIfPresent("k"));
    }

    @Test
    public void testShortcutMethods() {
        assertNotNull(CaffeineUtils.newShortCache(100));
        assertNotNull(CaffeineUtils.newStandardCache(100));
        assertNotNull(CaffeineUtils.newLongCache(100));
    }
}
