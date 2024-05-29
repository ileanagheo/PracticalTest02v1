package ro.pub.cs.systems.eim.practicaltest02v10;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String query;
    private TextView responseTextView;

    public ClientThread(String address, int port, String query, TextView responseTextView) {
        this.address = address;
        this.port = port;
        this.query = query;
        this.responseTextView = responseTextView;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            printWriter.println(query);
            printWriter.flush();

            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }

            responseTextView.post(new Runnable() {
                @Override
                public void run() {
                    responseTextView.setText(response.toString());
                }
            });

            socket.close();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error in client thread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
