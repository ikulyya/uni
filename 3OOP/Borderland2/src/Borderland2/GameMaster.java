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
public class GameMaster {
    private List<Arena> arenas;
    private List<Player> players;
    private Random random;

    /**
     * Creates a new GameMaster.
     */
    public GameMaster() {
        this.arenas = new ArrayList<>();
        this.players = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Adds an arena to the GameMaster's control.
     * 
     * @param arena The arena to add
     */
    public void addArena(Arena arena) {
        arenas.add(arena);
    }

    /**
     * Adds a player to the GameMaster's control.
     * 
     * @param player The player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Runs a specific game in a specific arena.
     * This method activates the arena's hazard and then runs the game.
     * 
     * @param arena The arena where the game takes place
     * @param game The game to run
     * @return List of players who won the game
     */
    public List<Player> runGame(Arena arena, Game game) {
        System.out.println("Running game: " + game.getName() + " in arena: " + arena.getName());
        
        // Activate the hazard
        arena.activateHazard(players);
        
        // Play the game
        List<Player> winners = game.play(players);
        
        // Report results
        System.out.println("Game completed. Winners: " + winners.size() + " players");
        for (Player winner : winners) {
            System.out.println("- " + winner.getName() + " won and received a card");
        }
        
        return winners;
    }

    /**
     * Randomly selects the next game to play from available arenas.
     * 
     * @return A randomly selected game, or null if no games are available
     */
    public Game selectNextGame() {
        if (arenas.isEmpty()) {
            return null;
        }
        
        // Get a random arena
        Arena randomArena = arenas.get(random.nextInt(arenas.size()));
        List<Game> games = randomArena.getGames();
        
        if (games.isEmpty()) {
            return null;
        }
        
        // Get a random game from the arena
        return games.get(random.nextInt(games.size()));
    }

    /**
     * Gets the list of arenas.
     * 
     * @return The list of arenas
     */
    public List<Arena> getArenas() {
        return new ArrayList<>(arenas);
    }

    /**
     * Gets the list of players.
     * 
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
}
