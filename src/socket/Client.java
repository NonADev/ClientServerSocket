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
        DataOutputStream out = new DataOutputStream(servidor.getOutputStream());

        MessageDigest md5 = MessageDigest.getInstance("MD5");

        out.writeByte(1);
        md5.update("wesley".getBytes());
        out.writeUTF(Md5ToString(md5.digest()));

        out.writeByte(2);
        md5.update("password".getBytes());
        out.writeUTF(Md5ToString(md5.digest()));

        out.writeByte(3);
        out.writeUTF("2+3");

        out.writeByte(-1);
        out.flush();

        DataInputStream in = new DataInputStream(servidor.getInputStream());

        boolean done = false;
        while (!done) {
            switch (in.readByte()) {
                case 1:
                    System.out.println("Usuario Presente:: "+in.readBoolean());
                    break;
                case 2:
                    System.out.println("Token:: "+in.readUTF());
                    break;
                case 3:
                    System.out.println("Resultado Equação:: "+in.readDouble());
                    break;

                case -1:
                    done = true;
            }
        }

        servidor.close();
    }
}
