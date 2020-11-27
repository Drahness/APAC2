package com.ieseljust.ad.myDBMS;

import java.sql.SQLException;
import java.util.Scanner;

public class DBMan {
	private static String user = "root";
	private static String pass="root";
	private static String ip="localhost";
	private static String port="3306";
    public static void main(String[] args) throws SQLException{

        ConnectionManager cm;
        String buff = "";
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Server address ("+ip+"): "+ConsoleColors.RESET);
            buff = keyboard.nextLine();
            if(!buff.isEmpty()) {
            	ip = buff;
            }
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Port ("+port+"): "+ConsoleColors.RESET);
            buff = keyboard.nextLine();
            if(!buff.isEmpty()) {
            	port = buff;
            }
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Username ("+user+"): "+ConsoleColors.RESET);
            buff = keyboard.nextLine();
            if(!buff.isEmpty()) {
            	user = buff;
            }
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"# Password ("+pass+"): "+ConsoleColors.BLACK);
            buff = keyboard.nextLine();
            if(!buff.isEmpty()) {
            	pass = buff;
            }
            System.out.print(ConsoleColors.RESET);
            cm = new ConnectionManager(ip, port, user, pass);
        } while(cm.connectDBMS()==null);
        cm.startShell();
        keyboard.close();
    }
}
