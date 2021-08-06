package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignSellerShopServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/sellerShop",fallbackFactory = FeignSellerShopServiceFallbackFactory.class)
public interface FeignSellerShopService {




}
