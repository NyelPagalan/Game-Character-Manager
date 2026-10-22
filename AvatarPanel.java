package juvinne;
 
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
 
/** Animated character card: pulsing glow, animated health bar, themed colours. */
public class AvatarPanel extends JPanel {
    private GameCharacter character;
    private float pulse = 0f;
    private double phase = 0;
    private double shownHealth = 0;
 
    public AvatarPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(380, 340));
        new Timer(30, e -> {
            phase += 0.06;
            pulse = Math.max(0f, pulse - 0.035f);
            if (character != null) {
                shownHealth += (character.getHealth() - shownHealth) * 0.15;
            }
            repaint();
        }).start();
    }
 
    public void setCharacter(GameCharacter c) {
        if (c != character) {
            character = c;
            if (c != null) shownHealth = c.getHealth();
        }
        repaint();
    }
 
    public void pulse() { pulse = 1f; }
 
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
 
        Color theme = character == null ? new Color(90, 95, 120) : character.getThemeColor();
        g.setPaint(new GradientPaint(0, 0, new Color(30, 33, 50), 0, h,
                new Color(theme.getRed() / 4 + 20, theme.getGreen() / 4 + 20, theme.getBlue() / 4 + 25)));
        g.fill(new RoundRectangle2D.Float(0, 0, w, h, 26, 26));
        g.setColor(new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), 140));
        g.setStroke(new BasicStroke(2f));
        g.draw(new RoundRectangle2D.Float(1, 1, w - 3, h - 3, 26, 26));
 
        if (character == null) {
            g.setColor(new Color(170, 175, 200));
            g.setFont(new Font("SansSerif", Font.ITALIC, 16));
            String msg = "Select or create a character";
            g.drawString(msg, (w - g.getFontMetrics().stringWidth(msg)) / 2, h / 2);
            g.dispose();
            return;
        }
 
        int cx = w / 2, cy = 115;
        double radius = 62 + pulse * 14 + Math.sin(phase) * 3;
 
        // glow rings
        for (int i = 4; i >= 1; i--) {
            int rr = (int) (radius + i * 9 + pulse * 10);
            g.setColor(new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), 22 + (int) (pulse * 25)));
            g.fillOval(cx - rr, cy - rr, rr * 2, rr * 2);
        }
        g.setPaint(new GradientPaint(cx, (float) (cy - radius), theme.brighter(),
                cx, (float) (cy + radius), theme.darker().darker()));
        g.fillOval((int) (cx - radius), (int) (cy - radius), (int) (radius * 2), (int) (radius * 2));
 
        g.setColor(Color.WHITE);
        character.drawEmblem(g, cx, cy, (int) radius);
 
        // text
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        centered(g, character.getName(), w, 225);
        g.setColor(theme.brighter());
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        centered(g, character.getRole().toUpperCase() + "  \u2022  " + character.getAbilityName(), w, 247);
        g.setColor(new Color(190, 195, 215));
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        centered(g, character.getDescription(), w, 267);
 
        // health bar
        int bx = 30, by = 285, bw = w - 60, bh = 18;
        double ratio = Math.max(0, Math.min(1, shownHealth / GameCharacter.MAX_HEALTH));
        g.setColor(new Color(0, 0, 0, 110));
        g.fillRoundRect(bx, by, bw, bh, 12, 12);
        g.setColor(Color.getHSBColor((float) (ratio * 0.33), 0.75f, 0.95f));
        g.fillRoundRect(bx, by, (int) (bw * ratio), bh, 12, 12);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        centered(g, "HP " + character.getHealth() + "/" + GameCharacter.MAX_HEALTH
                + "     PWR " + character.getPower(), w, by + 14);
        g.dispose();
    }
 
    private void centered(Graphics2D g, String text, int w, int y) {
        g.drawString(text, (w - g.getFontMetrics().stringWidth(text)) / 2, y);
    }
}
 