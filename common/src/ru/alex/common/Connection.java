package ru.alex.common;

import java.io.*;
import java.net.Socket;

public class Connection implements AutoCloseable{
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Connection(Socket socket) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }
// Метод отправки сообщения по сокет соединению
    public void send(Message message) throws IOException {
        outputStream.writeObject(message);
        outputStream.flush();
    }
//    Метод получения сообщения поь сокет соединению
    public Message read()throws IOException{
        try{
            return (Message) inputStream.readObject();
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        
        outputStream.close();
        inputStream.close();
    }
}
