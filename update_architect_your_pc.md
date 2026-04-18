
# Architect Your PC – Updated Flow (Detailed UX + Logic)

## Overview
This document defines the updated **Architect Your PC** feature flow with:
- Step-by-step UX
- Search-based component selection
- Intel / AMD filtering logic
- Consistent UI across all components

---

## 1. Entry Screen
User clicks: **"Architect Your PC"**

➡️ Navigate to: **Select Motherboard Screen**

---

## 2. Select Motherboard

### UI:
- Title: "Select Motherboard"
- Two Cards:
  - Intel
  - AMD

### Flow:
- If user selects **Intel**
  ➝ Go to Intel Motherboard Selection
- If user selects **AMD**
  ➝ Go to AMD Motherboard Selection

---

## 3. Motherboard Search Screen

### UI Elements:
- Search Bar (Top)
- Suggested List (Top 5 Motherboards)
- Full List (Scrollable)

### Logic:
- If Intel selected:
  - Show ONLY Intel-compatible motherboards
- If AMD selected:
  - Show ONLY AMD-compatible motherboards

---

## 4. Select CPU

### UI:
- Title: "Select CPU"
- Two Options:
  - Intel
  - AMD

### Flow:
- If Intel selected:
  ➝ Show Intel CPUs only
- If AMD selected:
  ➝ Show AMD CPUs only

### Search:
- Search bar filters CPUs dynamically

---

## 5. Select GPU

### UI:
- Search Bar
- Suggested GPUs (Top 5)
- Full GPU List

### Logic:
- No brand restriction
- User can search any GPU

---

## 6. Select Other Components

Same structure for:
- Power Supply
- SSD
- RAM
- Cabinet
- Monitor

### UI Pattern (Consistent):
- Search Bar (Top)
- Top 5 Suggestions
- Scrollable List

---

## 7. UX Design Guidelines

### Consistency:
- Same layout for all components
- Search always visible

### Suggestions:
- Based on popularity + budget

### Selection Feedback:
- Highlight selected item
- Show "Selected" tag

---

## 8. Navigation Flow

Architect PC  
→ Motherboard (Intel/AMD)  
→ Search Motherboard  
→ CPU (Intel/AMD)  
→ GPU  
→ PSU  
→ SSD  
→ RAM  
→ Cabinet  
→ Monitor  
→ Final Summary  

---

## 9. Developer Notes

- Use RecyclerView for lists
- Use SearchView for filtering
- Maintain selected components in ViewModel
- Apply filters using tags (intel/amd)

---

## 10. UI Wireframe Idea

[ Search Bar ]
-------------------
| Top Suggestions |
-------------------
| Component List |
-------------------

---

## Conclusion
This flow ensures:
✔ Clean UX  
✔ Fast selection  
✔ Smart filtering  
✔ Scalable architecture  

