package juvinne;
 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.ParseException;
import java.util.List;
 
/** Swing GUI: create, view, search, update, remove (with undo) and battle turns. */
public class GameFrame extends JFrame {
    private static final Color BG = new Color(192,128,129);
    private static final Color PANEL = new Color(145,95,109);
    private static final Color FIELD = new Color(153,102,102);
    private static final Color TEXT = new Color(255,228,225);
    private static final Color ACCENT = new Color(159,29,53);
 
    private final CharacterManager manager = new CharacterManager();
    private final DefaultListModel<GameCharacter> listModel = new DefaultListModel<>();
    private final JList<GameCharacter> list = new JList<>(listModel);
    private final AvatarPanel avatar = new AvatarPanel();
 
    private final JTextField searchField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JComboBox<String> typeBox = new JComboBox<>(CharacterManager.TYPES);
    private final JSpinner healthSpinner =
            new JSpinner(new SpinnerNumberModel(100, 1, GameCharacter.MAX_HEALTH, 1));
    private final JSpinner powerSpinner =
            new JSpinner(new SpinnerNumberModel(30, 1, GameCharacter.MAX_POWER, 1));
    private final JTextArea log = new JTextArea(6, 40);
    private final JLabel queueLabel = new JLabel(" ");
    private JButton undoButton;
 
    private boolean refreshing = false;
    private GameCharacter lastShown = null;
 
    public GameFrame() {
        super("Juvinne - Game Character Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
 
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        root.add(buildLeft(), BorderLayout.WEST);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildForm(), BorderLayout.EAST);
        root.add(buildLog(), BorderLayout.SOUTH);
        setContentPane(root);
 
        setSize(1080, 720);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
 
        manager.seedDefaults();
        refreshList();
        if (!listModel.isEmpty()) list.setSelectedIndex(0);
        log("Welcome! Create, search, update or remove characters, then battle with the turn queue.");
    }
 
