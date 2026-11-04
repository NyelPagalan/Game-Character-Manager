package juvinne;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
 
public class GameGUI extends JFrame {
 
    // ADT: an ArrayList (list) stores all the game characters
    private ArrayList<GameCharacter> characters = new ArrayList<>();
 
    // GUI components
    private JTextField nameField = new JTextField(12);
    private JComboBox<String> typeBox = new JComboBox<>(new String[]{
            "Warrior (Ghost)", "Mage (Urban Attacker)", "Healer (Medic)", "Assassin (Templar)"});
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> characterList = new JList<>(listModel);
    private JTextArea output = new JTextArea();
 
    // Colors / theme
    private Color darkBlue = new Color(25, 25, 50);
    private Color gold = new Color(245, 196, 81);
 
    public GameGUI() {
        setTitle("Game Character Arena");
        setSize(1300, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(darkBlue);
 
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
 
        // starting characters from the original program
        addToList(new Warrior("Ghost"));
        addToList(new Mage("Urban Attacker"));
        addToList(new Healer("Medic"));
        addToList(new Assassin("Templar"));
    }
 
    // ---------- TOP: user input (name + type) ----------
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(60, 40, 110));
 
        JLabel title = new JLabel("GAME CHARACTER ARENA   ");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(gold);
 
        JButton addButton = makeButton("Add Character", new Color(61, 139, 255));
        addButton.addActionListener(e -> addCharacter());
 
        JButton removeButton = makeButton("Remove Selected", new Color(190, 60, 70));
        removeButton.addActionListener(e -> removeCharacter());
 
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setForeground(Color.WHITE);
 
        panel.add(title);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(typeLabel);
        panel.add(typeBox);
        panel.add(addButton);
        panel.add(removeButton);
        return panel;
    }
 
    // ---------- CENTER: character list + output ----------
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBackground(darkBlue);
 
        characterList.setBackground(new Color(40, 40, 75));
        characterList.setForeground(Color.WHITE);
        characterList.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JScrollPane listScroll = new JScrollPane(characterList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(gold), "Characters", 0, 0, null, gold));
 
        output.setEditable(false);
        output.setBackground(new Color(15, 15, 30));
        output.setForeground(new Color(120, 255, 160));
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(gold), "Battle Log", 0, 0, null, gold));
 
        panel.add(listScroll);
        panel.add(outputScroll);
        return panel;
    }
 
    // ---------- BOTTOM: action buttons ----------
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(60, 40, 110));
 
        JButton nameButton = makeButton("Show Name", new Color(90, 90, 130));
        nameButton.addActionListener(e -> {
            GameCharacter c = getSelectedCharacter();
            if (c != null) output.append(c.showName() + "\n");
        });
 
        JButton attackButton = makeButton("Attack", new Color(217, 69, 61));
        attackButton.addActionListener(e -> {
            GameCharacter c = getSelectedCharacter();
            if (c != null) output.append(c.attack() + "\n");
        });
 
        JButton specialButton = makeButton("Special Move", new Color(142, 77, 255));
        specialButton.addActionListener(e -> {
            GameCharacter c = getSelectedCharacter();
            if (c != null) output.append(c.specialMove() + "\n");
        });
 
        JButton clearButton = makeButton("Clear Log", new Color(90, 90, 130));
        clearButton.addActionListener(e -> output.setText(""));
 
        panel.add(nameButton);
        panel.add(attackButton);
        panel.add(specialButton);
        panel.add(clearButton);
        return panel;
    }
 
    // ---------- Logic ----------
    private void addCharacter() {
        String name = nameField.getText().trim();
 
        // Input validation
        if (name.isEmpty()) {
            showError("Please enter a name.");
            return;
        }
        if (name.length() > 12) {
            showError("Name must be 12 characters or fewer.");
            return;
        }
        if (!name.matches("[A-Za-z0-9 ]+")) {
            showError("Name can only contain letters, numbers, and spaces.");
            return;
        }
        for (GameCharacter c : characters) {
            if (c.name.equalsIgnoreCase(name)) {
                showError("That name is already taken.");
                return;
            }
        }
 
        // Create the chosen type (polymorphism: all stored as GameCharacter)
        GameCharacter newChar;
        int choice = typeBox.getSelectedIndex();
        if (choice == 0) {
            newChar = new Warrior(name);
        } else if (choice == 1) {
            newChar = new Mage(name);
        } else if (choice == 2) {
            newChar = new Healer(name);
        } else {
            newChar = new Assassin(name);
        }
 
        addToList(newChar);
        nameField.setText("");
    }
 
    private void addToList(GameCharacter c) {
        characters.add(c);
        listModel.addElement(c.name + " - " + c.getType());
    }
 
    private void removeCharacter() {
        int index = characterList.getSelectedIndex();
        if (index == -1) {
            showError("Please select a character from the list first.");
            return;
        }
        characters.remove(index);
        listModel.remove(index);
    }
 
    private GameCharacter getSelectedCharacter() {
        int index = characterList.getSelectedIndex();
        if (index == -1) {
            showError("Please select a character from the list first.");
            return null;
        }
        return characters.get(index);
    }
 
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }
 
    private JButton makeButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }
}
 