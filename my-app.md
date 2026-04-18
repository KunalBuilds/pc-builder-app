# MyPC Builder App — Screen-by-Screen UI/UX Specification
### Design System: "Neon Nucleus" · Dark Theme Only · For Stitch Screen Generation

---

## 🎨 Global Design System

**Background:** `#131314` (near-black) · **Primary Accent:** `#00F0FF` (Neon Cyan)
**Surface cards:** `#2A2A2B` · **Text:** `#E5E2E3` · **Subtext:** `#B9CACB`
**Success:** `#4CAF50` · **Warning/Amber:** `#FFC107` · **Error:** `#FFB4AB`
**Font style:** Bold headers, ALL-CAPS section labels, monospace for data · **Corner radius:** 20–28dp cards, 14–16dp buttons
**Special FX:** Neon cyan glow circle behind icons · Gradient buttons (cyan-to-teal, dark text) · 1dp cyan stroke on active/AI cards

---

---

# SCREEN 1 — Splash Screen

**Purpose:** App launch screen shown while Firebase auth state is checked.

## Layout Structure
- Full-screen `FrameLayout`, background `#131314`
- Layer 1 (bottom): Hero image `login_hero_bg` — full bleed, `centerCrop`, `alpha 0.4`
- Layer 2: Dark overlay `#CC131314` covering full screen
- Layer 3 (top): Centered content column

## Center Content (vertical, gravity center)
```
┌─────────────────────────────────┐
│                                 │
│      [Neon Glow Circle 110dp]   │
│       [PC Icon 80dp inside]     │
│                                 │
│      NEON NUCLEUS               │  ← 11sp · Bold · #00F0FF · letterSpacing 0.3
│      MyPC App                   │  ← 38sp · Bold · #E5E2E3
│      Build Your Dream PC        │  ← 15sp · #00F0FF
│                                 │
│      ──── [Loading Bar] ────    │  ← 180dp wide · cyan · indeterminate
│                                 │
└─────────────────────────────────┘
          v1.0 • Powered by AI      ← 11sp · #849495 · bottom center
```

## Key UI Details
- Neon glow circle: `@drawable/neon_glow_circle`, `alpha 0.4`, 110×110dp
- PC icon sits centered inside glow circle at 80×80dp
- Progress bar: `LinearProgressIndicator`, `trackCornerRadius 4dp`, cyan indicator
- Version footer pinned to bottom with 24dp margin

## Behaviour
- Auto-navigates after Firebase auth check completes
- Logged in → Home · Not logged in → Login Screen

---

---

# SCREEN 2 — Login Screen

**Purpose:** User authentication via Email/Password. Google Sign-In placeholder present.

## Layout Structure
- Full-screen `FrameLayout`, background `#131314`
- Layer 1: Hero image `login_hero_bg` — top 320dp, `centerCrop`
- Layer 2: Gradient fade overlay (`bg_fade_bottom`) over hero image bottom
- Layer 3: `ScrollView` containing all content

## Top Branding Section (overlaid on hero, gravity bottom-center, 260dp height)
```
┌─────────────────────────────────┐
│  [hero image — 320dp tall]      │
│                                 │
│         NEON NUCLEUS            │  ← 11sp · Bold · #00F0FF · letterSpacing 0.3
│         MyPC App                │  ← 36sp · Bold · #E5E2E3
│       SYSTEM_ACCESS_PROTOCOL    │  ← 12sp · #B9CACB · letterSpacing 0.1
└─────────────────────────────────┘
```

## Login Card (Glassmorphism)
- Background: `#E6201F20` (90% opaque dark surface) · `cornerRadius 28dp` · `elevation 0dp`
- Horizontal padding: 20dp · Internal padding: 28dp

