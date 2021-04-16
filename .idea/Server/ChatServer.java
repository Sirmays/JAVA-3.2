package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    private final AuthenticationService authenticationService;
    private final Set<ClientHandler> loggedClients;

    public ChatServer() {
        authenticationService = new AuthenticationService();
        loggedClients = new HashSet<>();

        try {
            ServerSocket socket = new ServerSocket(8080);
            System.out.println("Server is running up...");
            while (true) {
                System.out.println("Waiting for a connection...");
                Socket clientSocket = socket.accept();
                new ClientHandler(clientSocket, this);
            }
        } catch (IOException e) {
            throw new ChatServerException("Something went wrong", e);
        }
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void broadcast(String message) {
        for (ClientHandler clientHandler : loggedClients) {
            clientHandler.sendMessage(message);
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        loggedClients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        loggedClients.remove(clientHandler);
    }

    public boolean isLoggedIn(String name) {

        return loggedClients.stream()
                .filter(client -> client.getName().equals(name))
                .findFirst()
                .isPresent();
    }

    public synchronized void directMessage(String massage, String name) {
        Iterator<ClientHandler> iterator = loggedClients.iterator();
        while (iterator.hasNext()) {
            ClientHandler handler = iterator.next();
            if (handler.getName().equals(name)) {
                handler.sendMessage(massage);
            }
        }
    }
}