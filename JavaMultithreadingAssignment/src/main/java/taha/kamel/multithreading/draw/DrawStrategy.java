package taha.kamel.multithreading.draw;

import taha.kamel.multithreading.player.Player;
import taha.kamel.multithreading.card.Card;
import taha.kamel.multithreading.game.CardGame;

public interface DrawStrategy {
    Card draw(CardGame game, Player drawingPlayer);
}
