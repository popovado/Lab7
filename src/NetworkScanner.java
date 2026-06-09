import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class NetworkScanner {

    private static final int PORT = 9999; // ← Убедитесь, что совпадает с Server.java
    private static final String BASE_IP = "127.0.0.";
    private static final int START = 1;
    private static final int END = 5;

    public static void main(String[] args) {
        System.out.println("🔍 Сканирование порта " + PORT + " в диапазоне " + BASE_IP + START + "–" + BASE_IP + END);

        for (int i = START; i <= END; i++) {
            String ip = BASE_IP + i;
            if (isPortOpen(ip, PORT)) {
                System.out.println("✅ Сервер найден: " + ip + ":" + PORT);
            }
        }
        System.out.println("🎉 Сканирование завершено.");
    }

    private static boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(host, port), 300);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}