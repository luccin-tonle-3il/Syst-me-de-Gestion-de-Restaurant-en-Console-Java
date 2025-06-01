package manger;

import java.sql.SQLException;
import java.util.Scanner;

public abstract class ConsoleInterface {
	protected Scanner scanner;
    protected boolean running;

    public ConsoleInterface() {
        scanner = new Scanner(System.in);
        running = true;
    }

    public abstract void demarrerInterface();
    public abstract void processChoice(int choice) throws SQLException;
    public abstract void start() throws SQLException;
    protected void printError(String message) {
        System.out.println("\nERREUR: " + message + "\n");
    }
    protected void close() {
        scanner.close();
        running = false;
    }
}
