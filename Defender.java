package juvinne;
 
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
 
public class Defender extends GameCharacter {
    public Defender(String name, int health, int power) { super(name, health, power); }
 
    @Override public String getRole() { return "Defender"; }
    @Override public String getAbilityName() { return "Fortress"; }
    @Override public String getDescription() { return "Tough protector, raises damage-absorbing shields."; }
    @Override public Color getThemeColor() { return new Color(52, 152, 219); }
 
    @Override
    public String attack() {
        int dmg = getPower() / 2 + RNG.nextInt(getPower() / 5 + 1);
        return getName() + " bashes with a shield for " + dmg + " damage!";
    }
 
    @Override
    public String specialMove() {
        return getName() + " raises FORTRESS, absorbing up to " + (getPower() * 2) + " incoming damage!";
    }
 
    @Override
    public void drawEmblem(Graphics2D g, int cx, int cy, int size) {
        int r = (int) (size * 0.55);
        Polygon shield = new Polygon(
                new int[]{cx - r, cx + r, cx + r, cx, cx - r},
                new int[]{cy - r, cy - r, cy, cy + r + 12, cy}, 5);
        g.fillPolygon(shield);
        int s = (int) (r * 0.6);
        Polygon inner = new Polygon(
                new int[]{cx - s, cx + s, cx + s, cx, cx - s},
                new int[]{cy - s, cy - s, cy - 2, cy + s + 6, cy - 2}, 5);
        g.setColor(getThemeColor().darker());
        g.fillPolygon(inner);
    }
}
 