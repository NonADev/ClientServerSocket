package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client {
    public static String Md5ToString(byte[] b) {
        return new BigInteger(1, b).toString(16);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Socket servidor = new Socket("localhost", 8099);

        DataInputStream in = new DataInputStream(servidor.getInputStream());
        DataOutputStream out = new DataOutputStream(servidor.getOutputStream());

        out.writeByte(1);
        out.writeUTF("wesley");

        out.writeByte(2);
        out.writeUTF("qwe");

        out.writeByte(3);
        out.writeUTF("2-3");

        out.writeByte(-1);
        out.flush();

        boolean done = false;
        while (!done) {
            switch (in.readByte()) {
                case 1:
                    Boolean a = in.readBoolean();
                    System.out.println("Usuario Presente:: "+a);
                    if (!a) {
                        servidor.close();
                        return;
                    }
                    break;
                case 2:
                    System.out.println("Token:: "+in.readUTF());
                    break;
                case 3:
                    System.out.println("Resultado Equação:: "+in.readUTF());
                    break;

                case -1:
                    done = true;
            }
        }

        servidor.close();
    }
}
