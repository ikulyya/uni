/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Borderland2;

/**
 *
 * @author Ilkin
 */
public enum Hazard {
    FOG(1),
    FIRE(2),
    BUILDING_COLLAPSE(3),
    MIST(1);

    private final int fearIncrease;

    /**
     * Creates a new hazard with specified fear increase.
     * 
     * @param fearIncrease The amount of fear added when hazard activates
     */
    Hazard(int fearIncrease) {
        this.fearIncrease = fearIncrease;
    }

    /**
     * Gets the amount of fear this hazard adds when activated.
     * 
     * @return Fear increase amount
     */
    public int getFearIncrease() {
        return fearIncrease;
    }
}
