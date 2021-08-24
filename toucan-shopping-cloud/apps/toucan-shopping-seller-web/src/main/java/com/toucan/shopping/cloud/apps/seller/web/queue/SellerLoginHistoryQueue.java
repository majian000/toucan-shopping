package com.toucan.shopping.cloud.apps.seller.web.queue;

import com.toucan.shopping.modules.seller.vo.SellerLoginHistoryVO;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 登录历史队列
 */
@Component
public class SellerLoginHistoryQueue {

    private ConcurrentLinkedQueue<SellerLoginHistoryVO> sellerLoginHistoryVOs = new ConcurrentLinkedQueue<SellerLoginHistoryVO>();

    public void push(SellerLoginHistoryVO SellerLoginHistoryVO)
    {
        sellerLoginHistoryVOs.add(SellerLoginHistoryVO);
    }

    public SellerLoginHistoryVO pop()
    {
        return sellerLoginHistoryVOs.poll();
    }
}
