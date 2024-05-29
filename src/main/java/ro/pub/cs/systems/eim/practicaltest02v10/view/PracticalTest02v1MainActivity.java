package ro.pub.cs.systems.eim.practicaltest02v10.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ro.pub.cs.systems.eim.practicaltest02v10.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02v10.R;
import ro.pub.cs.systems.eim.practicaltest02v10.ServerThread;

public class PracticalTest02v1MainActivity extends AppCompatActivity {

    private EditText serverPortEditText, clientAddressEditText, clientPortEditText, queryEditText;
    private TextView responseTextView;
    private Button startServerButton, sendRequestButton;
    private ServerThread serverThread;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v1_main);

        serverPortEditText = findViewById(R.id.server_port);
        clientAddressEditText = findViewById(R.id.client_address);
        clientPortEditText = findViewById(R.id.client_port);
        queryEditText = findViewById(R.id.query_text);
        responseTextView = findViewById(R.id.response_view);
        startServerButton = findViewById(R.id.start_server);
        sendRequestButton = findViewById(R.id.send_request);

        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverPort = serverPortEditText.getText().toString();
                if (serverPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a server port", Toast.LENGTH_SHORT).show();
                    return;
                }
                serverThread = new ServerThread(Integer.parseInt(serverPort));
                serverThread.start();
            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientAddress = clientAddressEditText.getText().toString();
                String clientPort = clientPortEditText.getText().toString();
                String query = queryEditText.getText().toString();
                if (clientAddress.isEmpty() || clientPort.isEmpty() || query.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), query, responseTextView);
                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
