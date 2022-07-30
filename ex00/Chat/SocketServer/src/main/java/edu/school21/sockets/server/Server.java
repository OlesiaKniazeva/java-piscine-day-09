package edu.school21.sockets.server;

import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component("server")
public class Server {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private final UsersService usersService;

    @Autowired
    public Server(UsersService usersService) {
        this.usersService = usersService;
    }
    public void connect(int port) {
        try (ServerSocket server = new ServerSocket(port)) {

            System.out.println("Waiting for connection from client");
            clientSocket = server.accept();
            System.out.println("Connection to client established!");

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String greeting = "Hello from server!\n";
                out.write(greeting);
                out.flush();

                while (true) {
                    String word;

                    while (!(word = in.readLine()).equals("signUp")) {
                        System.out.println("client command: " + word);
                        out.write("No such command\n");
                        out.flush();
                    }

                    out.write( "Enter username\n");
                    out.flush();

                    String username = in.readLine();
                    System.out.println("got: " + username);

                    out.write( "Enter password\n");
                    out.flush();

                    String password = in.readLine();
                    System.out.println("got: " + password);

                    if (usersService.signUp(username, password)) {
                        out.write("Successful!!\n");
                    } else {
                        out.write("User already registered!\n");
                    }
                    out.flush();
                    break;
                }

            } finally {
                System.out.println("Connection closed");
                in.close();
                out.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
