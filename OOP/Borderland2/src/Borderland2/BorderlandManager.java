/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ilkin
 */
public class BorderlandManager {
    private GameMaster gameMaster;
    private List<Player> players;
    private List<Arena> arenas;
    private List<Game> games;
    private Random random;
    private static final int TOTAL_CARDS = 52;

    /**
     * Creates a new BorderlandManager.
     */
    public BorderlandManager() {
        this.gameMaster = new GameMaster();
        this.players = new ArrayList<>();
        this.arenas = new ArrayList<>();
        this.games = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Loads player data from a file.
     * Expected format: Name Mental Physical
     * 
     * @param filename The name of the file to load
     * @throws IOException If an I/O error occurs
     */
    public void loadPlayers(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String name = parts[0];
                    int mental = Integer.parseInt(parts[1]);
                    int physical = Integer.parseInt(parts[2]);
                    
                    Player player = new Player(name, mental, physical);
                    players.add(player);
                    gameMaster.addPlayer(player);
                }
            }
        }
        
        System.out.println("Loaded " + players.size() + " players");
    }

    /**
     * Loads game data from a file.
     * Expected format: Name Type Difficulty
     * 
     * @param filename The name of the file to load
     * @throws IOException If an I/O error occurs
     */
    public void loadGames(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String name = parts[0];
                    GameType type = parts[1].equalsIgnoreCase("mental") ? 
                                   GameType.MENTAL : GameType.PHYSICAL;
                    int difficulty = Integer.parseInt(parts[2]);
                    
                    Game game = new Game(name, type, difficulty);
                    games.add(game);
                }
            }
        }
        
        System.out.println("Loaded " + games.size() + " games");
    }

    /**
     * Loads arena data from a file.
     * Expected format: Name Hazard
     * 
     * @param filename The name of the file to load
     * @throws IOException If an I/O error occurs
     */
    public void loadArenas(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String name = parts[0];
                    Hazard hazard = Hazard.valueOf(parts[1].toUpperCase());
                    
                    Arena arena = new Arena(name, hazard);
                    arenas.add(arena);
                    gameMaster.addArena(arena);
                }
            }
        }
        
        System.out.println("Loaded " + arenas.size() + " arenas");
    }

    /**
     * Sets up the game by assigning games to arenas.
     */
    public void setupGame() {
        // Assign games to arenas
        for (Game game : games) {
            if (!arenas.isEmpty()) {
                Arena randomArena = arenas.get(random.nextInt(arenas.size()));
                randomArena.addGame(game);
            }
        }
        
        System.out.println("Game setup complete");
        for (Arena arena : arenas) {
            System.out.println(arena.getName() + " has " + arena.getGames().size() + " games");
        }
    }

    /**
     * Starts a game in a specific arena.
     * 
     * @param arena The arena where the game takes place
     * @param game The game to start
     */
    public void startGame(Arena arena, Game game) {
        gameMaster.runGame(arena, game);
    }

    /**
     * Checks if the game is over.
     * The game ends when all cards have been collected or all players are inactive.
     * 
     * @return True if the game is over, false otherwise
     */
    public boolean checkGameStatus() {
        // Check if all players are inactive
        boolean allInactive = true;
        int totalCardsCollected = 0;
        
        for (Player player : players) {
            if (player.isActive()) {
                allInactive = false;
            }
            totalCardsCollected += player.getTotalCards();
        }
        
        if (allInactive) {
            System.out.println("Game over: All players are inactive");
            return true;
        }
        
        // Check if all cards have been collected
        if (totalCardsCollected >= TOTAL_CARDS) {
            System.out.println("Game over: All cards have been collected");
            return true;
        }
        
        return false;
    }

    /**
     * Prints the status of all players.
     */
    public void printPlayerStatus() {
        System.out.println("\nPlayer Status:");
        for (Player player : players) {
            System.out.println(player);
        }
    }

    /**
     * Gets the list of players.
     * 
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
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
     * Gets the list of games.
     * 
     * @return The list of games
     */
    public List<Game> getGames() {
        return new ArrayList<>(games);
    }
}
