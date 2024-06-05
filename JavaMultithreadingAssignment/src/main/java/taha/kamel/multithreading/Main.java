package taha.kamel.multithreading;

import taha.kamel.multithreading.game.OldMaidCardGame;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Enter number of players: ");
        Scanner sc = new Scanner(System.in);
        int num = Integer.parseInt(sc.nextLine());
        OldMaidCardGame game =
                OldMaidCardGame.Builder()
                .setNumberOfPlayers(num)
                .build();
        game.startGame();
    }
}