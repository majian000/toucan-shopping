-- ============================================
-- 接口监控原始记录表（联机实时写入，保留 7 天）
-- ============================================
CREATE TABLE IF NOT EXISTS api_monitor_record (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT  COMMENT '主键ID',
    api_url         VARCHAR(500) NOT NULL              COMMENT '接口URL',
    http_method     VARCHAR(10)                        COMMENT '请求方法 GET/POST/PUT/DELETE',
    elapsed_ms      INT NOT NULL                       COMMENT '响应耗时(毫秒)',
    status_code     INT                                COMMENT 'HTTP响应状态码 200/404/500',
    trace_id        VARCHAR(8)                         COMMENT '链路追踪ID，关联日志系统',
    app_name        VARCHAR(100)                       COMMENT '应用名称，取自 spring.application.name',
    server_ip       VARCHAR(50)                        COMMENT '请求处理的服务器IP',
    request_time    DATETIME(3) NOT NULL               COMMENT '请求时间(毫秒精度)',
    create_date     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    INDEX idx_url_time (api_url, request_time)         COMMENT '按接口+时间查询聚合',
    INDEX idx_elapsed (elapsed_ms)                     COMMENT '慢请求查询'
) COMMENT='接口监控原始记录';

-- ============================================
-- 接口监控分钟聚合表（定时聚合写入，保留 30 天）
-- ============================================
CREATE TABLE IF NOT EXISTS api_monitor_metrics (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT  COMMENT '主键ID',
    api_url         VARCHAR(500) NOT NULL              COMMENT '接口URL',
    app_name        VARCHAR(100)                       COMMENT '应用名称',
    time_window     DATETIME NOT NULL                  COMMENT '时间窗口(精确到分钟，如 16:32:00)',
    request_count   INT DEFAULT 0                      COMMENT '该分钟内请求总数',
    avg_ms          DECIMAL(10,2) DEFAULT 0            COMMENT '平均响应耗时(毫秒)',
    max_ms          INT DEFAULT 0                      COMMENT '最大响应耗时(毫秒)',
    min_ms          INT DEFAULT 0                      COMMENT '最小响应耗时(毫秒)',
    create_date     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    UNIQUE KEY uk_url_app_window (api_url, app_name, time_window) COMMENT '同一接口+应用+分钟唯一',
    INDEX idx_time_window (time_window)                 COMMENT '按时间范围查询趋势'
) COMMENT='接口监控分钟聚合';
