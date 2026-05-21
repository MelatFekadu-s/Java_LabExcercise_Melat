# Projects (JavaFX)

This repository contains three JavaFX applications: **ChatBox**, **StudentNotepadFX**, and **PokerGameFX**.  
Each project demonstrates different aspects of software engineering: networking, GUI design, file I/O, and object‑oriented programming.

---

## 💬 ChatBox (ChatClientFX + ChatServer)

### Overview
A client‑server chat application with JavaFX GUI for real‑time text and photo sharing.

### Objectives
- Enable multiple clients to connect and chat simultaneously.
- Support sending and receiving photos.
- Provide a user‑friendly chat interface.

### Significance
Demonstrates networking, concurrency, and GUI integration — essential skills for software engineering students.

### High‑Level Explanation
The server listens on port `12823` and handles multiple clients using threads.  
Clients connect via sockets, send text or photos, and display them in the GUI.

### Scope
Supports local network chat with text and photo sharing.  
Does not include encryption or advanced features like emojis.

### Key Features
- Multi‑client support.
- JavaFX GUI for chat window.
- Photo sending and inline display.
- Server broadcasts messages to all clients.

### Conclusion
A functional chat system showcasing networking and GUI programming, bridging theory with practical application.

---

## 📝 StudentNotepadFX

### Overview
A JavaFX‑based notepad application designed for students to write, save, and search notes efficiently.

### Objectives
- Provide a simple text editor with file open/save functionality.
- Enable search with highlighted preview results.
- Support keyboard shortcuts for quick actions.

### Significance
Helps students manage notes in a lightweight, user‑friendly tool without needing heavy word processors.

### High‑Level Explanation
Uses JavaFX layouts (`BorderPane`, `VBox`) for structure, `TextArea` for editing, and regex search for highlighting.

### Scope
Focused on basic note‑taking and search.  
Does not include advanced formatting or cloud sync.

### Key Features
- File open/save with error handling.
- Search bar with green “Search” button.
- Highlight preview window showing matches in yellow.
- Menu items styled with colors (blue, green, red).

### Conclusion
A practical, easy‑to‑use notepad tailored for student needs, demonstrating JavaFX GUI and file I/O concepts.

---

## 🎲 PokerGameFX

### Overview
A simplified poker game built with JavaFX where two players compete based on the highest card.

### Objectives
- Simulate card dealing and comparison.
- Track scores across multiple rounds.
- Provide clear rules and reset functionality.

### Significance
Introduces students to object‑oriented programming (Card, Deck) and GUI design while keeping gameplay simple.

### High‑Level Explanation
Deck is shuffled and cards dealt to each player.  
Highest ranked card determines the winner.  
GUI displays hands, results, and scores.

### Scope
Simplified poker rules (highest card wins).  
Does not include full poker hand rankings.

### Key Features
- Deal Cards (green button).
- New Game (blue button).
- Rules popup (orange button).
- Exit (red button).
- Score tracking across rounds.

### Conclusion
An engaging way to learn OOP, collections, and JavaFX styling while creating a fun, interactive game.

---


