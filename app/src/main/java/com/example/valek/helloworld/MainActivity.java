package com.example.valek.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import android.os.StrictMode;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {
    private final String SERVER = "wss://chatclick.ru:8101/Message/?id=79824935588&uuid=76KaRRrpiY1kcalaL1PIL7yn2DlIWhH88LyAzxUQSt5bxHhslcKbXnsUCeOWVUq1xcyD4NYUL3x746D8&deviceid=1861febabe470baf&tz=+05:00";
    private final int TIMEOUT = 5000;
    private List<User> usersList = new ArrayList<>();

    Button btnSend;
    ListView listView;
    BoxAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnSend = (Button) findViewById(R.id.btnSend);
        listView = (ListView) findViewById(R.id.lvMain);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        boxAdapter = new BoxAdapter(this, usersList);
        listView.setAdapter(boxAdapter);

        View.OnClickListener oclBtnRequest = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WebSocket ws = connect();
                    ws.sendText("{\n" +
                            "    \"mid\": \"6dcb6bb4-344c-4687-9d1f-87d54f78a8f0\",\n" +
                            "    \"action\": \"user.list\",\n" +
                            "    \"options\": {\n" +
                            "        \"locale\": \"ru\",\n" +
                            "        \"userlist\": [],\n" +
                            "        \"status\": -1,\n" +
                            "        \"isfriend\": -1,\n" +
                            "        \"country\": 0,\n" +
                            "        \"city\": 0,\n" +
                            "        \"sex\": \"\",\n" +
                            "        \"minage\": 0,\n" +
                            "        \"maxage\": 0,\n" +
                            "        \"mask\": \"\",\n" +
                            "        \"lat1\": 0,\n" +
                            "        \"lng1\": 0,\n" +
                            "        \"lat2\": 0,\n" +
                            "        \"lng2\": 0\n" +
                            "    }\n" +
                            "}\n");

                    Thread.sleep(1000);

                } catch (IOException e) {
                } catch (WebSocketException ex) {
                } catch (InterruptedException exx) {

                }

                if (usersList != null) {
                    boxAdapter = new BoxAdapter(MainActivity.this, usersList);
                    listView.setAdapter(boxAdapter);
                }
            }
        };
        btnSend.setOnClickListener(oclBtnRequest);
    }

    private void fillUsersList(String message) {
        JSONObject jsonObject = null;
        if (message != null) {
            try {
                jsonObject = new JSONObject(message);
                JSONArray result = jsonObject.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jsonUser = result.getJSONObject(i);
                    User user = new User();
                    user.setLogin(jsonUser.getString("login"));
                    user.setAvatar(jsonUser.getString("avatar"));
                    usersList.add(user);
                }
            } catch (JSONException e) {
            }

        }
    }

    private WebSocket connect() throws IOException, WebSocketException {
        SSLContext context = null;
        try {
            context = NaiveSSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
        }

        return new WebSocketFactory()
                .setSSLContext(context)
                .setConnectionTimeout(TIMEOUT)
                .createSocket(SERVER)
                .addListener(new WebSocketAdapter() {
                    // A text message arrived from the server.
                    public void onTextMessage(WebSocket websocket, String message) {

                        if (message.contains("\"action\":\"user.list\",\"status\":1000,")) {
                            fillUsersList(message);
                        }
                    }
                })
                .connect();
    }


}
