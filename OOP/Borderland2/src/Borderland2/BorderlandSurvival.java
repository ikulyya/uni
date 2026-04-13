/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Borderland2;

import java.io.IOException;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Ilkin
 */
public class BorderlandSurvival {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        System.out.println("Welcome to Borderland Survival!");
        
        try {
            BorderlandManager manager = new BorderlandManager();
            
            manager.loadPlayers("players.txt");
            manager.loadGames("games.txt");
            manager.loadArenas("arena.txt");
            
            manager.setupGame();
            
            Random random = new Random();
            boolean gameOver = false;
            int round = 1;
            
            while (!gameOver) {
                System.out.println("\n=== Round " + round + " ===");
                
                List<Arena> arenas = manager.getArenas();
                if (arenas.isEmpty()) {
                    System.out.println("No arenas available. Game over.");
                    break;
                }
                Arena arena = arenas.get(random.nextInt(arenas.size()));
                
                List<Game> games = arena.getGames();
                if (games.isEmpty()) {
                    System.out.println("No games available in arena " + arena.getName() + ". Skipping round.");
                    continue;
                }
                Game game = games.get(random.nextInt(games.size()));
                
                // Run the game
                manager.startGame(arena, game);
                
                // Check game status
                gameOver = manager.checkGameStatus();
                
                // Print player status
                manager.printPlayerStatus();
                
                round++;
            }
            
            System.out.println("\nGame Over! Final Status:");
            manager.printPlayerStatus();
            
        } catch (IOException e) {
            System.err.println("Error loading game data:");
        }
   }
   
}
        