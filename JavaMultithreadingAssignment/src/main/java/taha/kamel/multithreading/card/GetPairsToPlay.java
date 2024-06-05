package taha.kamel.multithreading.card;

import taha.kamel.multithreading.card.Card;
import taha.kamel.multithreading.card.GetCardsToPlayStrategy;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GetPairsToPlay implements GetCardsToPlayStrategy {
    @Override
    public List<Card> cardsToPlay(List<Card> hand) {
        hand.sort(Comparator.comparing(Card::getLabel));
        List<Card> cardsToPlay = new LinkedList<>();
        for (int i = 0; i < hand.size() - 1; i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                if (!hand.get(i).getLabel().equals(hand.get(j).getLabel())) break;
                if (hand.get(i).getColor().equals(hand.get(j).getColor())) {
                    cardsToPlay.add(hand.get(i));
                    cardsToPlay.add(hand.get(j));
                    break;
                }
            }
        }
        return cardsToPlay;
    }
}
