package ru.alex.project;

import ru.alex.common.Connection;
import ru.alex.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private int port;
    private int countHelp;
    private int countPing;
    private int countRequests;
    private int countPopular;

    public Server(int port) {
        this.port = port;
    }

    public void responseServer(Message messageClient, Connection connection) throws IOException {
        if(messageClient.getText().equals("help")){
            Message message = new Message("/help - список доступных запросов и их описание\n" +
                    "/ping - время ответа сервера\n" +
                    "/requests - количество успешно обработанных запросов\n" +
                    "/popular - название самого популярного запроса", "server");
            connection.send(message);
            countHelp++;
        }
        else if(messageClient.getText().startsWith("ping")){

            long timeStart = Long.parseLong(messageClient.getText().substring(4));
            Message message = new Message("Время ответа сервера " + (System.nanoTime() - timeStart) +
                    " н/с", "server");
            connection.send(message);
            countPing++;
        }
        else if(messageClient.getText().equals("requests")){
            int sum = countHelp + countPing + countPopular + countRequests;
            Message message = new Message("Количество успешно обработанных запросов " + sum, "server");
            connection.send(message);
            countRequests++;
        }

        else if(messageClient.getText().equals("popular")){
            Map<String, Integer> map = new HashMap<>();
            map.put("help", countHelp);
            map.put("ping", countPing);
            map.put("requests", countRequests);
            map.put("popular", countPopular);
            var max = map.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow();
            Message message = new Message("Самый популярный запрос " + max.getKey(), "server");
            connection.send(message);
            countPopular++;
        }
        else {
            Message message = new Message("Вы ввели не верную команду", "server");
            connection.send(message);
        }
    }

    public void startServer(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                try{
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    Message messageClient = connection.read();
                    responseServer(messageClient, connection);

                } catch (IOException e){
                    System.out.println("Ошибка запуска сервера");
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
