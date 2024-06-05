package taha.kamel.multithreading.game;

import taha.kamel.multithreading.player.Player;

import java.util.List;

public interface CardGame {
    void join(Player player);
    void dealCards();
    void startGame();
    int getNumPlayers();
    List<Player> getPlayers();
    void createPlayers(int number);
    Player getNextPlayer(Player currentPlayer);
    void removePlayer(Player player);
}
