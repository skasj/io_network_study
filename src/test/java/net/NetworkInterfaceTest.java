package net;

import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class NetworkInterfaceTest {
    public static void main(String[] args) throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> tools = NetworkInterface.getNetworkInterfaces();
        NetworkInterface networkInterface;
        InetAddress inetAddress;
        System.out.println("本地地址" + InetAddress.getLocalHost());
        System.out.println("回环地址" + InetAddress.getLoopbackAddress());
        while (tools.hasMoreElements()){
            System.out.println("-------------------");//分割线
            networkInterface = tools.nextElement();
            System.out.println(networkInterface.getName());
            System.out.println(networkInterface.getDisplayName());
            System.out.println(networkInterface.isLoopback());
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            System.out.println(Arrays.toString(hardwareAddress));
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()){
                inetAddress = inetAddresses.nextElement();
//                System.out.println(inetAddress.getCanonicalHostName());
                System.out.println("name:  " + inetAddress.getHostName());
                System.out.println("ip:  "+inetAddress.getHostAddress());

            }

            List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
        }
    }
}
