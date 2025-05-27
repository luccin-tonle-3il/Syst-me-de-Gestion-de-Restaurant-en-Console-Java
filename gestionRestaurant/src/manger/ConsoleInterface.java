package manger;

import java.util.Scanner;

public abstract class ConsoleInterface {
	protected Scanner scanner;
    protected boolean running;

    public ConsoleInterface() {
        scanner = new Scanner(System.in);
        running = true;
    }

    public abstract void demarrerInterface();
    public abstract void processChoice(int choice);
    public abstract void start();
    protected void printError(String message) {
        System.out.println("\nERREUR: " + message + "\n");
    }
    
    protected void close() {
        scanner.close();
        running = false;
    }
}