```
   Initialize Session               ← 22sp · Bold · #E5E2E3
   Verify operator credentials...   ← 13sp · #B9CACB · marginBottom 24dp

   OPERATOR ID (EMAIL)              ← 10sp · Bold · #00F0FF · letterSpacing 0.15
   ┌──────────────────────────────┐
   │  operator@mypcapp.com        │  ← 52dp height · OutlinedBox · stroke #00F0FF
   └──────────────────────────────┘

   ACCESS KEY (PASSWORD)            ← 10sp · Bold · #00F0FF · marginTop 18dp
   ┌──────────────────────────────┐
   │  ••••••••              [👁]  │  ← 52dp height · passwordToggle cyan
   └──────────────────────────────┘

   [      ACCESS SYSTEM       ]     ← 56dp · neon gradient btn · #001A1A text · Bold

   [ProgressBar]                    ← spinner · cyan · hidden until loading

   ─────────────── OR ──────────────

   [ Create New Account ]           ← 52dp · outlined · strokeColor #00F0FF · text #00F0FF

   [ Google Sign-In (disabled) ]    ← text button · alpha 0.4 · currently inactive
```

## Footer
```
      BUILD  •  COMPARE  •  UPGRADE   ← 11sp · #849495 · center · letterSpacing 0.3
```

---

---

# SCREEN 3 — Home Dashboard

**Purpose:** Main hub screen. Navigates to the 3 core features of the app.

## Layout Structure
- `LinearLayout` vertical, background `#131314`
- Top bar (56dp) + scrollable content

## Top Bar (56dp, `#1C1B1C`)
```
 [PC Icon 28dp]  NEON NUCLEUS             [⋮ Logout menu]
                 ← 14sp · Bold · #00F0FF · letterSpacing 0.15
```

## Hero Banner (200dp, 16dp horizontal margin)
- Background: `home_hero_banner` image · `centerCrop`
- Overlay: `#BF0D1B2A` dark tint layer

```
┌──────────────────────────────────────────┐
│  [hero image — full bleed]               │
│                                          │
│  OPERATOR CONSOLE          ← 10sp #00F0FF│
│  Welcome, [Name]! 👋       ← 24sp Bold   │
│  Initialize new config...  ← 12sp #B9CACB│
└──────────────────────────────────────────┘
```

## Section Label
```
  CORE ACTIONS    ← 10sp · Bold · #00F0FF · marginStart 20dp
```

## Three Action Cards (stacked, 16dp horizontal margin, 12dp gap between)

### Card 1 — Architect Your PC
```
┌──────────────────────────────────────────┐
│ [🖥️ glow bg]  BUILD                      │ ← 10sp · Bold · #00F0FF
│               Architect Your PC          │ ← 18sp · Bold · #E5E2E3
│               Choose components, set     │ ← 12sp · #B9CACB
│               budget & get AI builds     │              ›  ← 32sp #00F0FF
└──────────────────────────────────────────┘
  bg: #2A2A2B · cornerRadius 22dp · glow alpha 0.25
```

### Card 2 — Benchmark Your Rig
```
┌──────────────────────────────────────────┐
│ [🏆 glow bg]  COMPARE                    │ ← 10sp · Bold · #4CAF50 (green)
│               Benchmark Your Rig        │ ← 18sp · Bold · #E5E2E3
│               Compare builds against    │ ← 12sp · #B9CACB
│               global specs & configs    │              ›  ← 32sp #4CAF50
└──────────────────────────────────────────┘
  glow alpha 0.15 · arrow/label: green #4CAF50
```

### Card 3 — Help & AI Chat
```
┌──────────────────────────────────────────┐
│ [💬 glow bg]  SUPPORT HUB               │ ← 10sp · Bold · #FFC107 (amber)
│               Help & AI Chat            │ ← 18sp · Bold · #E5E2E3
│               AI chatbot + expert       │ ← 12sp · #B9CACB
│               phone support             │              ›  ← 32sp #FFC107
└──────────────────────────────────────────┘
  glow alpha 0.12 · arrow/label: amber #FFC107
```

---

---

# SCREEN 4 — Platform Picker (Start of PC Build Flow)

**Purpose:** First step in the "Architect Your PC" flow. User picks Intel or AMD platform which determines compatible motherboards and CPUs.

## Layout Structure
- Toolbar + 4dp step progress bar + scrollable content

## Header
```
  Architect Your PC    ← toolbar title · back arrow
  ══════════════▌      ← 4dp progress bar · 13% cyan fill

  STEP 1 OF 8          ← 10sp · Bold · #00F0FF · letterSpacing 0.25
  Select Motherboard   ← 26sp · Bold · #E5E2E3
  Choose your platform. This determines
  which CPUs are compatible.         ← 13sp · #B9CACB · marginBottom 32dp
```

