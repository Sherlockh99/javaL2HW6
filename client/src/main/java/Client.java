import java.io.*;
import java.net.Socket;

public class Client {
    private static final String ADDRESS = "localhost";
    private static final int PORT = 8189;
    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {
        try {
            socket = new Socket(ADDRESS,PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Thread th = new Thread(() -> {
                try {
                    while (true) {
                       String line = reader.readLine();
                        if (line.equals("/end")) {
                            System.out.println("Client end session");
                        }
                        out.writeUTF(line);
                    }
                } catch (IOException e) {
                        e.printStackTrace();
                }
            });
            th.setDaemon(true);
            th.start();

            try {
                while (true) {
                    String str = in.readUTF();
                    if (str.equals("/end")) {
                        System.out.println("Server end session");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
