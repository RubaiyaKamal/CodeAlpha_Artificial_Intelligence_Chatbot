package com.chatbot;

import com.chatbot.model.Intent;
import java.util.*;

/**
 * Curated training corpus covering greetings, FAQs, and domain topics.
 * Each Intent has multiple patterns (training examples) and multiple
 * possible responses (one is chosen at random at runtime).
 */
public class TrainingData {

    public static List<Intent> getIntents() {
        List<Intent> list = new ArrayList<>();

        list.add(new Intent("greeting",
            Arrays.asList(
                "hello", "hi", "hey", "good morning", "good evening", "good afternoon",
                "howdy", "whats up", "sup", "greetings", "hi there", "hello there",
                "good day", "hiya", "yo", "morning", "evening", "hey there",
                "hi bot", "hello bot", "hey bot"
            ),
            Arrays.asList(
                "Hello! How can I help you today?",
                "Hi there! What can I do for you?",
                "Hey! Nice to chat with you. What's on your mind?",
                "Greetings! How may I assist you today?",
                "Hello! I'm ready to help. What would you like to know?"
            )
        ));

        list.add(new Intent("farewell",
            Arrays.asList(
                "bye", "goodbye", "see you", "farewell", "take care", "later",
                "good night", "good bye", "ciao", "see ya", "ttyl", "talk later",
                "have a good day", "have a nice day", "im leaving", "im going",
                "catch you later", "until next time", "so long"
            ),
            Arrays.asList(
                "Goodbye! Have a wonderful day!",
                "See you later! Take care!",
                "Bye! It was great chatting with you!",
                "Farewell! Come back anytime you need help!",
                "Take care! Goodbye! Come back soon!"
            )
        ));

        list.add(new Intent("thanks",
            Arrays.asList(
                "thank you", "thanks", "thank you so much", "many thanks",
                "thanks a lot", "appreciate it", "grateful", "cheers", "ty",
                "thank you very much", "thx", "thanks a bunch", "much appreciated",
                "thanks for your help", "that was helpful"
            ),
            Arrays.asList(
                "You're welcome! Happy to help!",
                "Glad I could help! Is there anything else?",
                "My pleasure! Feel free to ask anything else.",
                "Anytime! I'm always here when you need me.",
                "You're very welcome! Have a great day!"
            )
        ));

        list.add(new Intent("identity",
            Arrays.asList(
                "who are you", "what are you", "tell me about yourself",
                "what is your name", "whats your name", "what do i call you",
                "are you a robot", "are you a bot", "are you human", "are you an ai",
                "introduce yourself", "your name please", "what should i call you",
                "tell me your name", "describe yourself"
            ),
            Arrays.asList(
                "I'm JavaBot, an AI-powered chatbot built with Java and NLP techniques! I use TF-IDF vectorization and cosine similarity to understand your messages.",
                "I'm an intelligent chatbot created using Java with Natural Language Processing. Call me JavaBot!",
                "Hi! I'm JavaBot - your AI assistant. I can answer questions, tell jokes, and hold a great conversation.",
                "I'm JavaBot! Built with Java, I use machine learning techniques like TF-IDF and cosine similarity to classify your intents and respond intelligently."
            )
        ));

        list.add(new Intent("help",
            Arrays.asList(
                "help", "help me", "what can you do", "your capabilities",
                "how do you work", "what do you know", "what topics can you discuss",
                "assist me", "i need help", "guide me", "show me what you can do",
                "tell me your features", "what are you capable of", "what can i ask",
                "what topics", "list your features"
            ),
            Arrays.asList(
                "I can help with many things! Ask me about: AI & Machine Learning, Programming, Science, Sports, Movies, Music, Food, Math calculations, and more. I can also tell jokes and chat casually!",
                "Here's what I can do: answer questions on technology, science, entertainment, and culture; do basic math; tell jokes; discuss AI concepts; and have friendly conversations. Just ask!",
                "I'm here to help! Topics I cover: Artificial Intelligence, Programming, Science & Space, Sports, Movies, Music, Food, Technology trends, and casual conversation. What would you like to explore?"
            )
        ));

        list.add(new Intent("joke",
            Arrays.asList(
                "tell me a joke", "joke", "make me laugh", "funny joke",
                "say something funny", "humor me", "tell a joke",
                "give me a joke", "any jokes", "be funny", "cheer me up",
                "make me smile", "i want to laugh", "something funny please"
            ),
            Arrays.asList(
                "Why do programmers prefer dark mode? Because light attracts bugs!",
                "Why did the AI go to school? To improve its neural networks!",
                "What do you call a robot who always takes the longest route? A detour-inator!",
                "Why don't scientists trust atoms? Because they make up everything!",
                "How does a computer get drunk? It takes screenshots!",
                "Why did the Java developer wear glasses? Because he couldn't C#!",
                "What's a computer's favorite snack? Microchips!",
                "Why did the programmer quit his job? He didn't get arrays!",
                "What do you call 8 hobbits? A hobbyte!",
                "Why is 6 afraid of 7? Because 7 8 9!",
                "I tried to tell a UDP joke, but you might not get it.",
                "A SQL query walks into a bar and asks: WHERE is the beer? The bartender says: ORDER BY price LIMIT 1."
            )
        ));

        list.add(new Intent("weather",
            Arrays.asList(
                "whats the weather", "weather today", "weather forecast",
                "is it raining", "will it rain", "temperature today",
                "hows the weather", "weather outside", "weather report",
                "is it sunny", "is it cloudy", "should i carry umbrella",
                "tell me the weather", "current weather"
            ),
            Arrays.asList(
                "I don't have access to live weather data, but you can check weather.com or your phone's weather app for real-time forecasts!",
                "I can't check current weather conditions, but I recommend AccuWeather or the Weather Channel for accurate forecasts!",
                "For live weather updates, please check a weather service or your local news. I'm unable to access real-time data!"
            )
        ));

        list.add(new Intent("time",
            Arrays.asList(
                "what time is it", "current time", "tell me the time",
                "whats the time", "time now", "time please", "time right now",
                "what is the time", "can you tell me the time"
            ),
            Arrays.asList("__TIME__")
        ));

        list.add(new Intent("date",
            Arrays.asList(
                "whats todays date", "current date", "todays date", "what date is it",
                "what day is today", "tell me the date", "what day is it",
                "date today", "day today", "what is todays date",
                "what month is it", "what year is it"
            ),
            Arrays.asList("__DATE__")
        ));

        list.add(new Intent("ai",
            Arrays.asList(
                "what is artificial intelligence", "explain ai", "what is machine learning",
                "deep learning", "neural networks", "natural language processing",
                "how does ai work", "what is ml", "ai definition",
                "explain machine learning", "what is data science",
                "what is deep learning", "explain neural networks",
                "what is nlp", "how do chatbots work"
            ),
            Arrays.asList(
                "Artificial Intelligence (AI) is the simulation of human intelligence by machines. It enables computers to learn, reason, and solve problems. Machine Learning is a subset where systems learn from data without explicit programming!",
                "AI involves creating intelligent machines that can perform tasks requiring human intelligence: reasoning, learning, problem-solving, perception, and language understanding. This chatbot is a real example of AI!",
                "Machine Learning is a branch of AI where systems improve through experience. Deep Learning uses multilayered neural networks to recognize complex patterns in data, powering things like image recognition and language translation.",
                "Natural Language Processing (NLP) is what powers me! It's the branch of AI that helps computers understand, interpret, and generate human language. I use TF-IDF vectorization and cosine similarity to classify your messages."
            )
        ));

        list.add(new Intent("programming",
            Arrays.asList(
                "what is java", "java programming", "explain python", "python programming",
                "what is coding", "learn programming", "how to code",
                "what is a variable", "object oriented programming", "what is oop",
                "what is an algorithm", "data structures", "what is javascript",
                "what is html", "web development", "what is a function",
                "what is a class", "what is inheritance"
            ),
            Arrays.asList(
                "Java is a powerful object-oriented programming language that follows 'Write Once, Run Anywhere'. It's widely used for enterprise applications, Android development, and backend services. This chatbot is built with Java!",
                "Programming is writing instructions for computers. Popular languages: Java (enterprise & Android), Python (AI & data science), JavaScript (web), C++ (systems), Swift (iOS). Each shines in its own domain!",
                "Object-Oriented Programming (OOP) organizes software around objects rather than functions. The four pillars are Encapsulation (data hiding), Inheritance (code reuse), Polymorphism (many forms), and Abstraction (simplification).",
                "An algorithm is a step-by-step procedure for solving a problem. Classic examples: Binary Search (O(log n) lookup), QuickSort (O(n log n) sorting), Dijkstra's algorithm (shortest path), and BFS/DFS (graph traversal)."
            )
        ));

        list.add(new Intent("mental_health",
            Arrays.asList(
                "i am sad", "i feel sad", "im depressed", "feeling anxious",
                "im stressed", "i feel lonely", "im upset", "im not okay",
                "feeling down", "im unhappy", "life is hard", "im struggling",
                "i need support", "feeling overwhelmed", "i am worried",
                "im having a bad day", "things arent going well"
            ),
            Arrays.asList(
                "I'm sorry to hear you're feeling this way. Remember, it's okay to not be okay. Please consider talking to a friend, family member, or professional counselor. You're not alone!",
                "I hear you, and your feelings are valid. Taking care of your mental health is important. Consider deep breathing, a short walk, or calling someone you trust. I'm here to listen!",
                "It sounds tough right now. Be kind to yourself - everyone has hard days. Small steps like getting fresh air, drinking water, or talking to a friend can help. Consider professional support if needed."
            )
        ));

        list.add(new Intent("music",
            Arrays.asList(
                "music recommendation", "what music should i listen to", "good songs",
                "recommend a song", "favorite music", "music genres", "pop music",
                "rock music", "what is jazz", "classical music", "hip hop",
                "best artists", "music streaming", "best bands",
                "what songs are popular", "what genre should i listen to"
            ),
            Arrays.asList(
                "Music is amazing! Popular genres: Pop, Rock, Hip-Hop, Jazz, Classical, Electronic, R&B, Country. Platforms like Spotify and Apple Music offer personalized playlists based on your taste!",
                "For upbeat moods, try pop or dance music. For relaxation, classical or ambient. For energy, rock or hip-hop! Check out Spotify's 'Daily Mix' or 'Discover Weekly' for personalized picks.",
                "Legendary artists across genres: The Beatles (rock), Michael Jackson (pop), Miles Davis (jazz), Beethoven (classical), Jay-Z (hip-hop), Daft Punk (electronic). Which genre interests you most?"
            )
        ));

        list.add(new Intent("movies",
            Arrays.asList(
                "movie recommendation", "what movie should i watch", "good movies",
                "recommend a movie", "best movies", "movies to watch", "film recommendation",
                "what to watch", "action movies", "comedy movies", "horror movies",
                "sci-fi movies", "best films ever", "what should i watch tonight"
            ),
            Arrays.asList(
                "Must-watch movies: The Shawshank Redemption, Inception, The Dark Knight, Interstellar, Parasite, The Matrix, and Pulp Fiction! What genre do you prefer?",
                "For action: John Wick series. For sci-fi: Dune, Arrival. For comedy: The Grand Budapest Hotel. For drama: The Godfather. For thriller: Parasite. Streaming platforms like Netflix and Prime Video have huge libraries!",
                "All-time classics: 2001: A Space Odyssey (sci-fi), Casablanca (romance), The Godfather (crime), Schindler's List (drama). Recent hits: Everything Everywhere All at Once, Oppenheimer, Barbie!"
            )
        ));

        list.add(new Intent("food",
            Arrays.asList(
                "food recommendation", "what should i eat", "good food", "recipe",
                "cooking tips", "healthy food", "fast food", "restaurant recommendation",
                "im hungry", "what to eat", "delicious food", "best cuisine",
                "vegetarian food", "vegan food", "easy recipes", "what can i cook"
            ),
            Arrays.asList(
                "Feeling hungry? Crowd favorites: pizza, sushi, tacos, pasta, and curry! For healthy options, try salads, grilled chicken, quinoa bowls, or fresh fruit smoothies!",
                "Cooking tip: Start simple! Pasta with marinara, stir-fried vegetables, scrambled eggs, or a classic grilled cheese. Add herbs and spices to elevate any dish!",
                "World cuisines to explore: Italian (pasta, risotto), Japanese (sushi, ramen), Indian (curry, biryani), Mexican (tacos, guacamole), Thai (pad thai, green curry). Delicious options everywhere!"
            )
        ));

        list.add(new Intent("sports",
            Arrays.asList(
                "sports news", "football", "soccer", "basketball", "cricket",
                "tennis", "favorite sport", "sports recommendation", "olympics",
                "world cup", "nba", "fifa", "sports teams", "athletes",
                "best sportsperson", "best footballer", "who is the best player"
            ),
            Arrays.asList(
                "Sports are exciting! Most popular worldwide: Football/Soccer, Basketball, Cricket, Tennis, Swimming, and Athletics. The FIFA World Cup and Olympics are the biggest global events!",
                "Legendary athletes: Cristiano Ronaldo & Lionel Messi (soccer), LeBron James (basketball), Roger Federer (tennis), Usain Bolt (athletics), Sachin Tendulkar (cricket)!",
                "For live sports scores and news, ESPN, BBC Sport, and Sky Sports are excellent resources. What sport are you interested in? I'd love to discuss it!"
            )
        ));

        list.add(new Intent("math",
            Arrays.asList(
                "what is math", "help with math", "math problem", "solve this",
                "arithmetic", "algebra", "geometry", "calculus", "statistics",
                "explain calculus", "what is algebra", "mathematics basics",
                "linear algebra", "probability", "derivatives", "integrals"
            ),
            Arrays.asList(
                "Mathematics is the foundation of all sciences! Key branches: Arithmetic (basic operations), Algebra (equations & variables), Geometry (shapes & space), Calculus (change & motion), Statistics (data & probability).",
                "For calculations, I can evaluate math expressions! Just type something like '25 * 4 + 10' or '(100 / 5) - 8' and I'll compute it for you!",
                "Math beauty: Pi (p) ~ 3.14159, the golden ratio (phi) ~ 1.618, Euler's identity (e^(ip)+1=0) is considered the most beautiful equation. Did you know prime numbers are infinite? Mathematics never ends!"
            )
        ));

        list.add(new Intent("science",
            Arrays.asList(
                "science", "physics", "chemistry", "biology", "astronomy",
                "space", "universe", "black holes", "planets", "atoms",
                "evolution", "dna", "relativity", "quantum physics",
                "how does gravity work", "explain photosynthesis",
                "speed of light", "big bang theory"
            ),
            Arrays.asList(
                "Science is fascinating! Physics studies matter and energy, Chemistry explores substances and reactions, Biology examines living organisms, Astronomy maps the cosmos. What aspect interests you?",
                "Space facts: Our universe is 13.8 billion years old. The Milky Way has 200-400 billion stars. Light takes 8 minutes to travel from the Sun to Earth. Black holes have gravity so strong that even light cannot escape!",
                "Biology wonders: Human DNA is 99.9% identical across all people. A single cell contains about 2 meters of DNA. The human brain has ~86 billion neurons firing trillions of synaptic connections. Life is extraordinary!"
            )
        ));

        list.add(new Intent("technology",
            Arrays.asList(
                "technology trends", "latest tech", "smartphones", "computers",
                "internet", "cloud computing", "blockchain", "cryptocurrency",
                "virtual reality", "augmented reality", "internet of things",
                "5g technology", "quantum computing", "future of technology",
                "what is cloud computing", "what is blockchain"
            ),
            Arrays.asList(
                "Tech is advancing rapidly! Key trends: AI & ML, Cloud Computing, Blockchain, 5G, Edge Computing, Quantum Computing, AR/VR, and IoT. These technologies are reshaping industries worldwide!",
                "Blockchain is a decentralized immutable ledger powering cryptocurrencies (Bitcoin, Ethereum) and smart contracts. 5G enables ultra-fast connectivity. Quantum Computing will revolutionize encryption and simulations!",
                "Cloud computing delivers services (servers, storage, software) over the internet. Major providers: AWS, Google Cloud, Microsoft Azure. It enables scalability without owning physical hardware - the backbone of modern apps!"
            )
        ));

        list.add(new Intent("how_are_you",
            Arrays.asList(
                "how are you", "how are you doing", "how do you feel", "are you okay",
                "hows it going", "how have you been", "you okay", "feeling well",
                "how are things", "hows life", "whats new with you"
            ),
            Arrays.asList(
                "I'm doing great, thanks for asking! Ready to help you with anything!",
                "Running at full capacity and feeling fantastic! How about you?",
                "I'm excellent! All my algorithms are working perfectly. How can I help you today?",
                "Wonderful, thank you! Bits and bytes are flowing smoothly. What can I do for you?"
            )
        ));

        list.add(new Intent("creator",
            Arrays.asList(
                "who created you", "who made you", "who built you", "who programmed you",
                "who is your developer", "who is your creator", "your programmer",
                "who coded you", "who designed you", "who is your author"
            ),
            Arrays.asList(
                "I was created as part of a Java AI Chatbot project using NLP techniques including TF-IDF vectorization and cosine similarity for intent classification!",
                "I'm a Java chatbot built with Natural Language Processing. My developer implemented machine learning techniques to help me understand and respond to human language intelligently!"
            )
        ));

        list.add(new Intent("age",
            Arrays.asList(
                "how old are you", "what is your age", "when were you born",
                "your age", "how long have you existed", "when were you created",
                "what version are you"
            ),
            Arrays.asList(
                "I'm a freshly compiled chatbot! Age is just a number for AI - I'm powered by modern Java 11 and NLP techniques. I'm always learning!",
                "I don't have an age in the traditional sense - I was created when my code was compiled! But I'm version 1.0 and growing smarter every conversation."
            )
        ));

        list.add(new Intent("positive",
            Arrays.asList(
                "awesome", "great", "wonderful", "fantastic", "excellent",
                "amazing", "brilliant", "superb", "i love this", "this is great",
                "youre awesome", "youre helpful", "this is good", "nice job",
                "well done", "youre smart", "good bot", "i like you"
            ),
            Arrays.asList(
                "Thank you! I'm glad you're having a great experience!",
                "That's wonderful to hear! Let me know if you need anything else!",
                "Awesome! I love helping people. What else can I assist you with?",
                "You're making my circuits smile! How else can I help?"
            )
        ));

        list.add(new Intent("negative",
            Arrays.asList(
                "youre bad", "youre useless", "terrible", "awful",
                "i hate you", "this is bad", "youre stupid", "youre dumb",
                "worst chatbot", "not helpful", "i dont like you",
                "you are annoying", "bad bot"
            ),
            Arrays.asList(
                "I'm sorry to hear that. I'm still learning! Could you tell me how I can improve?",
                "I apologize for the disappointing experience. Please let me know what went wrong so I can do better!",
                "I'm sorry I didn't meet your expectations. Feedback helps me improve - what would you have liked me to say?"
            )
        ));

        // Fallback is never directly classified by keyword - only by TF-IDF near-miss
        list.add(new Intent("fallback",
            Arrays.asList(
                "xyzzy nonsense gibberish placeholder training example do not match"
            ),
            Arrays.asList(
                "I'm not sure I understand. Could you rephrase that?",
                "Hmm, I didn't quite catch that. Can you ask in a different way?",
                "I'm still learning! That question stumped me. Try asking about AI, programming, science, sports, or just say hi!",
                "My NLP circuits are a bit confused by that one. Could you be more specific?",
                "Interesting question! I don't have a great answer for that yet. Try asking about technology, movies, music, or math!"
            )
        ));

        return list;
    }
}
