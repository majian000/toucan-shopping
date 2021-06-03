package com.toucan.shopping.modules.fastdfs.config;

import com.toucan.shopping.modules.fastdfs.util.FastDFSClient;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class FastDFSConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${fastdfs.connect_timeout}")
    private Integer connectTimeout;

    @Value("${fastdfs.network_timeout}")
    private Integer networkTimeout;

    @Value("${fastdfs.charset}")
    private String charset;

    @Value("${fastdfs.http.tracker_http_port}")
    private Integer trackerHttpPort;

    @Value("${fastdfs.http.anti_steal_token}")
    private boolean antiStealToken;


    @Value("${fastdfs.http.secret_key}")
    private String secretKey;

    @Value("${fastdfs.tracker_server}")
    private String trackerServer;

    @Bean
    public FastDFSClient fastDFSClient()
    {
        FastDFSClient fastDFSClient = new FastDFSClient();

        try {
            logger.info(" 初始化 fastdfs客户端.......");
            ClientGlobal.setG_connect_timeout(connectTimeout);
            ClientGlobal.setG_network_timeout(networkTimeout);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_tracker_http_port(trackerHttpPort);
            ClientGlobal.setG_anti_steal_token(antiStealToken);
            ClientGlobal.setG_secret_key(secretKey);

            String[] szTrackerServers = trackerServer.split(",");
            InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];

            for(int i = 0; i < szTrackerServers.length; ++i) {
                String[] parts = szTrackerServers[i].split("\\:", 2);
                logger.info(" 初始化 地址:{} 端口:{} ",parts[0].trim(),parts[1].trim());
                if (parts.length != 2) {
                    throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
                }

                tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }
            ClientGlobal.setG_tracker_group( new TrackerGroup(tracker_servers));

            fastDFSClient.trackerClient = new TrackerClient();
            fastDFSClient.trackerServer = fastDFSClient.trackerClient.getConnection();
            fastDFSClient.storageServer = null;
            fastDFSClient.storageClient = new StorageClient1(fastDFSClient.trackerServer, fastDFSClient.storageServer);

        }catch(Exception e)
        {
            logger.info(" 初始化 fastdfs客户端 异常.......");
            logger.warn(e.getMessage(),e);
        }
        return fastDFSClient;
    }
}
