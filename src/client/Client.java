// File: src/main/java/org/example/exo/client/Client.java

package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9999;      // Порт сервера

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Подключено к серверу " + SERVER_HOST + ":" + SERVER_PORT);
            System.out.println("Введите строку (или 'quit' для выхода):");

            while (true) {
                userInput = stdIn.readLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    System.out.println("Завершение работы клиента...");
                    break;
                }

                // Отправляем строку серверу
                out.println(userInput);

                // Получаем и выводим ответ сервера
                String response = in.readLine();
                if (response != null) {
                    System.out.println("Ответ от сервера: " + response);
                } else {
                    // Сервер закрыл соединение
                    System.out.println("Сервер закрыл соединение.");
                    break;
                }

                System.out.println("Введите следующую строку (или 'quit' для выхода):");
            }
        } catch (IOException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
            e.printStackTrace();
        }
    }
}