package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final int PORT = 9999; // Можно вынести в конфигурацию
    private final ExecutorService threadPool;

    public Server() {
        // Создаем пул потоков, например, фиксированный размер 10
        this.threadPool = Executors.newFixedThreadPool(10); // Размер пула можно настроить
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (!serverSocket.isClosed()) { // Цикл ожидания соединений
                Socket clientSocket = serverSocket.accept(); // Блокируется, ждет клиентское соединение
                System.out.println("Новое соединение: " + clientSocket.getRemoteSocketAddress());

                // Передаем сокет клиентскому обработчику и запускаем в отдельном потоке из пула
                ClientHandler handler = new ClientHandler(clientSocket, this::processRequest);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при работе сервера", e);
            System.err.println("Ошибка сервера: " + e.getMessage());
            // Возможно, стоит предусмотреть остановку пула потоков здесь
        } finally {
            stop(); // Останавливаем пул потоков при завершении работы сервера
        }
    }

    // Метод, который будет вызываться из ClientHandler для обработки данных
    private String processRequest(String inputData) {
        return StringProcessor.process(inputData);
    }

    public void stop() {
        if (!threadPool.isShutdown()) {
            threadPool.shutdown(); // Позволяет завершить выполняющиеся задачи
            System.out.println("Пул потоков сервера остановлен.");
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start(); // Запускаем сервер
    }
}