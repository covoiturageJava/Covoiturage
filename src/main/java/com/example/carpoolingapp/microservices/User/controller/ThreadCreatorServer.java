package com.example.carpoolingapp.microservices.User.controller;

import com.example.carpoolingapp.model.DatabaseInitializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCreatorServer extends WebSocketServer {

    private final ThreadPoolExecutor threadPool;
    private final ConcurrentHashMap<Integer, ScheduledFuture<?>> threadTimeouts;
    private final ConcurrentHashMap<Integer, ThreadData> threadQueue;
    private final AtomicInteger threadIdGenerator;

    public ThreadCreatorServer(int port) {
        super(new InetSocketAddress(port));
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        this.threadTimeouts = new ConcurrentHashMap<>();
        this.threadQueue = new ConcurrentHashMap<>();
        this.threadIdGenerator = new AtomicInteger(0);
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Nouvelle connexion : " + conn.getRemoteSocketAddress());
        conn.send("Bienvenue sur le serveur ThreadCreator !");
    }
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            if (message.contains("\"action\":\"accept\"")) {
                handleAcceptThread(conn, message);
            }
            else if (message.contains("\"action\":\"remove_thread\"")) {
                handleRemoveThread(conn, message);
            }
            else if (message.equalsIgnoreCase("status")) {
                sendStatus(conn);
            } else {
                createNewThread(conn, message);
            }
        } catch (Exception e) {
            conn.send("Erreur de traitement du message : " + e.getMessage());
        }
    }
    private void handleRemoveThread(WebSocket conn, String message) {
        try {
            JsonObject jsonMessage = JsonParser.parseString(message).getAsJsonObject();
            if (!jsonMessage.has("threadId") || jsonMessage.get("threadId").isJsonNull()) {
                conn.send("Erreur : threadId est manquant ou null.");
                return;
            }
            int threadId = jsonMessage.get("threadId").getAsInt();
            boolean removed = removeThread(threadId);
            if (removed) {
                conn.send("Thread ID " + threadId + " supprimé avec succès de la file d'attente.");
            } else {
                conn.send("Erreur : Thread ID " + threadId + " n'existe pas ou a déjà expiré.");
            }
        } catch (Exception e) {
            conn.send("Erreur lors de la suppression du thread : " + e.getMessage());
        }
    }
    private boolean removeThread(int threadId) {
        if (threadQueue.containsKey(threadId)) {
            threadQueue.remove(threadId);
            if (threadTimeouts.containsKey(threadId)) {
                threadTimeouts.get(threadId).cancel(false);
                threadTimeouts.remove(threadId);
            }
            return true;
        }
        return false;
    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connexion fermée : " + conn.getRemoteSocketAddress() + " Raison : " + reason);
    }
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Erreur détectée : " + ex.getMessage());
    }
    @Override
    public void onStart() {
        System.out.println("Serveur ThreadCreator démarré sur le port " + getPort());
    }
    private void createNewThread(WebSocket conn, String message) {
        try {
            JsonObject jsonMessage = JsonParser.parseString(message).getAsJsonObject();
            int threadId = threadIdGenerator.incrementAndGet();
            int trajetId = jsonMessage.get("trajetId").getAsInt();
            int userId = jsonMessage.has("userId") && !jsonMessage.get("userId").isJsonNull()
                    ? jsonMessage.get("userId").getAsInt()
                    : 0;
            double departLat = jsonMessage.get("departLat").getAsDouble();
            double departLng = jsonMessage.get("departLng").getAsDouble();
            double arriveeLat = jsonMessage.get("arriveeLat").getAsDouble();
            double arriveeLng = jsonMessage.get("arriveeLng").getAsDouble();
            String distance = jsonMessage.get("distance").getAsString();
            String type ="Standard";
            ThreadData threadData = new ThreadData(
                    userId,type,trajetId, departLat, departLng, arriveeLat, arriveeLng, distance
            );
            threadQueue.put(threadId, threadData);
            ScheduledFuture<?> timeout = Executors.newScheduledThreadPool(1).schedule(() -> {
                if (threadQueue.remove(threadId) != null) {
                    conn.send("Thread ID " + threadId + " lié au Trajet ID " + trajetId + " a expiré (non accepté dans les 3 minutes).");
                }
            }, 3, TimeUnit.MINUTES);
            threadTimeouts.put(threadId, timeout);
            System.out.println("Nouveau thread créé : ID=" + threadId +
                    ", trajetId=" + trajetId +
                    ", UserID=" + userId +
                    ", Depart=(" + departLat + ", " + departLng +
                    "), Arrivee=(" + arriveeLat + ", " + arriveeLng +
                    "), Distance=" + distance);
            conn.send("Type:Standard, Thread ID " + threadId + " lié au Trajet ID " + trajetId +
                    " ajouté à la file d'attente. Acceptez-le avec 'accept:" + threadId + "'.");
        } catch (Exception e) {
            conn.send("Erreur dans la création du thread : " + e.getMessage());
        }
    }
    private void createNewRouteThreads(WebSocket conn, ThreadData threadData, String driverId, int userId,int id) {
        try {
            int threadId = threadIdGenerator.incrementAndGet();
            int trajetId = threadData.trajetId;
            double departLat = threadData.departLat;
            double departLng = threadData.departLng;
            double arriveeLat = threadData.arriveeLat;
            double arriveeLng = threadData.arriveeLng;
            String distance = threadData.distance;
            Trajet trajetthreads= new Trajet(departLat,"Route1",trajetId,departLng,arriveeLat,arriveeLng,distance,
                    driverId,userId
            );
            threadQueue.put(threadId, trajetthreads);
            ScheduledFuture<?> timeout = Executors.newScheduledThreadPool(1).schedule(() -> {
                if (threadQueue.remove(threadId) != null) {
                    conn.send("Thread ID " + threadId + " lié au Trajet ID " + trajetId + " a expiré (non accepté dans les 3 minutes).");
                }
            }, 60, TimeUnit.MINUTES);
            threadTimeouts.put(threadId, timeout);
            System.out.println("Nouveau Trajet créé : ID=" + threadId +
                    ", trajetId=" + trajetId +
                    ", UserID=" + userId +", driverId="+driverId+
                    ", Depart=(" + departLat + ", " + departLng +
                    "), Arrivee=(" + arriveeLat + ", " + arriveeLng +
                    "), Distance=" + distance);
            conn.send("Type:Route, Thread ID " + threadId + " lié au Trajet ID " + trajetId +
                    " ajouté à la file d'attente. Acceptez-le avec 'accept:" + threadId + "'.");
        } catch (Exception e) {
            conn.send("Erreur dans la création du thread : " + e.getMessage());
        }
    }
    private void handleAcceptThread(WebSocket conn, String message) {
        try {
            JsonObject jsonMessage = JsonParser.parseString(message).getAsJsonObject();
            if (!jsonMessage.has("threadId") || jsonMessage.get("threadId").isJsonNull()) {
                conn.send("Erreur : threadId est manquant ou null.");
                return;
            }
            int threadId = jsonMessage.get("threadId").getAsInt();
            if (!jsonMessage.has("trajetId") || jsonMessage.get("trajetId").isJsonNull()) {
                conn.send("Erreur : idTrajet est manquant ou null.");
                return;
            }
            int idTrajet = jsonMessage.get("trajetId").getAsInt();
            if (!jsonMessage.has("driverId") || jsonMessage.get("driverId").isJsonNull()) {
                conn.send("Erreur : driverId est manquant ou null.");
                return;
            }
            String driverId = jsonMessage.get("driverId").getAsString();
            int userId = jsonMessage.has("userId") && !jsonMessage.get("userId").isJsonNull()
                    ? jsonMessage.get("userId").getAsInt()
                    : 1;
            if (threadQueue.containsKey(threadId)) {
                ThreadData threadData = threadQueue.remove(threadId);
                threadTimeouts.get(threadId).cancel(false);
                threadTimeouts.remove(threadId);
                boolean inserted = saveTrajetToDatabase(threadData, driverId, userId, idTrajet);
                if (inserted) {
                    conn.send("Trajet enregistré avec succès dans la base de données.");
                    createNewRouteThreads(conn,threadData, driverId, userId, idTrajet);
                } else {
                    conn.send("Erreur lors de l'enregistrement du trajet dans la base de données.");
                }
                threadPool.execute(() -> processThread(threadId, threadData));
                conn.send("Thread ID " + threadId + " accepté et démarré.");
            } else {
                conn.send("Thread ID " + threadId + " n'existe pas ou a expiré.");
            }
        } catch (Exception e) {
            conn.send("Erreur lors de l'acceptation du thread : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processThread(int threadId, ThreadData threadData) {
        try {
            System.out.println("Thread ID " + threadId + " en cours de traitement...");
            Thread.sleep(300_000); // Simule un traitement
            System.out.println("Thread ID " + threadId + " terminé !");
        } catch (InterruptedException e) {
            System.err.println("Erreur dans le thread ID " + threadId + ": " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void sendStatus(WebSocket conn) {
        if (threadQueue.isEmpty()) {
            conn.send("Aucun thread en attente.");
            System.out.println("Aucun thread en attente.");
            return;
        }
        StringBuilder status = new StringBuilder("Threads en attente : [");
        for (var entry : threadQueue.entrySet()) {
            Integer threadId = entry.getKey();
            ThreadData data = entry.getValue();
            status.append("{\"threadId\":").append(threadId)
                    .append(",\"Type\":").append(data.type)
                    .append(",\"trajetId\":").append(data.trajetId)
                    .append(",\"userId\":").append(data.userId)
                    .append(",\"departLat\":").append(data.departLat)
                    .append(",\"departLng\":").append(data.departLng)
                    .append(",\"arriveeLat\":").append(data.arriveeLat)
                    .append(",\"arriveeLng\":").append(data.arriveeLng)
                    .append(",\"distance\":\"").append(data.distance).append("\"},");
        }
        status.deleteCharAt(status.length() - 1); // Supprime la dernière virgule
        status.append("]");
        conn.send(status.toString());
        System.out.println(status);
    }
    private boolean saveTrajetToDatabase(ThreadData threadData, String driverId, int userId,int id) throws SQLException {
        Connection connection = DatabaseInitializer.getConnection();
        DatabaseInitializer.selectDatabase(connection);
        String insertQuery = "INSERT INTO trajet (id_trajet , id_user, id_driver, distance, latitudedepart, longitudedepart, " +
                "latitudefin, longitudefin, etat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1,id );
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, driverId);
            preparedStatement.setString(4, threadData.distance);
            preparedStatement.setDouble(5, threadData.departLat);
            preparedStatement.setDouble(6, threadData.departLng);
            preparedStatement.setDouble(7, threadData.arriveeLat);
            preparedStatement.setDouble(8, threadData.arriveeLng);
            preparedStatement.setInt(9, 2); // Etat initial
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du trajet dans la base : " + e.getMessage());
            return false;
        }
    }
    private static class Trajet extends ThreadData {
        private  double departLat;
        private  double departLng;
        private  double arriveeLat;
        private  double arriveeLng;
        private  String distance;
        private  String driverId;
        private  int userId;
        private  int trajetId;
        public Trajet(double departLat,String type, int trajetId, double departLng, double arriveeLat, double arriveeLng, String distance,
                      String driverId, int userId) {
            super(userId,type, trajetId,  departLat,  departLng,  arriveeLat,  arriveeLng,  distance);
            this.driverId = driverId;

        }
        @Override
        public String toString() {
            return "Trajet{" +
                    "departLat=" + departLat +
                    ", departLng=" + departLng +
                    ", arriveeLat=" + arriveeLat +
                    ", arriveeLng=" + arriveeLng +
                    ", distance='" + distance + '\'' +
                    ", driverId='" + driverId + '\'' +
                    ", userId=" + userId +
                    '}';
        }
    }
    private static class ThreadData {
        private final int userId;
        private final String type;
        private final int trajetId;
        private final double departLat;
        private final double departLng;
        private final double arriveeLat;
        private final double arriveeLng;
        private final String distance;

        public ThreadData(int userId, String type, int trajetId, double departLat, double departLng, double arriveeLat, double arriveeLng, String distance) {
            this.userId = userId;
            this.type = type;
            this.trajetId = trajetId;
            this.departLat = departLat;
            this.departLng = departLng;
            this.arriveeLat = arriveeLat;
            this.arriveeLng = arriveeLng;
            this.distance = distance;
        }
    }
    public static void main(String[] args) {
        ThreadCreatorServer server = new ThreadCreatorServer(8080);
        server.start();
    }
}