## Platform Cards (160dp height, cornerRadius 22dp)

### Intel Card
```
┌──────────────────────────────────────────┐
│  [🔵 glow circle 80dp, alpha 0.3]        │
│      PLATFORM         LGA1700 · DDR5     │ ← 10sp · Bold · #00F0FF
│      Intel            High-clock perf    │ ← 26sp · Bold
│                                       ›  │ ← 36sp · #00F0FF
└──────────────────────────────────────────┘
  bg: #201F20 · selected stroke: #00F0FF 2dp
```

### AMD Card
```
┌──────────────────────────────────────────┐
│  [🔴 glow circle 80dp, alpha 0.15]       │
│      PLATFORM         AM4/AM5 socket     │ ← 10sp · Bold · #FF6B6B (red)
│      AMD              Multi-core champ   │ ← 26sp · Bold
│                                       ›  │ ← 36sp · #FF6B6B
└──────────────────────────────────────────┘
  bg: #201F20 · selected stroke: #FF4444 2dp
```

## Info Note Card
```
┌──────────────────────────────────────────┐
│  ℹ️  Your choice filters compatible MBs   │ ← 12sp · #00F0FF
│        and CPUs. GPU, RAM, and other     │
│        components remain brand-agnostic. │
└──────────────────────────────────────────┘
  bg: #1A00F0FF (cyan tint) · cornerRadius 12dp
```

---

---

# SCREEN 5 — Component Selection (Steps 2–8)

**Purpose:** Repeating step screen used for picking each PC component: CPU, GPU, RAM, SSD, PSU, Monitor, Cabinet. Reuses the same layout across all 7 remaining steps.

## Layout Structure
- Toolbar + step progress bar + search + scrollable list + bottom bar

## Header Area
```
  Select Components    ← toolbar title · back arrow
  ██████▌              ← 4dp progress bar · advances 12% per step (12→25→37→50→62→75→87→100%)

  🔌 Motherboard        Step 1 of 8   ← category title 18sp Bold left · step right 12sp #00F0FF
  No component selected yet            ← 13sp · monospace · #B9CACB (updates on selection)
```

## Search Bar
```
  🔍  Search components...             ← 48dp · OutlinedBox · stroke #00F0FF · cyan search icon
```

## Content Sections (scrollable)
```
  ⭐ TOP SUGGESTIONS    ← 10sp · Bold · #00F0FF · letterSpacing 0.2
  ─────────────────────────────────────────
  [RecyclerView — top-scored / recommended components]

  ──── thin divider #1AFFFFFF ────

  📋 ALL COMPONENTS     ← 10sp · Bold · #B9CACB · letterSpacing 0.2
  ─────────────────────────────────────────
  [RecyclerView — full filtered + searchable list]
```

## Component Card Item (in both lists)
```
┌──────────────────────────────────────────┐
│  [icon]  Component Name      Score: 82  │ ← name 15sp Bold · score badge cyan
│          Brand · Category               │ ← 12sp · #B9CACB
│          ₹25,000          [🛒][🛒]       │ ← price Bold #00F0FF · Amazon🟠 Flipkart🔵
└──────────────────────────────────────────┘
  selected state: cyan glow border 2dp
```

## Bottom Bar (elevated, `#1C1B1C`)
```
  [ Skip ]              [ Next → ]
  text·#B9CACB·wt1      primary·52dp·cornerRadius 16dp·weight 2
```

---

---

# SCREEN 6 — Budget Planner

**Purpose:** User sets their total PC build budget. AI uses this to build or adjust the PC within the budget. Shows current selected component cost and triggers AI build.

## Layout Structure
- Mini toolbar (56dp) + scrollable content

## Mini Toolbar
```
  [PC Icon 32dp]   Budget Planner    ← 18sp · Bold · #E5E2E3
```

## Hero Section (centered, 28dp bottom padding)
```
          [🔵 glow 90dp · alpha 0.2]
               [💰 emoji 48sp]

          BUDGET PLANNER              ← 10sp · Bold · #00F0FF · letterSpacing 0.25
          Set Your Budget             ← 26sp · Bold · #E5E2E3
          Tell us your budget and our AI will
          engineer the perfect PC build for you   ← 13sp · #B9CACB · center
```

