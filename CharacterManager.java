package juvinne;
//ABSTRACTION: abstract class with abstract methods every character must have
abstract class GameCharacter {
 String name;

 public GameCharacter(String name) {
     this.name = name;
 }

 // Abstract methods (must be implemented by subclasses)
 public abstract String attack();
 public abstract String specialMove();
 public abstract String getType();

 // Concrete method
 public String showName() {
     return "Character: " + name + " (" + getType() + ")";
 }
}
