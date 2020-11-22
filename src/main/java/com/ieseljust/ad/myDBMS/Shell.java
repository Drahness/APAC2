package com.ieseljust.ad.myDBMS;

import java.util.Scanner;

public interface Shell {
    public default void startShell(){
		Scanner keyboard = new Scanner(System.in);
		String command;
		do {
			System.out.print(getShellString());
			command = keyboard.nextLine();
			startShell(command);
		} while (!command.equals("quit"));
		keyboard.close();
        }
    public String getShellString();
	public boolean startShell(String command);
}
