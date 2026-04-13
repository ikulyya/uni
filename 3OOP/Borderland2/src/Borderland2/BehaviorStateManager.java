/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;


/**
 *
 * @author Ilkin
 */
public class BehaviorStateManager {
    private static final int FEAR_SPIKE_THRESHOLD = 3;
    private static final int LOW_STAMINA_THRESHOLD = 2;
    private static final int TRUST_BUILDING_THRESHOLD = 5;
    
    /**
     * Updates a player's behavior state based on a fear spike event
     * Rational -> Cautious
     * Cautious -> Unstable
     * 
     * @param player The player whose state will be updated
     * @param fearIncrease The amount of fear that was added
     */
    public void handleFearSpike(Player player, int fearIncrease) {
        if (fearIncrease >= FEAR_SPIKE_THRESHOLD) {
            switch (player.getBehaviorType()) {
                case RATIONAL:
                    player.setBehaviorType(BehaviorType.CAUTIOUS);
                    System.out.println(player.getName() + " becomes CAUTIOUS due to fear spike");
                    break;
                case CAUTIOUS:
                    player.setBehaviorType(BehaviorType.UNSTABLE);
                    System.out.println(player.getName() + " becomes UNSTABLE due to fear spike");
                    break;
                default:
                    // No change for other states
                    break;
            }
        }
    }
    
    /**
     * Updates a player's behavior state based on a betrayal event
     * Rational -> Aggressive
     * Cautious -> Aggressive
     * 
     * @param player The player whose state will be updated
     * @param betrayer The player who committed the betrayal
     */
    public void handleBetrayal(Player player, Player betrayer) {
        // Significantly reduce trust
        player.modifyTrust(betrayer, -8);
        
        switch (player.getBehaviorType()) {
            case RATIONAL:
            case CAUTIOUS:
                player.setBehaviorType(BehaviorType.AGGRESSIVE);
                System.out.println(player.getName() + " becomes AGGRESSIVE due to betrayal by " + betrayer.getName());
                break;
            default:
                // No change for other states
                break;
        }
    }
    
    /**
     * Updates an aggressive player's state based on stamina and aggression duration
     * Aggressive -> Unstable (if prolonged aggression with low stamina)
     * 
     * @param player The player whose state will be updated
     * @param stamina The current physical stamina level
     * @param aggressionTurns How many turns the player has been aggressive
     */
    public void handleProlongedAggression(Player player, int stamina, int aggressionTurns) {
        if (player.getBehaviorType() == BehaviorType.AGGRESSIVE) {
            if (stamina <= LOW_STAMINA_THRESHOLD && aggressionTurns >= 3) {
                player.setBehaviorType(BehaviorType.UNSTABLE);
                System.out.println(player.getName() + " becomes UNSTABLE due to prolonged aggression with low stamina");
            }
        }
    }
    
    /**
     * Updates a player's behavior state based on rest or trust-building events
     * Aggressive -> Rational
     * Cautious -> Rational
     * Unstable -> Cautious
     * 
     * @param player The player whose state will be updated
     * @param trustLevel The accumulated trust level from the event
     */
    public void handleRestOrTrustBuilding(Player player, int trustLevel) {
        if (trustLevel >= TRUST_BUILDING_THRESHOLD) {
            switch (player.getBehaviorType()) {
                case AGGRESSIVE:
                case CAUTIOUS:
                    player.setBehaviorType(BehaviorType.RATIONAL);
                    System.out.println(player.getName() + " becomes RATIONAL due to rest/trust building");
                    break;
                case UNSTABLE:
                    player.setBehaviorType(BehaviorType.CAUTIOUS);
                    System.out.println(player.getName() + " becomes CAUTIOUS due to rest/trust building");
                    break;
                default:
                    // No change for RATIONAL state
                    break;
            }
        }
    }
}
