/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package borderland2test;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import Borderland2.*;
/**
 *
 * @author Ilkin
 */
public class BehaviorSystemTest {
   @Test
    public void testBehaviorTypeConstructor() {
        Player player1 = new Player("Alice", 8, 7);
        Player player2 = new Player("Bob", 6, 9, BehaviorType.AGGRESSIVE);
        
        // Default constructor should set RATIONAL behavior
        assertEquals(BehaviorType.RATIONAL, player1.getBehaviorType());
        
        // Constructor with behavior type parameter should set that type
        assertEquals(BehaviorType.AGGRESSIVE, player2.getBehaviorType());
    }
    
    @Test
    public void testBehaviorTypeSetting() {
        Player player = new Player("Charlie", 7, 7);
        
        // Test setting each behavior type
        player.setBehaviorType(BehaviorType.CAUTIOUS);
        assertEquals(BehaviorType.CAUTIOUS, player.getBehaviorType());
        
        player.setBehaviorType(BehaviorType.AGGRESSIVE);
        assertEquals(BehaviorType.AGGRESSIVE, player.getBehaviorType());
        
        player.setBehaviorType(BehaviorType.UNSTABLE);
        assertEquals(BehaviorType.UNSTABLE, player.getBehaviorType());
        
        player.setBehaviorType(BehaviorType.RATIONAL);
        assertEquals(BehaviorType.RATIONAL, player.getBehaviorType());
    }
    
    @Test
    public void testTrustRelationsFunctions() {
        Player player1 = new Player("Dave", 6, 8);
        Player player2 = new Player("Eve", 9, 5);
        Player player3 = new Player("Frank", 7, 7);
        
        // Test initial trust (should be 0)
        assertEquals(0, player1.getTrust(player2));
        
        // Test setting trust
        player1.setTrust(player2, 5);
        assertEquals(5, player1.getTrust(player2));
        
        // Trust should be directional (not automatically reciprocal)
        assertEquals(0, player2.getTrust(player1));
        
        // Test modifying trust positively
        player1.modifyTrust(player2, 3);
        assertEquals(8, player1.getTrust(player2));
        
        // Test modifying trust negatively
        player1.modifyTrust(player2, -4);
        assertEquals(4, player1.getTrust(player2));
        
        // Test multiple trust relationships
        player1.setTrust(player3, -2);
        assertEquals(-2, player1.getTrust(player3));
        assertEquals(4, player1.getTrust(player2));
        
        // Test getting all trust relations
        Map<Player, Integer> allTrust = player1.getAllTrustRelations();
        assertEquals(2, allTrust.size());
        assertEquals(Integer.valueOf(4), allTrust.get(player2));
        assertEquals(Integer.valueOf(-2), allTrust.get(player3));
    }
    
    @Test
    public void testBehaviorStateManagerFearSpike() {
        Player player = new Player("Grace", 7, 6);
        BehaviorStateManager manager = new BehaviorStateManager();
        
        // Small fear increase shouldn't change state
        manager.handleFearSpike(player, 2);
        assertEquals(BehaviorType.RATIONAL, player.getBehaviorType());
        
        // Large fear spike should change Rational to Cautious
        manager.handleFearSpike(player, 4);
        assertEquals(BehaviorType.CAUTIOUS, player.getBehaviorType());
        
        // Another large fear spike should change Cautious to Unstable
        manager.handleFearSpike(player, 5);
        assertEquals(BehaviorType.UNSTABLE, player.getBehaviorType());
        
        // Fear spike shouldn't change Unstable state further
        manager.handleFearSpike(player, 5);
        assertEquals(BehaviorType.UNSTABLE, player.getBehaviorType());
    }
    
    @Test
    public void testBehaviorStateManagerBetrayal() {
        Player player1 = new Player("Heidi", 8, 5);
        Player player2 = new Player("Ivan", 6, 7, BehaviorType.CAUTIOUS);
        Player player3 = new Player("Betrayer", 5, 9);
        BehaviorStateManager manager = new BehaviorStateManager();
        
        // Betrayal should change Rational to Aggressive
        manager.handleBetrayal(player1, player3);
        assertEquals(BehaviorType.AGGRESSIVE, player1.getBehaviorType());
        assertTrue(player1.getTrust(player3) < 0); // Trust should be negative
        
        // Betrayal should change Cautious to Aggressive
        manager.handleBetrayal(player2, player3);
        assertEquals(BehaviorType.AGGRESSIVE, player2.getBehaviorType());
        
        // Set player to Unstable to test
        player1.setBehaviorType(BehaviorType.UNSTABLE);
        manager.handleBetrayal(player1, player3);
        // Unstable should remain Unstable after betrayal
        assertEquals(BehaviorType.UNSTABLE, player1.getBehaviorType());
    }
    
