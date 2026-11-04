package juvinne;

class Mage extends GameCharacter {
    public Mage(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return name + " casts a fireball!";
    }

    @Override
    public String specialMove() {
        return name + " uses Lightning Storm!";
    }

    @Override
    public String getType() {
        return "Mage (Urban Attacker)";
    }
}