/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ilkin
 */
public class Player {
    private String name;
    private int mentals;
    private int physicals;
    private int fear;
    private boolean active;
    private List<Card> cards;
    private BehaviorType behaviorType;
    private Map<Player, Integer> trustRelations;
    
    
    public Player(String name, int mentals, int physicals){
        this.name = name;
        this.mentals =mentals;
        this.physicals = physicals;
        this.fear = 0;
        this.active = true;
        this.cards = new ArrayList<>();
        this.behaviorType = BehaviorType.RATIONAL; // Default behavior
        this.trustRelations = new HashMap<>();
    }
    
    public Player(String name, int mentals, int physicals, BehaviorType behaviorType){
        this(name, mentals, physicals);
        this.behaviorType = behaviorType;
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getMentals(){
        return this.mentals;
    }
    
    public int getPhysicals(){
        return this.physicals;
    }
    
    public int getFear(){
        return this.fear;
    }
    
    public boolean isActive(){
        return this.active;
    }
    
    /**
     * Get the behavior type of this player
     * 
     * @return The behavior type
     */
    public BehaviorType getBehaviorType() {
        return this.behaviorType;
    }
    
    /**
     * Set the behavior type of this player
     * 
     * @param behaviorType The new behavior type
     */
    public void setBehaviorType(BehaviorType behaviorType) {
        this.behaviorType = behaviorType;
    }
    
    /**
     * Sets the trust level towards another player
     * 
     * @param player The player to establish trust with
     * @param trustLevel The trust level (typically from -10 to 10, where negative is distrust)
     */
    public void setTrust(Player player, int trustLevel) {
        trustRelations.put(player, trustLevel);
    }
    
    /**
     * Gets the trust level towards another player
     * 
     * @param player The player to check trust with
     * @return The trust level, 0 if no relation exists
     */
    public int getTrust(Player player) {
        return trustRelations.getOrDefault(player, 0);
    }
    
    /**
     * Modifies the trust level towards another player
     * 
     * @param player The player to modify trust with
     * @param amount The amount to modify by (positive or negative)
     */
    public void modifyTrust(Player player, int amount) {
        int currentTrust = getTrust(player);
        trustRelations.put(player, currentTrust + amount);
    }
    
    /**
     * Adds a card to the player's collection.
     * 
     * @param card The card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    public int getTotalCards() {
        return cards.size();
    }
    
    /**
     * used whenver fear of player should be increased
     * @param amount of fear that shouldbe increased
     */
    public void increaseFear(int amount){
        this.fear += amount;
        if (this.fear >= 10){
            this.active = false;
        }
    }
    
    /**
     * used whenver fear of player should be decreased
     * @param amount of fear that shouldbe decreased
     */
    public void decreaseFear(int amount){
        this.fear -= amount;
        if (this.fear < 0) {
            this.fear = 0;
        }
    }
    
    /**
     * checks if player has all cards to win the game
     * @return whatever they has all cards(true) or no(false)
     */
    public boolean hasAllCards(){
        return cards.size()>= 52;
    }
    
    /**
     * Get all trust relationships for this player
     * 
     * @return Map of player->trust level
     */
    public Map<Player, Integer> getAllTrustRelations() {
        return new HashMap<>(trustRelations);
    }
    
    @Override
    public String toString() {
        return String.format("%s (Mental: %d, Physical: %d, Fear: %d, Behavior: %s, %s, Cards: %d)",
                name, mentals, physicals, fear, behaviorType.toString(), active ? "Active" : "Inactive", cards.size());
    }
}