    @Test
    public void testBehaviorStateManagerProlongedAggression() {
        Player player = new Player("Jack", 5, 6);
        player.setBehaviorType(BehaviorType.AGGRESSIVE);
        BehaviorStateManager manager = new BehaviorStateManager();
        
        // High stamina should not change state
        manager.handleProlongedAggression(player, 5, 4);
        assertEquals(BehaviorType.AGGRESSIVE, player.getBehaviorType());
        
        // Low stamina but short aggression time should not change state
        manager.handleProlongedAggression(player, 1, 2);
        assertEquals(BehaviorType.AGGRESSIVE, player.getBehaviorType());
        
        // Low stamina and long aggression time should change to Unstable
        manager.handleProlongedAggression(player, 1, 5);
        assertEquals(BehaviorType.UNSTABLE, player.getBehaviorType());
    }
    
    @Test
    public void testBehaviorStateManagerRestAndTrustBuilding() {
        BehaviorStateManager manager = new BehaviorStateManager();
        
        // Test recovery from Aggressive to Rational
        Player player1 = new Player("Kelly", 7, 6, BehaviorType.AGGRESSIVE);
        manager.handleRestOrTrustBuilding(player1, 6);
        assertEquals(BehaviorType.RATIONAL, player1.getBehaviorType());
        
        // Test recovery from Cautious to Rational
        Player player2 = new Player("Liam", 8, 5, BehaviorType.CAUTIOUS);
        manager.handleRestOrTrustBuilding(player2, 6);
        assertEquals(BehaviorType.RATIONAL, player2.getBehaviorType());
        
        // Test recovery from Unstable to Cautious
        Player player3 = new Player("Mia", 6, 8, BehaviorType.UNSTABLE);
        manager.handleRestOrTrustBuilding(player3, 6);
        assertEquals(BehaviorType.CAUTIOUS, player3.getBehaviorType());
        
        // Test insufficient trust/rest doesn't change state
        Player player4 = new Player("Noah", 7, 7, BehaviorType.AGGRESSIVE);
        manager.handleRestOrTrustBuilding(player4, 3);
        assertEquals(BehaviorType.AGGRESSIVE, player4.getBehaviorType());
        
        // Test that Rational stays Rational
        Player player5 = new Player("Olivia", 9, 4);
        manager.handleRestOrTrustBuilding(player5, 6);
        assertEquals(BehaviorType.RATIONAL, player5.getBehaviorType());
    }
    
    @Test
    public void testCompleteStateTransitionCycle() {
        Player player = new Player("Pat", 7, 7);
        Player betrayer = new Player("Quinn", 6, 8);
        BehaviorStateManager manager = new BehaviorStateManager();
        
        // Start in Rational state
        assertEquals(BehaviorType.RATIONAL, player.getBehaviorType());
        
        // Transition to Cautious via Fear
        manager.handleFearSpike(player, 4);
        assertEquals(BehaviorType.CAUTIOUS, player.getBehaviorType());
        
        // Transition to Aggressive via Betrayal
        manager.handleBetrayal(player, betrayer);
        assertEquals(BehaviorType.AGGRESSIVE, player.getBehaviorType());
        
        // Transition to Unstable via Prolonged Aggression
        manager.handleProlongedAggression(player, 1, 4);
        assertEquals(BehaviorType.UNSTABLE, player.getBehaviorType());
        
        // Transition back to Cautious via Trust Building
        manager.handleRestOrTrustBuilding(player, 7);
        assertEquals(BehaviorType.CAUTIOUS, player.getBehaviorType());
        
        // Finally back to Rational via more Trust Building
        manager.handleRestOrTrustBuilding(player, 6);
        assertEquals(BehaviorType.RATIONAL, player.getBehaviorType());
    }
    
    @Test
    public void testToStringIncludesBehaviorType() {
        Player player = new Player("Ryan", 6, 8, BehaviorType.AGGRESSIVE);
        String description = player.toString();
        
        // The toString output should contain the behavior type
        assertTrue(description.contains("Behavior: AGGRESSIVE"));
    } 
}
