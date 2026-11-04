package juvinne;

class Warrior extends GameCharacter {
    public Warrior(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return name + " swings a mighty sword!";
    }

    @Override
    public String specialMove() {
        return name + " uses Shield Bash!";
    }

    @Override
    public String getType() {
        return "Warrior (Ghost)";
    }
}