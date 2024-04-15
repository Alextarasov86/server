package ru.alex.project;

import ru.alex.common.Connection;
import ru.alex.common.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Client {
    private InetSocketAddress address;
    private String username;
    private Scanner scanner;

    public Client(InetSocketAddress address) {
        this.address = address;
        scanner = new Scanner(System.in);
    }

    public void run(){
        System.out.println("Введите имя: ");
        username = scanner.nextLine();
        while (true){
            System.out.println("Введите сообщение: ");
            String text = scanner.nextLine();
            try(Connection connection = new Connection(new Socket
                    (address.getHostName(),
                    address.getPort()))) {
                Message message = new Message(text, username);
                try{
                    if(message.getText().equals("ping")){
                        message.setText(message.getText() + System.nanoTime());
                        connection.send(message);
                    }
                    else {
                        connection.send(message);
                    }

                    Message fromServer = connection.read();
                    System.out.println(fromServer.getText());
                } catch (IOException e){}

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
