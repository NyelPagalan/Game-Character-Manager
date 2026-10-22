package juvinne;
 
import java.awt.Color;
import java.awt.Graphics2D;
 
public class Healer extends GameCharacter {
    public Healer(String name, int health, int power) { super(name, health, power); }
 
    @Override public String getRole() { return "Healer"; }
    @Override public String getAbilityName() { return "Renew"; }
    @Override public String getDescription() { return "Weak attacks, but can restore its own health."; }
    @Override public Color getThemeColor() { return new Color(46, 204, 113); }
 
    @Override
    public String attack() {
        int dmg = getPower() / 3 + RNG.nextInt(getPower() / 6 + 1);
        return getName() + " zaps with a light beam for " + dmg + " damage.";
    }
 
    @Override
    public String specialMove() {
        int before = getHealth();
        setHealth(before + getPower());
        int healed = getHealth() - before;
        return getName() + " casts RENEW, restoring " + healed + " HP (now "
                + getHealth() + "/" + MAX_HEALTH + ")!";
    }
 
    @Override
    public void drawEmblem(Graphics2D g, int cx, int cy, int size) {
        int r = (int) (size * 0.55);
        g.fillRoundRect(cx - r / 5, cy - r, r * 2 / 5, r * 2, 8, 8);
        g.fillRoundRect(cx - r, cy - r / 5, r * 2, r * 2 / 5, 8, 8);
    }
}