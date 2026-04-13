/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Ilkin
 */
public class Game {
    private String name;
    private GameType type;
    private int difficulty;
    private Random random;

    /**
     * Creates a new game with specified parameters.
     * 
     * @param name The name of the game
     * @param type The type of the game (MENTAL or PHYSICAL)
     * @param difficulty The difficulty level of the game
     */
    public Game(String name, GameType type, int difficulty) {
        this.name = name;
        this.type = type;
        this.difficulty = difficulty;
        this.random = new Random();
    }

    /**
     * Gets the name of this game.
     * 
     * @return The game's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of this game.
     * 
     * @return The game's type (MENTAL or PHYSICAL)
     */
    public GameType getType() {
        return type;
    }

    /**
     * Gets the difficulty level of this game.
     * 
     * @return The game's difficulty level
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Plays the game with the given list of players.
     * Players with appropriate stats above the difficulty level win and receive a card.
     * Players who lose become more afraid.
     * 
     * @param players List of players participating in the game
     * @return List of players who won the game
     */
    public List<Player> play(List<Player> players) {
        List<Player> winners = new ArrayList<>();
        
        for (Player player : players) {
            if (!player.isActive()) {
                continue;
            }
            
            int relevantStat = (type == GameType.MENTAL) ? 
                              player.getMentals() : 
                              player.getPhysicals();
            
            if (relevantStat >= difficulty) {
                // Player wins
                winners.add(player);
                Card reward = generateRandomCard();
                player.addCard(reward);
            } else {
                // Player loses
                player.increaseFear(1);
            }
        }
        
        return winners;
    }
    
    /**
     * Generates a random card as a reward.
     * 
     * @return A randomly generated card
     */
    private Card generateRandomCard() {
        Suit[] suits = Suit.values();
        Suit randomSuit = suits[random.nextInt(suits.length)];
        int randomNumber = random.nextInt(13) + 1;
        
        return new Card(randomSuit, randomNumber);
    }
    
    @Override
    public String toString() {
        return name + " (" + type + ", Difficulty: " + difficulty + ")";
    }
}