## Current Build Cost Card
```
  ┌──────────────────────────────────────────┐
  │  🖥️  Current selected build cost: ₹0     │  ← 14sp · Bold · #00F0FF · center
  └──────────────────────────────────────────┘
  bg: #201F20 · cornerRadius 16dp
```

## Quick Select Chips
```
  QUICK SELECT    ← 10sp · Bold · #00F0FF

  [₹30,000]  [₹50,000]  [₹75,000]  [₹1,00,000]  [₹1,50,000]
  ← Material3 Chip.Filter · bg #2A2A2B · selected: cyan fill
```

## Custom Budget Input
```
  CUSTOM BUDGET    ← 10sp · Bold · #00F0FF

  ┌─────────────────────────────────────┐
  │  ₹  │  Enter amount                 │  ← 64dp · 22sp · prefix "₹ " in cyan
  └─────────────────────────────────────┘
  OutlinedBox · prefix text #00F0FF · stroke cyan

  💡 AI recommends the best components within your budget
     with live buy links    ← 13sp · #B9CACB
```

## CTA Button
```
  [      GET AI SUGGESTIONS  🚀      ]   ← 60dp · neon gradient · Bold 15sp · #001A1A text
```

## AI Loading State (hidden → shown after button tap)
```
  🤖 AI is analyzing components...      ← 14sp · Bold · #00F0FF · center
  ██████████████░░░░░░░░░░░░░░░         ← 6dp progress bar · animated cyan
  → text cycles through step messages
```

## AI Build Result Card (appears after AI completes)
```
  ┌──────────────────────────────────────────┐
  │  🤖   AI BUILD READY      ← 10sp #00F0FF │
  │       ₹47,500 / ₹50,000   ← 20sp Bold    │  [✅]
  │       8 components selected ← 13sp #B9CACB│
  │                                           │
  │  [       VIEW AI BUILD →        ]         │ ← neon gradient button 56dp
  └──────────────────────────────────────────┘
  stroke: #00F0FF 1dp · cornerRadius 20dp · bg: #201F20
```

## Footer
```
  Powered by AI — Based on current Indian market prices   ← 11sp · #849495 · center
```

---

---

# SCREEN 7 — Build Result / Your PC

**Purpose:** Final screen showing the complete PC build. Displays AI Gemini performance analysis, all selected components with buy links, and upgrade suggestions.

## Layout Structure
- Toolbar + scrollable content

## Toolbar
```
  Your PC Build    ← title · back arrow
  (AI builds: "🤖 AI PC Build")
```

## Build Ready Hero Card (cornerRadius 24dp, `#2A2A2B`)
```
  ┌──────────────────────────────────────────┐
  │  ✅ Build Ready!      ← 20sp · Bold · #4CAF50  │  [🖥️ 48sp]
  │  (AI: "🤖 AI Built This PC!")                  │
  │  Budget: ₹50,000      ← 13sp · #B9CACB         │
  │  Total: ₹47,500       ← 26sp · Bold · #00F0FF  │
  │                                                │
  │  ✅ Within Budget! ₹2,500 remaining             │ ← 14sp · Bold · #4CAF50
  └──────────────────────────────────────────┘
```

## Action Buttons Row
```
  [ 💾 Save Build ]          [ 📤 Share ]
  50dp · primary #00F0FF     50dp · outlined · stroke #00F0FF
  cornerRadius 14dp           cornerRadius 14dp
```

## Gemini AI Analysis Card (stroke #00F0FF 1dp, cornerRadius 20dp)
```
  ┌──────────────────────────────────────────┐
  │  🤖   GEMINI AI ANALYSIS  ← 10sp Bold #00F0FF   [spinner]  │
  │       Performance Prediction  ← 15sp Bold #E5E2E3           │
  │                                                              │
  │  Analyzing your build...                                     │
  │  → fills with Gemini FPS prediction + bottleneck analysis    │
  │  ← 13sp · #B9CACB · lineSpacing 1.4x                         │
  └──────────────────────────────────────────┘
```

## Your Components Section
```
  YOUR COMPONENTS    ← 11sp · Bold · #00F0FF · letterSpacing 0.15
  ─────────────────────────────────────────
  [RecyclerView — component result cards]
```

