package server;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private final Socket clientSocket;
    private final Function<String, String> requestProcessor; // Функция для обработки запроса

    public ClientHandler(Socket socket, Function<String, String> processor) {
        this.clientSocket = socket;
        this.requestProcessor = processor;
    }

    @Override
    public void run() {
        // Используем try-with-resources для автоматического закрытия ресурсов
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) { // true для auto-flush

            String inputLine;
            // Читаем строки до тех пор, пока клиент не закроет соединение (или произойдет ошибка)
            while ((inputLine = reader.readLine()) != null && !clientSocket.isClosed()) {
                System.out.println("Получено от клиента " + clientSocket.getRemoteSocketAddress() + ": " + inputLine);

                // Обрабатываем строку с помощью внешней функции
                String processedOutput = requestProcessor.apply(inputLine);

                // Отправляем результат обратно клиенту
                writer.println(processedOutput);
                System.out.println("Отправлено клиенту " + clientSocket.getRemoteSocketAddress() + ": " + processedOutput);

                // Если строка "exit" (или другая команда), можно закрыть соединение
                // if ("exit".equalsIgnoreCase(inputLine.trim())) {
                //     break; // Выходим из цикла, чтобы закрыть соединение
                // }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при обработке соединения с клиентом " + clientSocket.getRemoteSocketAddress(), e);
            System.err.println("Ошибка при обработке соединения: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Закрываем сокет клиента в любом случае
                System.out.println("Соединение с клиентом " + clientSocket.getRemoteSocketAddress() + " закрыто.");
            } catch (IOException e) {
                logger.log(Level.WARNING, "Не удалось закрыть сокет клиента", e);
            }
        }
    }
}