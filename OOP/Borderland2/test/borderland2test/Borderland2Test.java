/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package borderland2test;

import org.junit.Test;
import static org.junit.Assert.*;
import Borderland2.*;
/**
 *
 * @author Ilkin
 */
public class Borderland2Test {
    @Test
    public void testPlayerCreation() {
        Player player = new Player("Alex", 8, 6);
        
        assertEquals("Alex", player.getName());
        assertEquals(8, player.getMentals());
        assertEquals(6, player.getPhysicals());
        assertEquals(0, player.getFear());
        assertTrue(player.isActive());
    }
    
    @Test
    public void testIncreaseFear() {
        Player player = new Player("Alex", 8, 6);
        
        player.increaseFear(1);
        assertEquals(1, player.getFear());
        assertTrue(player.isActive());
        
        player.increaseFear(1);
        assertEquals(2, player.getFear());
        assertTrue(player.isActive());
        
        player.increaseFear(1);
        assertEquals(3, player.getFear());
        assertTrue(player.isActive());
    }
    
}
