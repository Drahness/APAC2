package com.ieseljust.ad.myDBMS;

import java.sql.SQLException;
import java.util.Scanner;

public class DBMan {
    public static void main(String[] args) throws SQLException{

        ConnectionManager cm;
        Scanner keyboard = new Scanner(System.in);
        String user, pass, ip, port;
        do {
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Server address: "+ConsoleColors.RESET);
            ip = keyboard.nextLine();
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Port: "+ConsoleColors.RESET);
            port = keyboard.nextLine();
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Username: "+ConsoleColors.RESET);
            user = keyboard.nextLine();
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Password: "+ConsoleColors.BLACK);
            pass = keyboard.nextLine();
            System.out.print(ConsoleColors.RESET);
            cm = new ConnectionManager(ip, port, user, pass);
        } while(cm.connectDBMS()==null);
        cm.startShell();
        keyboard.close();
    }
}
