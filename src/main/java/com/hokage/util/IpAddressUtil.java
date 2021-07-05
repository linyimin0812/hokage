package com.hokage.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * @author yiminlin
 * @date 2021/07/05 11:19 pm
 * @description ip address util
 **/
public class IpAddressUtil {

    public static final String SLASH = "/";
    public static final String UP = "up";

    public static List<String> acquireIpList() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress address = addressEnumeration.nextElement();
                    if (address instanceof Inet4Address) {
                        ipList.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException ignored) {

        }

        return ipList;
    }

    public static boolean checkHealth(String ip, int port, String path) {
        if (StringUtils.startsWithIgnoreCase(path, SLASH)) {
            path = StringUtils.substring(path, 1);
        }

        try {
            Mono<AppStatus> mono = WebClient.create().get().uri(String.format("http://%s:%s/%s", ip, port, path)).retrieve().bodyToMono(AppStatus.class);
            String status = Optional.ofNullable(mono.block()).map(AppStatus::getStatus).orElse(StringUtils.EMPTY);
            return StringUtils.equalsIgnoreCase(status, UP);
        } catch (Exception e) {
            return false;
        }

    }

    @Data
    public static class AppStatus {
        private String status;
    }
}
