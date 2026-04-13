/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Ilkin
 */
public class Arena {
    private String name;
    private Hazard hazard;
    private List<Game> games;

    /**
     * Creates a new arena with the given name and hazard.
     * 
     * @param name The name of the arena
     * @param hazard The environmental hazard in this arena
     */
    public Arena(String name, Hazard hazard) {
        this.name = name;
        this.hazard = hazard;
        this.games = new ArrayList<>();
    }

    /**
     * Gets the name of this arena.
     * 
     * @return The arena's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the hazard in this arena.
     * 
     * @return The arena's hazard
     */
    public Hazard getHazard() {
        return hazard;
    }

    /**
     * Adds a game to this arena.
     * 
     * @param game The game to add
     */
    public void addGame(Game game) {
        games.add(game);
    }

    /**
     * Activates the hazard in this arena, affecting all active players.
     * This increases each player's fear level based on the hazard's intensity.
     * 
     * @param players The list of players in the arena
     */
    public void activateHazard(List<Player> players) {
        for (Player player : players) {
            if (player.isActive()) {
                for (int i = 0; i < hazard.getFearIncrease(); i++) {
                    player.increaseFear(1);
                }
            }
        }
    }

    /**
     * Gets the list of games in this arena.
     * 
     * @return The list of games
     */
    public List<Game> getGames() {
        return new ArrayList<>(games);
    }
    
    @Override
    public String toString() {
        return name + " (Hazard: " + hazard + ", Games: " + games.size() + ")";
    }
}
