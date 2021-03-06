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
			if(command.equals("help"))
				printHelp();
		} while (!command.equals("quit"));
    }
    public String getShellString();
	public boolean startShell(String command);
	public void printHelp();
}
