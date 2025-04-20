# StoreIt: In-Memory Key-Value Store

StoreIt is a Redis-inspired, multithreaded in-memory key-value store written in Java.

It supports core operations (`SET`, `GET`, `DELETE`), TTL expiration, LRU eviction, snapshot persistence, and multi-client communication over sockets.

---

## Features

- **SET / GET / DELETE** operations
- **TTL expiration** for keys (e.g. `SET key value EX 10`)
- **LRU eviction** when store reaches max size
- **Persistence**: Snapshot saved to disk on shutdown + every 10s
- **Thread-safe** with support for multiple clients
- **Socket server** handles raw commands over TCP
- **Pub/Sub** support via `SUBSCRIBE` and `PUBLISH` commands
- **Java CLI Client** to interact with StoreIt in the terminal

---

## Project Structure

storeit/
├── src/
│   ├── main/
│   │   └── java/com/storeit/
│   │       ├── store/          # In-memory store logic (GET, SET, TTL, LRU)
│   │       ├── pubsub/         # Pub/Sub system
│   │       ├── server/         # SocketServer
│   │       └── client/         # CLI Client
│   └── test/                   # Unit tests (optional)
├── out/                        # Compiled class files
└── README.md

---

## How to Run

### Compile

```bash
javac -d out src/main/java/com/storeit/**/*.java
```

### Start the Server

```bash
java -cp out com.storeit.server.SocketServer
```

You should see:

```bash
StoreIt Server started on port 6379
```

### Run the CLI Client

In a new terminal:

```bash
java -cp out com.storeit.client.StoreItClient
```

You should see:

```bash
StoreIt Server started on port 6379
```

### Example Commands

Inside the client:

```bash
SET foo bar
GET foo
DELETE foo

SET hello world EX 5     # Set with expiration (TTL)
GET hello

SUBSCRIBE chat           # Listen to a channel
```

In a second client:

```bash
PUBLISH chat hello-you
```

you'll see it broadcasted in the first client



