package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8099);
        Socket client = server.accept();

        DataInputStream in = new DataInputStream(client.getInputStream());

        boolean done = false;
        while (!done) {
            switch (in.readByte()) {
                case 1:
                    System.out.println("Usuario:: "+in.readUTF());
                    break;
                case 2:
                    System.out.println("Senha:: "+in.readUTF());
                    break;
                case 3:
                    System.out.println("Equação:: "+in.readUTF());
                    break;

                case -1:
                    done = true;
            }
        }

        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        out.writeByte(1);
        out.writeBoolean(true);

        out.writeByte(2);
        out.writeUTF("==asdqweqwrqweqweqwe");

        out.writeByte(3);
        out.writeDouble(340);

        out.writeByte(-1);
        out.flush();

        client.close();
        server.close();
    }
}
