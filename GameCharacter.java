package juvinne;
 
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
 
/**
 * ABSTRACTION: common state and behaviour for every character.
 * Subclasses must supply their own abilities, colours and emblem.
 */
public abstract class GameCharacter {
    public static final int MAX_HEALTH = 200;
    public static final int MAX_POWER = 100;
    protected static final Random RNG = new Random();
 
    private String name;
    private int health;
    private int power;
 
    protected GameCharacter(String name, int health, int power) {
        this.name = name;
        setHealth(health);
        setPower(power);
    }
 
    // ---- abstract behaviour (each type implements differently) ----
    public abstract String getRole();
    public abstract String getAbilityName();
    public abstract String getDescription();
    public abstract Color getThemeColor();
    public abstract String attack();
    public abstract String specialMove();
    /** Draws this character's emblem (polymorphic drawing). */
    public abstract void drawEmblem(Graphics2D g, int cx, int cy, int size);
 
    // ---- shared behaviour ----
    public String showName() {
        return getRole() + ": " + name;
    }
 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    public int getHealth() { return health; }
    public void setHealth(int health) {
        this.health = Math.max(1, Math.min(MAX_HEALTH, health));
    }
 
    public int getPower() { return power; }
    public void setPower(int power) {
        this.power = Math.max(1, Math.min(MAX_POWER, power));
    }
 
    @Override
    public String toString() {
        return name + "  [" + getRole() + "]";
    }
}
 