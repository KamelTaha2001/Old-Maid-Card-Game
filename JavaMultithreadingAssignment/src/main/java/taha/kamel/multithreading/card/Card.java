package taha.kamel.multithreading.card;

import taha.kamel.multithreading.color.Color;

public abstract class Card {
    private int id;
    private String label;
    private Color color;
    private String suit;

    public Card(int id, String label, Color color, String suit) {
        this.id = id;
        this.label = label;
        this.color = color;
        this.suit = suit;
    }

    // -------------------------- Getters and Setters --------------------------

    public int getId() { return id; }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }


    // -------------------------- Overridden Methods --------------------------

    @Override
    public String toString() {
        return label + " of " + suit;
    }
}
