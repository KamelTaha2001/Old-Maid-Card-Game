package taha.kamel.multithreading.player;

import taha.kamel.multithreading.card.Card;
import taha.kamel.multithreading.game.CardGame;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimplePlayer extends Player{

    public SimplePlayer(CardGame game, String name, CountDownLatch allStarted, CountDownLatch allDone, int id) {
        super(game, name, allStarted, allDone, id);
    }

    @Override
    public void playTurn() {
        if (isDone()) return;
        giveCard(drawCard());
        System.out.println("Discarding:");
        System.out.println(throwMatchingCards());
        System.out.println(getName() + " has " + getNumberOfCards() + " cards.");
        System.out.println("--------------------");
    }

    private List<Card> throwMatchingCards() {
        List<Card> toPlay = cardsToPlay();
        toPlay.forEach(this::discardCard);
        return toPlay;
    }

    @Override
    public void run() {
        throwMatchingCards();
        while (!isDone() && getNumberOfCards() > 0) {
            synchronized (this) {
                try {
                    getAllStartedLatch().countDown();
                    this.wait();
                } catch (Exception e) {
                    getAllDoneLatch().countDown();
                    break;
                }
            }

            playTurn();
            if (isDone()) {
                getAllDoneLatch().countDown();
                break;
            }
            Player nextPlayer = getGame().getNextPlayer(this);
            synchronized (nextPlayer) {
                nextPlayer.notify();
            }
        }
        if (getGame().getNumPlayers() > 1) {
            Player nextPlayer = getGame().getNextPlayer(this);
            synchronized (nextPlayer) {
                nextPlayer.notify();
            }
        }
        getGame().removePlayer(this);
    }
}
