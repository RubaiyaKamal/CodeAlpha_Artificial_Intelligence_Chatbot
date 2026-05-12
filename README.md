# JavaBot — AI Chatbot

A Java-based AI chatbot with Natural Language Processing, a Swing desktop GUI, and a browser-based web frontend. Built without any external ML libraries — all NLP logic is implemented from scratch.

---

## Features

- **NLP pipeline** — tokenization, stop-word removal, and a 29-rule suffix stemmer
- **TF-IDF vectorization** + cosine similarity for intent classification
- **24 intent categories** — greetings, jokes, AI/ML, programming, science, movies, music, sports, food, math, and more
- **Math evaluator** — type any expression like `25 * 4 + 10` and get the result
- **Swing desktop GUI** — dark-themed chat window with animated typing indicator
- **Web frontend** — same chat experience in the browser, served by a built-in HTTP server
- **Port scanning** — automatically tries ports 8080–8085 if the default is occupied
- **Auto-launches browser** on startup

---

## Screenshots

| Desktop GUI | Web Frontend |
|---|---|
| Dark Swing window with rounded chat bubbles | Dark purple browser UI with sidebar quick-topics |

---

## Project Structure

```
CodeAlpha_Artificial_Intelligence_Chatbot/
├── src/
│   └── main/
│       ├── java/com/chatbot/
│       │   ├── model/
│       │   │   └── Intent.java          # Data class: tag, patterns, responses
│       │   ├── NLPProcessor.java        # Tokenizer, stop-word removal, stemmer
│       │   ├── TFIDFVectorizer.java     # TF-IDF fit/transform + cosine similarity
│       │   ├── IntentClassifier.java    # Classifies input → intent tag
│       │   ├── TrainingData.java        # 24 intents with training patterns
│       │   ├── ChatEngine.java          # Core response logic + math evaluator
│       │   ├── gui/
│       │   │   └── ChatWindow.java      # Swing desktop GUI
│       │   ├── server/
│       │   │   ├── ChatServer.java      # JDK HttpServer wrapper
│       │   │   ├── ChatApiHandler.java  # POST /api/chat handler
│       │   │   └── StaticFileHandler.java # Serves web/ resources
│       │   └── Main.java               # Entry point
│       └── resources/
│           └── web/
│               ├── index.html           # Web UI markup
│               ├── style.css            # Dark purple theme
│               └── app.js              # Fetch-based chat client
├── pom.xml                              # Maven build (Java 11)
├── run.bat                              # One-click build & run (Windows)
└── .gitignore
```

---

## Getting Started

### Prerequisites

- **JDK 11 or newer** — [Download from Adoptium](https://adoptium.net/)
- **Maven** (optional) — if not installed, `run.bat` falls back to `javac`

### Run on Windows

Double-click **`run.bat`** — it builds the project and launches the app.

The console will show which port was bound:

```
[JavaBot] Web UI ready  →  http://localhost:8080
```

Both the desktop GUI and the browser will open automatically.

### Run with Maven manually

```bash
mvn clean package
java -jar target/ai-chatbot.jar
```

### Run with javac manually

```bash
# Create output directories
mkdir -p out/web

# Copy web resources
cp src/main/resources/web/* out/web/

# Compile
javac -encoding UTF-8 -d out \
  src/main/java/com/chatbot/model/Intent.java \
  src/main/java/com/chatbot/NLPProcessor.java \
  src/main/java/com/chatbot/TFIDFVectorizer.java \
  src/main/java/com/chatbot/IntentClassifier.java \
  src/main/java/com/chatbot/TrainingData.java \
  src/main/java/com/chatbot/ChatEngine.java \
  src/main/java/com/chatbot/server/ChatApiHandler.java \
  src/main/java/com/chatbot/server/StaticFileHandler.java \
  src/main/java/com/chatbot/server/ChatServer.java \
  src/main/java/com/chatbot/gui/ChatWindow.java \
  src/main/java/com/chatbot/Main.java

# Run
java -cp out com.chatbot.Main
```

---

## How It Works

### NLP Pipeline

```
User input
    │
    ▼
Tokenize (lowercase, strip punctuation, split on whitespace)
    │
    ▼
Remove stop words (~50 common English words)
    │
    ▼
Stem tokens (29 suffix-stripping rules: "running" → "run")
    │
    ▼
TF-IDF transform (query vector)
    │
    ▼
Cosine similarity against all training pattern vectors
    │
    ▼
Best match above threshold 0.12  →  intent tag
       (below threshold)         →  keyword regex fallback
                                 →  "fallback" intent
```

### Web API

| Method | Endpoint    | Body                        | Response                   |
|--------|-------------|-----------------------------|----------------------------|
| POST   | `/api/chat` | `{"message": "hello"}`      | `{"response": "Hi there!"}` |
| GET    | `/`         | —                           | `index.html`               |

### Port Binding

The server scans ports **8080 → 8085** and binds to the first available one. If all are occupied, it logs an error and only the desktop GUI runs.

---

## Topics the Bot Covers

| Category | Example inputs |
|---|---|
| Greetings | "hello", "hi", "hey there" |
| AI & ML | "what is machine learning", "explain neural networks" |
| Programming | "what is a variable", "tell me about Python" |
| Math | `25 * 4 + 10`, `(100 - 30) / 7` |
| Jokes | "tell me a joke", "make me laugh" |
| Science & Space | "what is gravity", "tell me about black holes" |
| Movies | "recommend a movie", "best films" |
| Music | "tell me about music", "favourite genre" |
| Sports | "what sports do you know", "football" |
| Food | "what should I eat", "recipe ideas" |
| Technology | "what is the internet", "explain blockchain" |
| Time & Date | "what time is it", "what's today's date" |

---

## Technologies Used

- **Java 11** — core language
- **javax.swing** — desktop GUI
- **com.sun.net.httpserver** — built-in HTTP server (no external deps)
- **HTML / CSS / JavaScript** — web frontend (vanilla, no frameworks)
- **Maven** — build and packaging

---

## Author

**Rubaiya Kamal** — CodeAlpha Internship, Month 2
