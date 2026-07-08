package com.toucan.shopping.modules.common.util;


import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtil {

    /**
     * 获取客户端ip地址(可以穿透代理)
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 输入cidr格式 查看是否能匹配IP
     * @param ip
     * @param cidr 10.0.0.0/8
     * @return
     * @throws UnknownHostException
     */
    public static boolean matches(String ip, String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        byte[] ipBytes = InetAddress.getByName(ip).getAddress();
        byte[] netBytes = InetAddress.getByName(parts[0]).getAddress();
        int mask = Integer.parseInt(parts[1]);

        int bits = ipBytes.length * 8;  // IPv4=32, IPv6=128
        for (int i = 0; i < bits; i++) {
            int byteIdx = i / 8;
            int bitIdx = 7 - (i % 8);      // 从高位开始
            int ipBit = (ipBytes[byteIdx] >> bitIdx) & 1;
            int netBit = (netBytes[byteIdx] >> bitIdx) & 1;
            if (i < mask && ipBit != netBit) return false;
            if (i >= mask) break;           // 掩码之后的部分不比较
        }
        return true;
    }
}
