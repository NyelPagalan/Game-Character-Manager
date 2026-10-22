package juvinne;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
 
public class Attacker extends GameCharacter {
    public Attacker(String name, int health, int power) { super(name, health, power); }
 
    @Override public String getRole() { return "Attacker"; }
    @Override public String getAbilityName() { return "Barrage"; }
    @Override public String getDescription() { return "High damage, fires rapid volleys."; }
    @Override public Color getThemeColor() { return new Color(231, 76, 60); }
 
    @Override
    public String attack() {
        int dmg = getPower() + RNG.nextInt(getPower() / 4 + 1);
        return getName() + " fires a plasma burst for " + dmg + " damage!";
    }
 
    @Override
    public String specialMove() {
        int total = 0;
        for (int i = 0; i < 3; i++) total += getPower() / 2 + RNG.nextInt(getPower() / 6 + 1);
        return getName() + " unleashes BARRAGE: 3 rapid shots for " + total + " total damage!";
    }
 
    @Override
    public void drawEmblem(Graphics2D g, int cx, int cy, int size) {
        int r = (int) (size * 0.55);
        g.setStroke(new BasicStroke(5f));
        g.drawOval(cx - r, cy - r, r * 2, r * 2);
        g.drawLine(cx - r - 10, cy, cx + r + 10, cy);
        g.drawLine(cx, cy - r - 10, cx, cy + r + 10);
        g.fillOval(cx - 5, cy - 5, 10, 10);
    }
}
 