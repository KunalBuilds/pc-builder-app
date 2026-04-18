
# Benchmark Your Rig – AI Upgrade & Performance System

## Overview
This document defines the upgraded **Benchmark Your Rig** feature powered by AI.

The feature allows users to:
- Add their full PC configuration
- Get an AI-generated performance score
- See FPS predictions (AAA + esports games)
- Get smart upgrade suggestions based on budget

---

## 1. Entry Flow

User clicks: **"Benchmark Your Rig"**

➡️ Navigate to: **Add Your PC Screen**

---

## 2. Add Your PC

### UI:
- CPU (Search + Select)
- GPU (Search + Select)
- RAM (Search + Select)
- SSD (Search + Select)
- PSU (Search + Select)
- Motherboard (Search + Select)

### UX:
- Same search-based UI used in Architect feature
- All components selectable via search bar

---

## 3. AI Benchmark Analysis

After user submits PC build:

AI analyzes:
- CPU + GPU pairing
- RAM capacity
- Bottleneck
- Power balance

---

## 4. AI Score System

### Output:
- Overall Score (0–100)
- Gaming Score
- Productivity Score

Example:
- Gaming Score: 82/100
- Productivity Score: 75/100

---

## 5. FPS Prediction (Core Feature)

AI provides:

### AAA Games:
- 1080p FPS
- 2K FPS
- 4K FPS

### Esports Games:
- Valorant FPS
- CS:GO FPS
- Fortnite FPS

Example Output:
- Valorant: 180–240 FPS
- CS:GO: 200+ FPS
- GTA V (2K): 60–80 FPS

---

## 6. Performance Insight

AI explains:
- Bottlenecks (CPU/GPU)
- Thermal risks
- Upgrade priority

Example:
"Your GPU is limiting performance at 2K gaming"

---

## 7. Upgrade Suggestion Feature (NEW)

After showing score:

AI asks:
👉 "Enter your upgrade budget"

User inputs: ₹10,000 / ₹20,000 / etc.

---

## 8. Smart Upgrade Engine

AI suggests:
- Best upgrade within budget
- Maximum performance gain

### Example:
Budget: ₹20,000

AI Suggestion:
- Upgrade GPU → RX 6700 XT
- Expected FPS boost: +40%

---

## 9. Upgrade Output UI

### Show:
- Recommended Component
- Price Range
- Performance Gain
- Reason

---

## 10. Prompt Example (IMPORTANT)

"User PC:
CPU: Ryzen 5 3600
GPU: GTX 1660
RAM: 16GB

1. Give gaming score (0-100)
2. FPS in Valorant, CS GO, GTA V (1080p, 2K)
3. Bottleneck
4. Suggest best upgrade under ₹20000"

---

## 11. UI Design

### Benchmark Result Screen:
- Score Card (Big number)
- FPS Cards (Game-wise)
- AI Insight Box

### Upgrade Section:
- Budget Input Field
- AI Recommendation Card

---

## 12. Flow Summary

Benchmark Rig  
→ Add PC  
→ Submit  
→ AI Score + FPS  
→ Enter Budget  
→ AI Upgrade Suggestion  

---

## 13. Developer Notes

- Reuse component search system
- Store build in ViewModel
- Use Gemini API for:
  - Scoring
  - FPS prediction
  - Upgrade suggestion

---

## Conclusion

This feature will:
✔ Make your app unique  
✔ Give real value to users  
✔ Act like a PC performance analyzer  

AI becomes a gaming expert inside your app.
