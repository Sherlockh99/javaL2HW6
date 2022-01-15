import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static Socket socket;
    private static final int PORT = 8189;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            socket = server.accept();
            System.out.println("Client connected!");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);

            Thread th = new Thread(() -> {
                while (true) {
                    try {
                        String line = scanner.nextLine();
                        if (line.equals("/end")) {
                            System.out.println("Server end session!");
                        }
                        out.writeUTF(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            th.setDaemon(true);
            th.start();

            //new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            System.out.println("Client end session!");
                            out.writeUTF("/end");
                            break;
                        }
                        System.out.println(str);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            //}).start();
    } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
