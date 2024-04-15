package ru.alex.project;

import ru.alex.project.Client;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        new Client(new InetSocketAddress("127.0.0.1", 2222)).run();
    }
}
