-- ============================================
-- 接口监控数据库（按年分库）
-- ============================================
CREATE DATABASE IF NOT EXISTS toucan_shopping_api_monitor_2026
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

CREATE DATABASE IF NOT EXISTS toucan_shopping_api_monitor_2027
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

-- ============================================
-- 接口监控原始记录表（按年分库 + 按月分表，保留 7 天）
-- 逻辑表名: api_monitor_record
-- 物理表名: toucan_shopping_api_monitor_YYYY.api_monitor_record_YYYY_MM
-- ============================================

-- 2026年01月（模板表）
USE toucan_shopping_api_monitor_2026;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_01 (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT  COMMENT '主键ID',
    api_url         VARCHAR(500) NOT NULL              COMMENT '接口URL',
    http_method     VARCHAR(10)                        COMMENT '请求方法 GET/POST/PUT/DELETE',
    elapsed_ms      INT NOT NULL                       COMMENT '响应耗时(毫秒)',
    status_code     INT                                COMMENT 'HTTP响应状态码 200/404/500',
    trace_id        VARCHAR(8)                         COMMENT '链路追踪ID，关联日志系统',
    app_name        VARCHAR(100)                       COMMENT '应用名称',
    server_ip       VARCHAR(50)                        COMMENT '请求处理服务器IP',
    request_time    DATETIME(3) NOT NULL               COMMENT '请求时间(毫秒精度)',
    sharding_date   DATE NOT NULL                      COMMENT '分片日期',
    create_date     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    INDEX idx_url_time (api_url, request_time),
    INDEX idx_elapsed (elapsed_ms)
) COMMENT='接口监控原始记录-2026年01月';

-- 2026年 02~12月
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_02 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_03 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_04 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_05 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_06 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_07 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_08 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_09 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_10 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_11 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2026_12 LIKE api_monitor_record_2026_01;

-- 2027年 01~12月
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_01 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_02 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_03 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_04 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_05 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_06 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_07 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_08 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_09 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_10 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_11 LIKE api_monitor_record_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_record_2027_12 LIKE api_monitor_record_2026_01;

-- ============================================
-- 接口监控分钟聚合表（按年分库 + 按月分表，保留 30 天）
-- 逻辑表名: api_monitor_metrics
-- 物理表名: toucan_shopping_api_monitor_YYYY.api_monitor_metrics_YYYY_MM
-- ============================================
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_01 (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT  COMMENT '主键ID',
    api_url         VARCHAR(500) NOT NULL              COMMENT '接口URL',
    app_name        VARCHAR(100)                       COMMENT '应用名称',
    time_window     DATETIME NOT NULL                  COMMENT '时间窗口(精确到分钟)',
    request_count   INT DEFAULT 0                      COMMENT '该分钟内请求总数',
    avg_ms          DECIMAL(10,2) DEFAULT 0            COMMENT '平均响应耗时(毫秒)',
    max_ms          INT DEFAULT 0                      COMMENT '最大响应耗时(毫秒)',
    min_ms          INT DEFAULT 0                      COMMENT '最小响应耗时(毫秒)',
    sharding_date   DATE NOT NULL                      COMMENT '分片日期',
    create_date     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    UNIQUE KEY uk_url_app_window (api_url, app_name, time_window, sharding_date),
    INDEX idx_time_window (time_window)
) COMMENT='接口监控分钟聚合-2026年01月';

-- 2026年 02~12月
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_02 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_03 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_04 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_05 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_06 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_07 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_08 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_09 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_10 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_11 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2026_12 LIKE api_monitor_metrics_2026_01;

-- 2027年 01~12月
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_01 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_02 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_03 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_04 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_05 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_06 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_07 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_08 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_09 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_10 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_11 LIKE api_monitor_metrics_2026_01;
CREATE TABLE IF NOT EXISTS api_monitor_metrics_2027_12 LIKE api_monitor_metrics_2026_01;
