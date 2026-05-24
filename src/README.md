# Graph-Based Maze Solver and Pattern Similarity Analyzer

Computer Science 3A Mini Project  
University of Johannesburg • 2026

---
## Presentation Link
https://youtu.be/8v4P92aousQ

---
## Dataset Link
https://www.kaggle.com/datasets/anthonytherrien/lost-in-minimazes?select=maze


## Team Members

| Student Number | Name |
|---|---|
| 224097183 | Dikgale Mmipe Branden |
| 221123990 | Siyanda Brian Nkosi |
| 221022135 | Mathiba Mantji Sarah |
| 222224016 | Siyabonga Gift Mzimela |

---

## Project Description

This project is a JavaFX desktop application that converts maze images into graph structures and applies graph algorithms to solve navigation problems.

The application supports:
- Maze solving using A* and Dijkstra’s algorithms
- Graph similarity analysis between maze patterns
- Interactive visualization of graph traversal and shortest paths
- Dynamic graph generation from uploaded maze images

The project demonstrates practical applications of graph theory in:
- Robotics
- GPS navigation
- Autonomous vehicles
- Emergency evacuation systems

---

## Folder Structure

- `src/` → Source code and README
- `dist/` → Executable runnable JAR file
- `ss/` → PowerPoint presentation slides

---

## Requirements

Before running the application, ensure the following are installed:

- Java 17 or later(javafx and jdk and sdk)
- Java added to the system environment variables (PATH)

---

## Setting Up Java Environment Variables (Windows)

### Step 1: Install Java

Install Java JDK 17 or later(javafx and jdk and sdk)

---

### Step 2: Locate Java Installation

Typical installation path:

```text
C:\Program Files\Java\jdk-17\bin (include javafx and jdk and sdk)

---

### Run command

```cmd
java -jar MazeSolver.jar
```

## How to Use 
1. **Upload a Maze** 
   - Click **"Upload Maze"** and select a maze image file. 
   - The image will be displayed with small green dots indicating valid path nodes. 
2. **Select Start and End Points** 
   - Click **"Select Start"**, then click on a green dot to set the start point. 
   - Click **"Select End"**, then click on a green dot to set the end point. 
   - Green and red markers will appear on the selected nodes. 
3. **Solve the Maze** 
   - Choose an algorithm from the dropdown (**Dijkstra** or **A***). 
   - Click **"Solve Maze"**. 
   - The shortest path will be drawn in blue, and statistics will appear in the text area. 
4. **Compare Similarity** 
   - Click **"Upload Comparison"** to load a second maze image. 
   - Click **"Check Similarity"** to compare the two maze structures. 
   - The similarity score and detailed metrics will be displayed. 
5. **Reset** 
   - Click **"Clear Points"** to remove start/end selections. 
   - Click **"Reset All"** to clear everything and start over.