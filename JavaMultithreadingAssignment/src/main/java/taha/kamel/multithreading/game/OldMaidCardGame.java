package taha.kamel.multithreading.game;

import taha.kamel.multithreading.card.Card;
import taha.kamel.multithreading.card.SimpleCard;
import taha.kamel.multithreading.color.Colors;
import taha.kamel.multithreading.player.Player;
import taha.kamel.multithreading.player.SimplePlayer;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class OldMaidCardGame implements CardGame {
    private int minPlayers = 2;
    private int maxPlayers = 8;
    private int numberOfPlayers = 2;
    private List<Card> deck;
    private List<Player> players;
    private List<Player> winningPlayers;
    private List<Thread> playersThreads;
    private CountDownLatch allStarted;
    private CountDownLatch allDone;


    private OldMaidCardGame() {
        deck = new ArrayList<>(43);
        players = new LinkedList<>();
        winningPlayers = new LinkedList<>();
        allStarted = new CountDownLatch(numberOfPlayers);
        allDone = new CountDownLatch(numberOfPlayers);
        generateCards();
    }

    public static class MaidBuilder {
        private OldMaidCardGame oldMaidCardGame;

        public MaidBuilder() {
            oldMaidCardGame = new OldMaidCardGame();
        }

        public MaidBuilder setNumberOfPlayers(int numPlayers) {
            oldMaidCardGame.setNumberOfPlayers(numPlayers);
            return this;
        }

        public OldMaidCardGame build() {
            return oldMaidCardGame;
        }
    }

    // -------------------------- Public Methods --------------------------

    public static MaidBuilder Builder() { return new MaidBuilder(); }

    public int getNumPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        allStarted = new CountDownLatch(numberOfPlayers);
        allDone = new CountDownLatch(numberOfPlayers);
    }

    // -------------------------- Private Methods --------------------------

    private void generateCards() {
        int id = 0;
        String[] labels = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "JOKER" };

        for (int i = 0; i < labels.length - 1; i++) {
            deck.add(new SimpleCard(id++, labels[i], Colors.RED, "Diamonds"));
            deck.add(new SimpleCard(id++, labels[i], Colors.RED, "Hearts"));
            deck.add(new SimpleCard(id++, labels[i], Colors.BLACK, "Spades"));
            deck.add(new SimpleCard(id++, labels[i], Colors.BLACK, "Clubs"));
        }
        deck.add(new SimpleCard(id, labels[labels.length - 1], Colors.GREY, "JOKER"));
        shuffle(deck);
    }

    private void shuffle(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) (Math.random() * (cards.size()));
            Card card = cards.get(randomIndex);
            cards.set(randomIndex, cards.get(i));
            cards.set(i, card);
        }
    }

    private void printResults() {
        for (Player p : winningPlayers)
            System.out.println(p.getName());
    }

    // -------------------------- Overridden Methods --------------------------

    @Override
    public synchronized void join(Player player) {
        if (players.contains(player))
            throw new IllegalArgumentException("Player already exists");
        players.add(player);
    }

    @Override
    public void dealCards() {
        synchronized (players) {
            if (players.size() < minPlayers)
                throw new IllegalStateException("No enough players!");
        }

        int playerIndex = 0;
        while (deck.size() > 0) {
            Card card = deck.remove(deck.size() - 1);
            synchronized (players) {
                players.get(playerIndex).giveCard(card);
                playerIndex = (playerIndex + 1) % players.size();
            }
        }
    }

    @Override
    public void startGame() {
        createPlayers(numberOfPlayers);

        try {
            allStarted.await();
        } catch (Exception e) {}
        synchronized (players.get(0)) {
            players.get(0).notify();
            System.out.println("\nFirst player is starting..\n");
        }

        try {
            allDone.await();
        } catch (Exception e) {}
        for (Thread t : playersThreads) {
            t.interrupt();
        }

        System.out.println("Win order:");
        printResults();
    }

    @Override
    public List<Player> getPlayers() { return players; }

    @Override
    public void createPlayers(int number) {
        if (number < minPlayers) throw new IllegalArgumentException("No enough players!");

        for (int i = 0; i < number; i++) {
            new SimplePlayer(this, "Player " + i, allStarted, allDone, i);
        }

        dealCards();
        System.out.println("-------------");
        for (Player p : players) {
            System.out.println(p.getName() + " initially has " + p.getNumberOfCards() + " cards.");
        }
        System.out.println("-------------");

        playersThreads = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            Thread playerThread = new Thread(players.get(i));
            playersThreads.add(playerThread);
            playerThread.start();
        }
    }

    @Override
    public Player getNextPlayer(Player player) {
        synchronized (players) {
            int nextPlayerIndex = (players.indexOf(player) + 1) % players.size();
            return players.get(nextPlayerIndex);
        }
    }

    @Override
    public void removePlayer(Player player) {
        winningPlayers.add(player);
        synchronized (players) {
            players.remove(player);
        }
    }
}
