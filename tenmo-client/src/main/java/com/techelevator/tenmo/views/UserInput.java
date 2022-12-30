package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.UserCredentials;
import java.util.Scanner;

public class UserInput {
    private static final Scanner input = new Scanner(System.in);

    public static int promptForMenuSelection(String prompt) {
        UserOutput.printSpace();
        int menuSelection;
        UserOutput.printInlineMessage(prompt);
        try {
            menuSelection = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public static UserCredentials promptForCredentials() {
        UserOutput.printSpace();
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public static String promptForString(String prompt) {
        UserOutput.printInlineMessage(prompt);
        return input.nextLine();
    }

    public static void pause() {
        UserOutput.printSpace();
        UserOutput.printInlineMessage("Press Enter to continue...");
        input.nextLine();
    }
}
