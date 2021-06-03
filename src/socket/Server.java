package socket;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.beans.Encoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {
    private static List<Usuario> usuarios = new ArrayList<>();

    private static void popularUsuarios() {
        usuarios.add(new Usuario("wesley", "qwe123QWE!@#"));
        usuarios.add(new Usuario("teste", "qwe"));
    }

    private static boolean usuarioPresente(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.usuario.equals(usuario.usuario) && u.senha.equals(usuario.senha)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException, ScriptException {
        popularUsuarios();

        ServerSocket server = new ServerSocket(8099);
        Socket client = server.accept();

        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        ChamadaDTO chamadaDTO = new ChamadaDTO();

        boolean done = false;
        while (!done) {
            if (chamadaDTO.usuario != null && chamadaDTO.senha != null) {
                if (!usuarioPresente(chamadaDTO)) {
                    out.writeByte(1);
                    out.writeBoolean(false);

                    out.writeByte(-1);
                    out.flush();

                    client.close();
                    server.close();
                    return;
                }
            }

            switch (in.readByte()) {
                case 1 -> {
                    chamadaDTO.usuario = in.readUTF();
                    System.out.println("Usuario:: " + chamadaDTO.usuario);
                }
                case 2 -> {
                    chamadaDTO.senha = in.readUTF();
                    System.out.println("Senha:: " + chamadaDTO.senha);
                }
                case 3 -> {
                    chamadaDTO.operacao = in.readUTF();
                    System.out.println("Equação:: " + chamadaDTO.operacao);
                    chamadaDTO.operacao = calcularOperacao(chamadaDTO.operacao);
                }
                case -1 -> {
                    if ((chamadaDTO.usuario.equals("wesley") && chamadaDTO.senha.equals("qwe123QWE!@#"))) {
                        out.writeByte(1);
                        out.writeBoolean(true);
                        done = true;
                    }
                    else {
                        out.writeByte(1);
                        out.writeBoolean(false);
                        done = true;
                    }
                }
            }
        }

        out.writeByte(2);
        out.writeUTF(generateString());

        out.writeByte(3);
        out.writeUTF(chamadaDTO.operacao);

        out.writeByte(-1);
        out.flush();

        client.close();
        server.close();
    }

    public static String generateString() {
        return UUID.randomUUID().toString();
    }

    private static String calcularOperacao(String operacao) {
        String charSplit = null;
        if (operacao.contains("-"))
            charSplit = "-";
        else if (operacao.contains("+"))
            charSplit = "\\+";
        else if (operacao.contains("*"))
            charSplit = "\\*";
        else if (operacao.contains("/"))
            charSplit = "/";

        String[] valoresString = operacao.split(charSplit);
        Double a = Double.parseDouble(valoresString[0]);
        Double b = Double.parseDouble(valoresString[1]);

        if (operacao.contains("-"))
            return "" + (a - b);
        if (operacao.contains("+"))
            return "" + (a + b);
        if (operacao.contains("*"))
            return "" + (a * b);
        if (operacao.contains("/"))
            return "" + (a / b);

        return null;
    }

    private static class Usuario {
        String usuario;
        String senha;

        public Usuario() {

        }

        public Usuario(String usuario, String senha) {
            this.senha = senha;
            this.usuario = usuario;
        }
    }

    private static class ChamadaDTO extends Usuario {
        String operacao;

        public ChamadaDTO() {

        }

        public ChamadaDTO(String usuario, String senha) {
            super(usuario, senha);
        }
    }
}
