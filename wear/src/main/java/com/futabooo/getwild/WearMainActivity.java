package com.futabooo.getwild;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WearMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wear_activity_main);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button button = (Button) stub.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GoogleApiClient client = new GoogleApiClient.Builder(getApplicationContext()).addApi(Wearable.API).build();
                                client.blockingConnect(100, TimeUnit.MILLISECONDS);
                                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();
                                List<Node> nodes = result.getNodes();
                                if (nodes.size() > 0) {
                                    client.blockingConnect(100, TimeUnit.MILLISECONDS);
                                    Wearable.MessageApi.sendMessage(client, nodes.get(0).getId(), "/get-wild", null);
                                    client.disconnect();
                                }
                                client.disconnect();
                            }
                        }).start();
                    }
                });
            }
        });
    }
}