## Component Result Card Item
```
  ┌──────────────────────────────────────────┐
  │  [icon]  Component Name                 │
  │          Brand · ₹Price                 │
  │  [🛒 Amazon]    [🛒 Flipkart]           │ ← orange / blue buttons
  └──────────────────────────────────────────┘
```

## Upgrade Suggestions Section
```
  💡 UPGRADE SUGGESTIONS    ← 11sp · Bold · #FFC107 (amber) · marginTop 24dp
  ─────────────────────────────────────────
  [RecyclerView — upgrade suggestion cards]
  OR:
  ✅ Your build is already optimal within your budget!   ← 14sp · #4CAF50 · center
```

---

---

# SCREEN 8 — Benchmark Your Rig

**Purpose:** AI-powered PC performance analyzer. User enters their existing PC specs and gets a Gemini AI-generated score, FPS predictions across resolutions and game types, and smart upgrade suggestions within a budget.

## Layout Structure
- Toolbar + scrollable content

## Toolbar
```
  Benchmark Your Rig    ← title · back arrow
```

## Hero Banner Card (120dp, cornerRadius 20dp, stroke #00F0FF 1dp)
```
  ┌──────────────────────────────────────────────┐
  │  AI PERFORMANCE ANALYZER  ← 10sp · #00F0FF   │
  │  Benchmark Your Rig       ← 22sp · Bold       │  [⚡ 48sp]
  │  Enter your PC specs → Get AI score + FPS    │ ← 12sp · #B9CACB
  └──────────────────────────────────────────────┘
  bg: #2A2A2B
```

## Input Form — Your PC Specs
```
  YOUR PC SPECS    ← 11sp · Bold · #00F0FF

  ┌──────────────────────────────────────────┐
  │  CPU          e.g. Intel i5-12400F       │
  ├──────────────────────────────────────────┤
  │  GPU          e.g. RTX 3060 12GB         │
  ├──────────────────────────────────────────┤
  │  RAM          e.g. 16GB DDR4 3200MHz     │
  ├──────────────────────────────────────────┤
  │  Storage      e.g. 512GB NVMe SSD        │
  ├──────────────────────────────────────────┤
  │  PSU          e.g. 650W 80+ Gold Corsair │
  ├──────────────────────────────────────────┤
  │  Motherboard  e.g. MSI B660M Pro         │
  └──────────────────────────────────────────┘
  All: OutlinedBox · stroke #00F0FF
```

## CTA Button
```
  [ ⚡  ANALYZE MY RIG WITH AI ]    ← 56dp · primary #00F0FF · Bold 15sp · cornerRadius 16dp
```

## Loading State (shown while Gemini processes)
```
          [Spinner 48dp · cyan]
          🔍 Reading your CPU specs...    ← 14sp · Bold · #00F0FF
          → animates through 5 messages:
            "🎮 Analyzing GPU..."
            "🧠 Calculating bottlenecks..."
            "📊 Predicting FPS scores..."
            "✅ Compiling AI report..."
```

## AI Score Card (stroke #00F0FF 1dp, cornerRadius 20dp)
```
  ┌──────────────────────────────────────────────────────┐
  │  🤖 AI PERFORMANCE SCORE    ← 10sp · Bold · #00F0FF  │
  │                                                      │
  │    78          82              74                    │
  │  52sp score  38sp #FF6B35   38sp #4CAF50             │
  │  Overall     🎮 Gaming       💼 Productivity          │
  │  (colour: green≥80, orange≥60, red<60)               │
  └──────────────────────────────────────────────────────┘
```

## FPS Cards — AAA Games (3-column row)
```
  🎮 FPS PREDICTIONS — AAA GAMES    ← 11sp · Bold · #00F0FF

  ┌──────────┐  ┌──────────┐  ┌──────────┐
  │  70-90   │  │  45-60   │  │  22-30   │  ← 24sp · #00F0FF
  │ FPS 1080p│  │ FPS 1440p│  │  FPS 4K  │  ← 11sp · #B9CACB
  └──────────┘  └──────────┘  └──────────┘
  bg: #201F20 · cornerRadius 14dp
```

