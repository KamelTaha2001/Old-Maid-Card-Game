package taha.kamel.multithreading.player;

import taha.kamel.multithreading.card.Card;
import taha.kamel.multithreading.card.GetCardsToPlayStrategy;
import taha.kamel.multithreading.card.GetPairsToPlay;
import taha.kamel.multithreading.draw.DrawFromNextPlayer;
import taha.kamel.multithreading.draw.DrawStrategy;
import taha.kamel.multithreading.game.CardGame;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public abstract class Player implements Runnable {
    private CardGame game;
    private int id;
    private String name;
    private LinkedList<Card> cards;
    private CountDownLatch allStarted;
    private CountDownLatch allDone;
    private boolean isDone;
    private DrawStrategy drawStrategy;
    private GetCardsToPlayStrategy cardsToPlayStrategy;

    public Player(CardGame game, String name, CountDownLatch allStarted, CountDownLatch allDone, int id) {
        this.game = game;
        this.name = name;
        this.allStarted = allStarted;
        this.allDone = allDone;
        cards = new LinkedList<>();
        game.join(this);
        this.id = id;
        drawStrategy = new DrawFromNextPlayer();
        cardsToPlayStrategy = new GetPairsToPlay();
    }

    // -------------------------- Abstract Methods --------------------------

    public abstract void playTurn();


    // -------------------------- Getters and Setters --------------------------

    public String getName() { return name; }

    public CardGame getGame() {
        return game;
    }

    public Iterator<Card> getCards() { return cards.iterator(); }

    public CountDownLatch getAllStartedLatch() {
        return allStarted;
    }

    public CountDownLatch getAllDoneLatch() {
        return allDone;
    }

    public int getId() {
        return id;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public DrawStrategy getDrawStrategy() {
        return drawStrategy;
    }

    public void setDrawStrategy(DrawStrategy drawStrategy) {
        this.drawStrategy = drawStrategy;
    }

    public GetCardsToPlayStrategy getCardsToPlayStrategy() {
        return cardsToPlayStrategy;
    }

    public void setCardsToPlayStrategy(GetCardsToPlayStrategy cardsToPlayStrategy) {
        this.cardsToPlayStrategy = cardsToPlayStrategy;
    }

    // -------------------------- Public Methods --------------------------

    /**
     * Adds a card to the cards in player's hand.
     * @param givenCard The card to add.
     */
    public void giveCard(Card givenCard) {
        cards.add(givenCard);
    }


    /**
     * @return The cards the player can discard.
     */
    public List<Card> cardsToPlay() {
        return cardsToPlayStrategy.cardsToPlay(cards);
    }

    /**
     * @return The number of cards in hand.
     */
    public int getNumberOfCards() {
        return cards.size();
    }

    /**
     * Throws a card from hand.
     */
    public Card discardCard(Card card) {
        cards.remove(card);
        if (getNumberOfCards() == 0) setDone(true);
        return card;
    }

    /**
     * Throws a random card from hand.
     */
    public Card discardRandomCard() {
        int index = (int)(Math.random() * cards.size());
        return discardCard(cards.get(index));
    }

    /**
     * @return A drawn card from another player depending on the used strategy of drawing.
     */
    public Card drawCard() {
        return drawStrategy.draw(getGame(), this);
    }

    public boolean hasCards() { return cards.size() > 0; }

    // -------------------------- Overridden Methods --------------------------

    @Override
    public String toString() {
        return name;
    }
}
