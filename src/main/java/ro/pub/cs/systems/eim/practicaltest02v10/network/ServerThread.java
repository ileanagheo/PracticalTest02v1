package ro.pub.cs.systems.eim.practicaltest02v10;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;

public class ServerThread extends Thread {

    private ServerSocket serverSocket;
    private int port;

    public ServerThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Log.i(Constants.TAG, "Server started on port: " + port);
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                new CommunicationThread(clientSocket).start();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopThread() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error stopping server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class CommunicationThread extends Thread {

    private Socket clientSocket;

    public CommunicationThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            String query = bufferedReader.readLine();

            if (query != null && !query.isEmpty()) {
                URL url = new URL("https://www.google.com/complete/search?client=chrome&q=" + query);
                URLConnection urlConnection = url.openConnection();
                BufferedReader urlReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = urlReader.readLine()) != null) {
                    result.append(line);
                }

                String response = parseAutocompleteSuggestions(result.toString());
                printWriter.println(response);
                printWriter.flush();
            }

            clientSocket.close();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error in communication thread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String parseAutocompleteSuggestions(String jsonResponse) {
        // Simplified parsing example
        StringBuilder suggestions = new StringBuilder();
        int startIndex = jsonResponse.indexOf("[[\"") + 3;
        int endIndex = jsonResponse.indexOf("\"]]");
        if (startIndex >= 0 && endIndex >= 0 && startIndex < endIndex) {
            String suggestionsPart = jsonResponse.substring(startIndex, endIndex);
            suggestions.append(suggestionsPart.replace("\",\"", ", "));
        }
        return suggestions.toString();
    }
}