## FPS Cards — Esports (3-column row)
```
  🏆 FPS PREDICTIONS — ESPORTS    ← 11sp · Bold · #FF6B35 (orange)

  ┌──────────┐  ┌──────────┐  ┌──────────┐
  │ 200-280  │  │ 180-240  │  │ 120-160  │  ← 22sp · #FF6B35
  │ Valorant │  │   CS2    │  │ Fortnite │  ← 11sp · #B9CACB
  └──────────┘  └──────────┘  └──────────┘
```

## AI Insight Card (stroke #00F0FF 1dp)
```
  ┌──────────────────────────────────────────────────────┐
  │  🤖 AI PERFORMANCE INSIGHT    ← 11sp · Bold · #00F0FF│
  │                                                      │
  │  Your GPU is the strongest component. CPU may        │
  │  slightly bottleneck at 1440p. Upgrade RAM to        │
  │  32GB for better multitasking performance.           │
  │                         ← 13sp · #B9CACB · 1.5x line│
  └──────────────────────────────────────────────────────┘
```

## Upgrade Suggester Section
```
  💡 AI UPGRADE SUGGESTER    ← 11sp · Bold · #4CAF50 (green)

  ┌──────────────────────────────────────┐
  │  Your upgrade budget (₹)             │  ← OutlinedBox · stroke #4CAF50
  └──────────────────────────────────────┘

  [ 💡  GET AI UPGRADE SUGGESTION ]    ← 52dp · bg #4CAF50 · Bold
```

## Upgrade Result Card (stroke #4CAF50 1dp, cornerRadius 20dp)
```
  ┌──────────────────────────────────────────────────────┐
  │  💡  AI UPGRADE RECOMMENDATION  ← 10sp · Bold · #4CAF50
  │      Best bang for your budget                       │
  │                                         [spinner]    │
  │  🔧 Recommended Upgrade: RX 6700 XT                 │
  │  💰 Price: ₹28,000–32,000                           │
  │  📈 Expected Gain: +40% FPS at 1440p                 │
  │  🎯 Why: Your 1660 is the bottleneck at 2K...        │
  │                         ← 13sp · #B9CACB · 1.5x line│
  └──────────────────────────────────────────────────────┘
```

---

---

# SCREEN 9 — Help & AI Chat

**Purpose:** Gemini AI-powered PC building chatbot. Users ask anything about PC building and get real AI responses. Also includes quick-question chips and a phone support button.

## Layout Structure
- Toolbar + chat RecyclerView + typing indicator + quick chips + input bar + call button

## Toolbar
```
  Help & Support    ← title · back arrow · bg #1C1B1C
```

## Chat Area (RecyclerView, `stackFromEnd = true`)

### Bot Message Bubble
```
  ╔═══════════════════════════════╗
  ║  👋 Hi! I'm your AI PC       ║  ← bg #201F20 · cornerRadius (TL=0)
  ║  Building Expert, powered    ║  ← 14sp · #E5E2E3 · lineSpacing 1.4x
  ║  by Google Gemini...          ║  left-aligned · max 80% width
  ╚═══════════════════════════════╝
```

### User Message Bubble
```
                   ╔══════════════════╗
                   ║  Best PC under  ║  ← bg #00BCD4 (cyan) · cornerRadius (TR=0)
                   ║  ₹60,000?       ║  ← 14sp · #FFFFFF · Bold · right-aligned
                   ╚══════════════════╝
```

## Typing Indicator
```
  🤖 AI is thinking...    ← 13sp · #00F0FF · paddingHorizontal 20dp · shown while Gemini responds
```

## Quick-Question Chips (horizontal scroll, no scrollbar visible)
```
  [Best ₹50k PC?]   [Best GPU for gaming?]   [Check compatibility?]
  ← Material3 Chip.Suggestion · bg #2A2A2B · text #E5E2E3 · no stroke
```

## Input Bar (bottom, `#1C1B1C`, elevation 8dp)
```
  ┌──────────────────────────────────────┐  ┌──────┐
  │  Ask anything about PC building...   │  │  →   │
  │  OutlinedBox · stroke #00F0FF        │  │ 52dp │ ← circular · bg #00F0FF
  └──────────────────────────────────────┘  └──────┘
  input weight 1 · button weight 0
```

## Call Support Button (full width, bottom)
```
  [ 📞  Call Our PC Expert: 1800-PC-HELP ]    ← 52dp · bg #4CAF50 · Bold 14sp · #FFFFFF text
```

