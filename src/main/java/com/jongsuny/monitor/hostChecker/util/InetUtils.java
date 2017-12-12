package com.jongsuny.monitor.hostChecker.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created by jongsuny on 17/11/29.
 */
public class InetUtils {
    private static final String IPV4_BASIC_PATTERN_STRING =
            "(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}" + // initial first field, 1-255
                    "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}" + // following 2 fields, 0-255 followed by .
                    "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"; // final field, 0-255
    private static final String IPV4_PUBLIC_REG = "\\b(?!(10|172\\.(1[6-9]|2[0-9]|3[0-2])|192\\.168))(?:(?:2"
            + "(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2"
            + "([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b";
    private static final Pattern IPV4_PATTERN = Pattern.compile("^" + IPV4_BASIC_PATTERN_STRING + "$");

    public static int ipToInt(String ip) {
        byte[] addr = ipToBytes(ip);
        int address = addr[3] & 0xFF;
        address |= ((addr[2] << 8) & 0xFF00);
        address |= ((addr[1] << 16) & 0xFF0000);
        address |= ((addr[0] << 24) & 0xFF000000);
        return address;
    }

    public static String intToIp(int ip) {
        byte[] addr = new byte[4];
        addr[0] = (byte) ((ip >>> 24) & 0xFF);
        addr[1] = (byte) ((ip >>> 16) & 0xFF);
        addr[2] = (byte) ((ip >>> 8) & 0xFF);
        addr[3] = (byte) (ip & 0xFF);
        return bytesToIp(addr);
    }

    public static String bytesToIp(byte[] src) {
        StringBuilder ip = new StringBuilder();
        ip.append(src[0] & 0xFF).append(".")
                .append(src[1] & 0xFF).append(".")
                .append(src[2] & 0xFF).append(".")
                .append(src[3] & 0xFF);
        return ip.toString();
    }

    public static byte[] ipToBytes(String ip) {
        try {
            return InetAddress.getByName(ip).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ip + " is invalid IP");
        }
    }

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * 10.0.0.0--10.255.255.255
     * 172.16.0.0--172.31.255.255
     * 192.168.0.0--192.168.255.255
     *
     * @param ip
     * @return
     */
    public static boolean isInternalIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        int address = ipToInt(ip);
        return (((address >>> 24) & 0xFF) == 10)
                || ((((address >>> 24) & 0xFF) == 172)
                && ((address >>> 16) & 0xFF) >= 16
                && ((address >>> 16) & 0xFF) <= 31)
                || ((((address >>> 24) & 0xFF) == 192)
                && (((address >>> 16) & 0xFF) == 168));
    }

    public static boolean isPublicIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return true;
        }
        return ip.matches(IPV4_PUBLIC_REG);
    }

    public static InetAddress[] resolve(String host) throws UnknownHostException {
        return InetAddress.getAllByName(host);
    }
}
