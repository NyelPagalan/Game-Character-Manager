
# 🎮 Game Character Manager

A Java-based Game Character Manager that demonstrates Object-Oriented Programming (OOP) concepts and Abstract Data Types (ADT) through an interactive Graphical User Interface (GUI). Users can create, manage, and interact with different game characters.

## ✨ Features

- **Add Characters** – Create new game characters by entering a name and selecting a character type.
- **Remove Characters** – Select and remove existing characters.
- **Character Selection** – Choose different characters to perform actions.
- **Attack System** – Execute character-specific attacks.
- **Special Abilities** – Use unique special moves for each character.
- **Character Information** – Display character names and types.
- **Battle Log** – View character actions and activities.
- **Input Validation** – Prevent empty names, duplicate names, and invalid characters.

## 🧙 Character Classes

The application includes four character types:

| Character | Type | Description |
|---|---|---|
| Ghost | Warrior | A powerful fighter specializing in combat. |
| Urban Attacker | Mage | A magic-based character with special abilities. |
| Medic | Healer | A support character focused on healing. |
| Templar | Assassin | A stealth-based character specializing in attacks. |

## 🛠️ Technologies Used

- Java
- Java Swing (GUI)
- Java AWT
- ArrayList

## 📚 OOP Concepts Implemented

This project demonstrates the following programming concepts:

- **Abstraction** – Uses an abstract `GameCharacter` class to define common character behaviors.
- **Inheritance** – Character classes inherit properties and methods from `GameCharacter`.
- **Polymorphism** – Different character classes implement their own versions of attack and special move methods.
- **Encapsulation** – Organizes character data and methods within classes.
- **Abstract Data Type (ADT)** – Uses `ArrayList` to store and manage game characters dynamically.

## 🚀 How to Run

1. Clone or download this repository.
2. Open the project in your preferred Java IDE (Eclipse, IntelliJ IDEA, or NetBeans).
3. Make sure Java is installed.
4. Locate and open `Main.java`.
5. Run the program.
6. The Game Character Manager GUI will appear.

## 📂 Project Structure

```text
Game-Character-Manager/
│
├── Main.java
├── GameGUI.java
├── GameCharacter.java
├── Warrior.java
├── Mage.java
├── Healer.java
├── Assassin.java
└── README.md
```

## 🎯 Project Objective

The objective of this project is to apply fundamental Java programming concepts, particularly Object-Oriented Programming and Abstract Data Types, in developing an interactive character management application. It provides practical experience in class design, inheritance, polymorphism, data management, and GUI development.

**Developed as an academic Java programming project.**
