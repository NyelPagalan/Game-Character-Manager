package juvinne;
 
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
 
public class Assassin extends GameCharacter {
    public Assassin(String name, int health, int power) { super(name, health, power); }
 
    @Override public String getRole() { return "Assassin"; }
    @Override public String getAbilityName() { return "Shadow Strike"; }
    @Override public String getDescription() { return "Fast and deadly, 30% chance of triple-damage critical hits."; }
    @Override public Color getThemeColor() { return new Color(155, 89, 182); }
 
    @Override
    public String attack() {
        boolean crit = RNG.nextInt(100) < 30;
        int dmg = getPower() + RNG.nextInt(getPower() / 5 + 1);
        if (crit) {
            return getName() + " lands a CRITICAL stab for " + (dmg * 3) + " damage!";
        }
        return getName() + " slashes from the shadows for " + dmg + " damage.";
    }
 
    @Override
    public String specialMove() {
        return getName() + " uses SHADOW STRIKE, ignoring armor for " + (int) (getPower() * 2.5) + " damage!";
    }
 
    @Override
    public void drawEmblem(Graphics2D g, int cx, int cy, int size) {
        int r = (int) (size * 0.55);
        Polygon blade = new Polygon(
                new int[]{cx, cx - r / 3, cx + r / 3},
                new int[]{cy - r - 10, cy + r / 4, cy + r / 4}, 3);
        g.fillPolygon(blade);
        g.fillRect(cx - r / 2, cy + r / 4, r, 8);
        g.fillRect(cx - 5, cy + r / 4 + 8, 10, r / 2);
    }
}
 