package juvinne;
 
class Assassin extends GameCharacter {
    public Assassin(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return name + " stabs from the shadows with a dagger!";
    }

    @Override
    public String specialMove() {
        return name + " uses Shadow Strike!";
    }

    @Override
    public String getType() {
        return "Assassin (Templar)";
    }
}
