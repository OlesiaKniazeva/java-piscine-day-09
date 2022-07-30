package edu.school21.sockets.client;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

@Component("client")
public class Client {
    private static String SUCCESS = "Successful!!";
    private static String UNSUCCESS = "User already registered!";
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void connect(int port) {
        try {
            try (Socket client = new Socket("localhost", port)) {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(System.in));

                String serverWord = in.readLine();
                System.out.println(serverWord);

                while (true) {
                    System.out.print("> ");
                    String word = reader.readLine();

                    out.write(word + "\n");
                    out.flush();

                    serverWord = in.readLine();
                    System.out.println(serverWord);

                    if (SUCCESS.equals(serverWord) || UNSUCCESS.equals(serverWord)) {
                        break;
                    }
                }

            } finally {
                in.close();
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
