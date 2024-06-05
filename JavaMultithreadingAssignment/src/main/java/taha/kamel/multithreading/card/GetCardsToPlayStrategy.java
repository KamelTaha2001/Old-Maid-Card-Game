package taha.kamel.multithreading.card;

import java.util.List;

public interface GetCardsToPlayStrategy {
    List<Card> cardsToPlay(List<Card> hand);
}
