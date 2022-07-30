package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.models.User;
import edu.school21.sockets.server.Server;
import edu.school21.sockets.services.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {
    public static int PORT_NUM;

    public static void main(String[] args) {
        checkArg(args);

        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);

        Server server = context.getBean("server", Server.class);

        server.connect(PORT_NUM);


        UsersService service = context.getBean("UsersService", UsersService.class);
        System.out.println(service.findAll());
        User user = service.findById(2L);
        System.out.println("Hashed password: " + user.getPassword());
    }

    private static void checkArg(String[] args) {
        final String arg = "--port=";

        if (!(args.length == 1) || !args[0].startsWith(arg) || args[0].length() == arg.length()) {
            exitError();
        }
        if (new Scanner(args[0].substring(args[0].indexOf('=') + 1)).hasNextInt()) {
            PORT_NUM =Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1));
            if (PORT_NUM < 0 || PORT_NUM > 65535) {
                System.err.println("You should enter valid port number");
                exitError();
            }
        } else {
           exitError();
        }
    }

    private static void exitError() {
        System.err.println("You should enter one argument --port=NUMBER_OF_PORT");
        System.exit(1);
    }
}