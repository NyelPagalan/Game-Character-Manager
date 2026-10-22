package juvinne;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
 
/**
 * ABSTRACT DATA TYPES used to manage the characters:
 *  - List  (ArrayList)  : the roster, supports view / search / update
 *  - Stack              : recycle bin, last removed character is restored first (undo)
 *  - Queue (LinkedList) : turn order, first in line acts, then goes to the back
 */
public class CharacterManager {
    public static final String[] TYPES = {"Attacker", "Defender", "Assassin", "Healer"};
 
    private final List<GameCharacter> roster = new ArrayList<>();
    private final Stack<GameCharacter> recycleBin = new Stack<>();
    private final Queue<GameCharacter> turnQueue = new LinkedList<>();
 
    public GameCharacter create(String type, String name, int health, int power) {
        switch (type) {
            case "Attacker": return new Attacker(name, health, power);
            case "Defender": return new Defender(name, health, power);
            case "Assassin": return new Assassin(name, health, power);
            case "Healer":   return new Healer(name, health, power);
            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
 
    public void seedDefaults() {
        add(create("Attacker", "Ghost", 120, 40));
        add(create("Defender", "Urban Tracker", 180, 25));
        add(create("Assassin", "Templar", 100, 45));
        add(create("Healer", "Medic", 110, 30));
    }
 
    public void add(GameCharacter c) {
        roster.add(c);
        turnQueue.add(c);
    }
 
    public GameCharacter findByName(String name) {
        for (GameCharacter c : roster) {
            if (c.getName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }
 
    /** Case-insensitive search by name or character type. Empty query returns all. */
    public List<GameCharacter> search(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<GameCharacter> result = new ArrayList<>();
        for (GameCharacter c : roster) {
            if (q.isEmpty() || c.getName().toLowerCase().contains(q)
                    || c.getRole().toLowerCase().contains(q)) {
                result.add(c);
            }
        }
        return result;
    }
 
    public void update(GameCharacter c, String name, int health, int power) {
        c.setName(name);
        c.setHealth(health);
        c.setPower(power);
    }
 
    public void remove(GameCharacter c) {
        if (roster.remove(c)) {
            turnQueue.remove(c);
            recycleBin.push(c);
        }
    }
 
    /** Restores the most recently removed character, or returns null if none. */
    public GameCharacter undoRemove() {
        if (recycleBin.isEmpty()) return null;
        GameCharacter c = recycleBin.pop();
        String base = c.getName();
        int n = 2;
        while (findByName(c.getName()) != null) {
            c.setName(base + " (" + n++ + ")");
        }
        add(c);
        return c;
    }
 
    public int removedCount() { return recycleBin.size(); }
 
    /** Next in line acts, then goes to the back of the queue. */
    public GameCharacter nextTurn() {
        GameCharacter c = turnQueue.poll();
        if (c != null) turnQueue.add(c);
        return c;
    }
 
    public List<GameCharacter> getTurnOrder() { return new ArrayList<>(turnQueue); }
}
 