---

---

# SCREEN 10 — Compare PC Builds

**Purpose:** Side-by-side component comparison between two custom PC builds. User selects components for Build 1 and Build 2, then compares total cost, CPU, and GPU.

## Layout Structure
- Toolbar + scrollable content

## Toolbar
```
  Compare PC Builds    ← title · bg #00F0FF · white title text · elevation 4dp
```

## Header Row
```
  Build 1          VS          Build 2
  ← 18sp #00F0FF   ← 18sp #FFC107   ← 18sp #4CAF50 (green)
  center           center            center
```

## Comparison Rows (one card per component)

### CPU Row Card (white bg, cornerRadius 16dp, elevation 4dp)
```
  ┌─────────────────────────────────────────────────────┐
  │  🖥️ Processor (CPU)    ← 13sp · Bold · #B9CACB       │
  │                                                     │
  │  Not selected   ┃   Not selected                    │
  │                 ┃                                   │
  │  [ Select ]     ┃   [ Select ]                      │ ← 36dp buttons
  │   cyan bg       ┃   green bg                        │
  └─────────────────────────────────────────────────────┘
```

### GPU Row Card (same structure)
```
  🎮 Graphics Card (GPU)
  Build 1 slot  ┃  Build 2 slot
  [Select cyan] ┃  [Select green]
```

## Compare Button
```
  [ ⚖️  Compare Builds ]    ← 56dp · bg #00F0FF · Bold 17sp · cornerRadius 16dp
```

## Results Card (white bg, cornerRadius 16dp)
```
  ┌────────────────────────────────────────────────────┐
  │  Select components above and tap Compare           │
  │  to see results.                                   │
  │  → fills with text comparison after tap            │
  │  ← 14sp · #B9CACB · monospace font                 │
  └────────────────────────────────────────────────────┘
```

---

---

# 📊 App Summary

## Total Screen Count

| # | Screen Name | File | Purpose |
|---|-------------|------|---------|
| 1 | **Splash Screen** | `activity_splash.xml` | Launch · Auth check |
| 2 | **Login Screen** | `activity_login.xml` | Email login · Account creation |
| 3 | **Home Dashboard** | `activity_main.xml` | Main hub · 3 feature entry points |
| 4 | **Platform Picker** | `activity_motherboard_picker.xml` | Intel vs AMD platform select |
| 5 | **Component Selection** | `activity_component_selection.xml` | Step 1–8 · Pick each PC part |
| 6 | **Budget Planner** | `activity_budget_input.xml` | Set budget · Trigger AI build |
| 7 | **Build Result** | `activity_result.xml` | Final build · Gemini AI analysis |
| 8 | **Benchmark Your Rig** | `activity_benchmark.xml` | Existing PC analysis · AI scores |
| 9 | **Help & AI Chat** | `activity_help.xml` | Gemini chatbot · Phone support |
| 10 | **Compare PC Builds** | `activity_compare_pc.xml` | Side-by-side CPU/GPU compare |

> **Total: 10 unique screens** across the app.
> Screen 5 (Component Selection) is reused 8 times in the build flow (one per component category: Motherboard, CPU, GPU, RAM, SSD, PSU, Monitor, Cabinet).
> This makes the effective screen count **17 screen instances** in a full Architect Your PC flow.

## Navigation Flow
```
Splash (1)
   └──→ Login (2)
            └──→ Home Dashboard (3)
                   ├──→ [BUILD] Platform Picker (4)
                   │              └──→ Component Selection × 8 (5)
                   │                        └──→ Budget Planner (6)
                   │                                  └──→ Build Result (7)
                   ├──→ [BENCHMARK] Benchmark Your Rig (8)
                   └──→ [HELP] Help & AI Chat (9)
                   └──→ [COMPARE] Compare PC Builds (10)  ← via overflow/direct
```

## AI Integration Summary
| Screen | Gemini Feature |
|--------|---------------|
| Screen 6 — Budget Planner | AI assembles full PC build within budget |
| Screen 7 — Build Result | AI predicts FPS, bottlenecks, upgrade paths |
| Screen 8 — Benchmark | AI scores PC · predicts FPS per game/resolution · suggests upgrades |
| Screen 9 — AI Chat | Real-time multi-turn conversation · PC building expert |
