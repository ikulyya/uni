/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;

/**
 *
 * @author Ilkin
 */
public class Card {
    private Suit suit;
    private int number;

    /**
     * Creates a new card with specified suit and number.
     * 
     * @param suit The suit of the card
     * @param number The number of the card (1-13)
     */
    public Card(Suit suit, int number) {
        if (number < 1 || number > 13) {
            throw new IllegalArgumentException("Card number must be between 1 and 13");
        }
        this.suit = suit;
        this.number = number;
    }

    /**
     * Gets the suit of this card.
     * 
     * @return The card's suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Gets the number of this card.
     * 
     * @return The card's number (1-13)
     */
    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        String cardName;
        switch (number) {
            case 1:
                cardName = "Ace";
                break;
            case 11:
                cardName = "Jack";
                break;
            case 12:
                cardName = "Queen";
                break;
            case 13:
                cardName = "King";
                break;
            default:
                cardName = String.valueOf(number);
        }
        return cardName + " of " + suit;
    }
}

