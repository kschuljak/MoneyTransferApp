package com.techelevator.tenmo.views;

import java.util.Scanner;

public class UserInput {

    private Scanner input = new Scanner(System.in);

    public String getResponse(String message) {
        System.out.println(message);
        return input.nextLine().strip().toLowerCase();
    }
}