    // ------------------------------------------------------------ builders
    private JPanel buildLeft() {
        JPanel p = darkPanel(new BorderLayout(6, 6));
        p.setPreferredSize(new Dimension(240, 0));
        p.setBorder(titled("Roster"));
 
        JPanel searchRow = new JPanel(new BorderLayout(4, 0));
        searchRow.setOpaque(false);
        styleField(searchField);
        searchField.setToolTipText("Search by name or type");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshList(); }
            public void removeUpdate(DocumentEvent e) { refreshList(); }
            public void changedUpdate(DocumentEvent e) { refreshList(); }
        });
        JButton clear = button("X", new Color(90, 95, 120), e -> searchField.setText(""));
        searchRow.add(new JLabel("\uD83D\uDD0D") {{ setForeground(TEXT); }}, BorderLayout.WEST);
        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(clear, BorderLayout.EAST);
        p.add(searchRow, BorderLayout.NORTH);
 
        list.setBackground(FIELD);
        list.setForeground(TEXT);
        list.setFixedCellHeight(34);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object value, int index,
                                                          boolean selected, boolean focus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, value, index, selected, focus);
                GameCharacter c = (GameCharacter) value;
                lbl.setBorder(new EmptyBorder(0, 10, 0, 6));
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setForeground(selected ? Color.WHITE : c.getThemeColor().brighter());
                lbl.setBackground(selected ? c.getThemeColor().darker() : FIELD);
                return lbl;
            }
        });
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !refreshing) onSelect();
        });
        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(BorderFactory.createEmptyBorder());
        p.add(sp, BorderLayout.CENTER);
        return p;
    }
 
    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setOpaque(false);
        p.add(avatar, BorderLayout.CENTER);
 
        JPanel south = new JPanel(new BorderLayout(6, 8));
        south.setOpaque(false);
        queueLabel.setForeground(ACCENT);
        queueLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        queueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        south.add(queueLabel, BorderLayout.NORTH);
 
        JPanel actions = new JPanel(new GridLayout(1, 3, 8, 0));
        actions.setOpaque(false);
        actions.add(button("Attack", new Color(231, 76, 60), e -> act(false)));
        actions.add(button("Special Move", new Color(155, 89, 182), e -> act(true)));
        actions.add(button("Next Turn \u25B6", new Color(230, 126, 34), e -> nextTurn()));
        south.add(actions, BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }
 
    private JPanel buildForm() {
        JPanel p = darkPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(250, 0));
        p.setBorder(titled("Character Editor"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridx = 0;
 
        styleField(nameField);
        typeBox.setBackground(FIELD);
        typeBox.setForeground(TEXT);
        styleSpinner(healthSpinner);
        styleSpinner(powerSpinner);
 
        int row = 0;
        row = addRow(p, gc, row, "Name (letters, numbers, max 20)", nameField);
        row = addRow(p, gc, row, "Type (used when adding)", typeBox);
        row = addRow(p, gc, row, "Health (1-" + GameCharacter.MAX_HEALTH + ")", healthSpinner);
        row = addRow(p, gc, row, "Power (1-" + GameCharacter.MAX_POWER + ")", powerSpinner);
 
        gc.gridy = row++;
        gc.insets = new Insets(14, 6, 4, 6);
        p.add(button("Add New Character", new Color(46, 204, 113), e -> addCharacter()), gc);
        gc.insets = new Insets(4, 6, 4, 6);
        gc.gridy = row++;
        p.add(button("Update Selected", new Color(52, 152, 219), e -> updateCharacter()), gc);
        gc.gridy = row++;
        p.add(button("Remove Selected", new Color(231, 76, 60), e -> removeCharacter()), gc);
        undoButton = button("Undo Remove", new Color(127, 140, 141), e -> undoRemove());
        gc.gridy = row++;
        p.add(undoButton, gc);
 
        gc.gridy = row;
        gc.weighty = 1;
        p.add(Box.createVerticalGlue(), gc);
        return p;
    }
 
    private JScrollPane buildLog() {
        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setBackground(PANEL);
        log.setForeground(new Color(160, 235, 180));
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(log);
        sp.setBorder(titled("Battle Log"));
        sp.setPreferredSize(new Dimension(0, 140));
        sp.getViewport().setBackground(PANEL);
        return sp;
    }
 
    // ------------------------------------------------------------ actions
    private void addCharacter() {
        String name = validName(null);
        if (name == null) return;
        int health = readSpinner(healthSpinner, "Health");
        int power = readSpinner(powerSpinner, "Power");
        if (health < 0 || power < 0) return;
 
        GameCharacter c = manager.create((String) typeBox.getSelectedItem(), name, health, power);
        manager.add(c);
        searchField.setText("");
        refreshList();
        list.setSelectedValue(c, true);
        log("Created " + c.showName() + " (HP " + health + ", PWR " + power + ").");
    }
 
    private void updateCharacter() {
        GameCharacter c = list.getSelectedValue();
        if (c == null) { error("Select a character in the roster first."); return; }
        String name = validName(c);
        if (name == null) return;
        int health = readSpinner(healthSpinner, "Health");
        int power = readSpinner(powerSpinner, "Power");
        if (health < 0 || power < 0) return;
 
        String old = c.getName();
        manager.update(c, name, health, power);
        refreshList();
        fillForm(c);
        log("Updated " + old + " -> " + c.showName() + " (HP " + health + ", PWR " + power + ").");
    }
 
    private void removeCharacter() {
        GameCharacter c = list.getSelectedValue();
        if (c == null) { error("Select a character in the roster first."); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Remove " + c.getName() + "?\n(You can undo this.)",
                "Confirm removal", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        manager.remove(c);
        lastShown = null;
        refreshList();
        log("Removed " + c.getName() + " (moved to recycle stack).");
    }
 
    private void undoRemove() {
        GameCharacter c = manager.undoRemove();
        if (c == null) { error("Nothing to restore."); return; }
        searchField.setText("");
        refreshList();
        list.setSelectedValue(c, true);
        log("Restored " + c.showName() + ".");
    }
 
    private void act(boolean special) {
        GameCharacter c = list.getSelectedValue();
        if (c == null) { error("Select a character first."); return; }
        perform(c, special);
    }
 
    private void perform(GameCharacter c, boolean special) {
        log(special ? c.specialMove() : c.attack());
        avatar.pulse();
        fillForm(c);       // health may have changed (Healer)
        list.repaint();
        avatar.setCharacter(c);
    }
 
    private void nextTurn() {
        GameCharacter c = manager.nextTurn();
        if (c == null) { error("The turn queue is empty. Add a character first."); return; }
        log("--- " + c.getName() + "'s turn ---");
        if (listModel.contains(c)) list.setSelectedValue(c, true);
        boolean special = randomBoolean();
        perform(c, special);
        updateQueueLabel();
    }
 
    private boolean randomBoolean() { return Math.random() < 0.4; }
 
    // ------------------------------------------------------------ validation
    private String validName(GameCharacter ignore) {
        String name = nameField.getText().trim().replaceAll("\\s+", " ");
        if (name.isEmpty()) { error("Name cannot be empty."); return null; }
        if (name.length() > 20) { error("Name must be 20 characters or fewer."); return null; }
        if (!name.matches("[A-Za-z0-9 _-]+")) {
            error("Name may only contain letters, numbers, spaces, '-' and '_'.");
            return null;
        }
        GameCharacter existing = manager.findByName(name);
        if (existing != null && existing != ignore) {
            error("A character named \"" + existing.getName() + "\" already exists.");
            return null;
        }
        return name;
    }
 
    private int readSpinner(JSpinner spinner, String label) {
        try {
            spinner.commitEdit();
        } catch (ParseException e) {
            error(label + " must be a whole number within its allowed range.");
            return -1;
        }
        return (Integer) spinner.getValue();
    }
 
    // ------------------------------------------------------------ helpers
    private void refreshList() {
        GameCharacter keep = list.getSelectedValue();
        refreshing = true;
        listModel.clear();
        for (GameCharacter c : manager.search(searchField.getText())) listModel.addElement(c);
        if (keep != null && listModel.contains(keep)) list.setSelectedValue(keep, true);
        refreshing = false;
        onSelect();
        updateQueueLabel();
        if (undoButton != null) {
            undoButton.setText("Undo Remove (" + manager.removedCount() + ")");
            undoButton.setEnabled(manager.removedCount() > 0);
        }
    }
 
    private void onSelect() {
        GameCharacter c = list.getSelectedValue();
        avatar.setCharacter(c);
        if (c != null && c != lastShown) fillForm(c);
        lastShown = c;
    }
 
    private void fillForm(GameCharacter c) {
        nameField.setText(c.getName());
        typeBox.setSelectedItem(c.getRole());
        healthSpinner.setValue(c.getHealth());
        powerSpinner.setValue(c.getPower());
    }
 
    private void updateQueueLabel() {
        StringBuilder sb = new StringBuilder("Turn queue:  ");
        List<GameCharacter> order = manager.getTurnOrder();
        if (order.isEmpty()) sb.append("(empty)");
        for (int i = 0; i < order.size(); i++) {
            if (i > 0) sb.append("  \u2192  ");
            sb.append(order.get(i).getName());
        }
        queueLabel.setText(sb.toString());
    }
 
    private void log(String msg) {
        log.append(msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
 
    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }
 
    private int addRow(JPanel p, GridBagConstraints gc, int row, String label, JComponent field) {
        JLabel l = new JLabel(label);
        l.setForeground(new Color(170, 175, 200));
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        gc.gridy = row++;
        p.add(l, gc);
        gc.gridy = row++;
        p.add(field, gc);
        return row;
    }
 
    private JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(PANEL);
        return p;
    }
 
    private TitledBorder titled(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 75, 100)), " " + title + " ");
        tb.setTitleColor(ACCENT);
        tb.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        return tb;
    }
 
    private void styleField(JTextField f) {
        f.setBackground(FIELD);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 75, 100)), new EmptyBorder(4, 6, 4, 6)));
    }
 
    private void styleSpinner(JSpinner s) {
        JTextField tf = ((JSpinner.DefaultEditor) s.getEditor()).getTextField();
        tf.setBackground(FIELD);
        tf.setForeground(TEXT);
        tf.setCaretColor(TEXT);
    }
 
    private JButton button(String text, Color color, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(9, 12, 9, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }
}
 