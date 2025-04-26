# StoreIt

Redis-inspired, multithreaded in-memory key-value store.

Supports `SET`, `GET` & `DELETE` operations, TTL expiration, LRU eviction, snapshot persistence, and multi-client communication over sockets.

---

## How to Run

### Compile

```bash
javac -d out src/main/java/com/storeit/**/*.java
```

### Start the Server

```bash
java -cp out com.storeit.server.SocketServer
StoreIt Server started on port 6379
```

### Run the CLI Client

In a new terminal:

```bash
java -cp out com.storeit.client.StoreItClient
StoreIt Server started on port 6379
```

### Example Commands

Inside the client:

```bash
SET key value
GET key
DELETE key

SET key value EX 5
GET key

SUBSCRIBE chat
```

In a second client:

```bash
PUBLISH chat hey
```

