package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;
import edu.school21.sockets.config.ClientApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {
    public static int PORT_NUM;

    public static void main(String[] args) {
        checkArg(args);

        ApplicationContext context = new AnnotationConfigApplicationContext(ClientApplicationConfig.class);

        Client client = context.getBean("client", Client.class);
        client.connect(PORT_NUM);
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
