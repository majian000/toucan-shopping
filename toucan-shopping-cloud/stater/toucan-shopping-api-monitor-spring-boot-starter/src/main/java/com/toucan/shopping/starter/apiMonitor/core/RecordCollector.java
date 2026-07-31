package com.toucan.shopping.starter.apiMonitor.core;

import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控记录采集器
 * 有界队列缓冲，满则丢弃 + 计数告警
 */
public class RecordCollector {

    private static final int QUEUE_CAPACITY = 10000;

    private final LinkedBlockingQueue<ApiMonitorRecordVO> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private final AtomicLong droppedCount = new AtomicLong(0);

    /** 入队，队列满则丢弃 */
    public void collect(ApiMonitorRecordVO record) {
        if (!queue.offer(record)) {
            droppedCount.incrementAndGet();
        }
    }

    /** 批量取出 */
    public void drainTo(List<ApiMonitorRecordVO> batch, int maxSize) {
        queue.drainTo(batch, maxSize);
    }

    /** 获取并重置丢弃计数 */
    public long getAndResetDroppedCount() {
        return droppedCount.getAndSet(0);
    }

    /** 清空队列 */
    public void clear() {
        queue.clear();
    }
}
