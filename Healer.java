package juvinne;
 
class Healer extends GameCharacter {
    public Healer(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return name + " strikes with a holy staff!";
    }

    @Override
    public String specialMove() {
        return name + " uses Healing Light and restores the team's health!";
    }

    @Override
    public String getType() {
        return "Healer (Medic)";
    }
}
