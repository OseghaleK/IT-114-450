## Project Name: Sketch Guesss
### Project Summary: (Here's your proposal converted to regular headers and bullet points:

Project Description
- Real-time multiplayer drawing and guessing game
- One player draws a hidden word while others guess
- Features:
  * Synchronized drawing canvas
  * Score tracking
  * Turn-based rounds

Key Features
- Real-Time Drawing Sync
  * Uses CoordPayload and DRAW_SYNC to update all clients
- Turn System
  * RoundStartPayload assigns drawer role
  * RoundEndPayload reveals the word
- Scoreboard
  * PointsPayload tracks scores in order
- Room Management
  * /createroom and /joinroom commands
- CLI Interface
  * Text-based controls:
    - /draw x,y for drawing
    - /guess word for guessing

Milestone Submissions
- Milestone 1: Basic Networking & Chat
  * Implemented Payload system
  * Added room management
- Milestone 2: Drawing & Guessing
  * Added canvas sync:
    - DimensionPayload
    - CoordPayload
- Milestone 3: Game Logic & Polish
  * Completed scoring system
  * Implemented round system
  * Added error handling

Technical Stack
- Language: Java
- Networking: Object streams over sockets
- Sync Protocol: Custom payload serialization
- Threading: Dedicated ClientReader thread for incoming data

Demo Highlights
- Three clients connecting to one room
- Real-time drawing synchronization
- Correct guess triggering score update
- Round transition with new drawer assignment

Challenges & Solutions
- Challenge: Thread-safe drawing updates
  * Solution: Synchronized board array in ClientMain
- Challenge: Network latency
  * Solution: Optimized payload size (simple CoordPayload)
- Challenge: State consistency
  * Solution: Server-authoritative design

Future Enhancements
- GUI interface to replace CLI
- Color selection for drawing
- Turn timer implementation
- Word difficulty levels system)
### Github Link: (main branch with your final project)
### Your Name: Oseghale Akimien
### Course/Section: It 114- 450
 
 
### Proposal Checklist and Evidence

- Milestone 1
  - (file:///C:/Users/17323/Downloads/oka_IT114-450-M2025_it114-milestone-1_07-24-2025_19-23-22.pdf)  
- Milestone 2
  - (file:///C:/Users/17323/Downloads/oka_IT114-450-M2025_it114-milestone-2-drawing_07-25-2025_23-10-55.pdf)
- Milestone 3
  - (file:///C:/Users/17323/Downloads/oka_IT114-450-M2025_it114-milestone-3-drawing_07-30-2025_17-52-55.pdf)
- Demo Link
  - (https://youtu.be/tom57IjMHDo) 
