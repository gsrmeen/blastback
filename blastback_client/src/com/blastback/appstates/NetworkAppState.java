/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blastback.appstates;

import com.blastback.GameClient;
import com.blastback.shared.networking.data.ClientCoordinatesMessageData;
import com.blastback.listeners.ClientListener;
import com.blastback.shared.messages.HelloMessage;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;


public class NetworkAppState extends BaseAppState
{

    private GameClient _app;
    private PlayerAppState _playerAppState;
    private SimulationAppState _simulationAppState;
    
    private Client _clientInstance;
    private Timer _messageTimer;
    private final int _timerTick = 50;
    private final int _port;
    private final String _ip;

    public NetworkAppState()
    {
        _ip = "localhost";
        _port = 7777;
    }

    public NetworkAppState(String ip, int port)
    {
        _ip = ip;
        _port = port;
    }

    @Override
    protected void initialize(Application app)
    {
        _app = (GameClient) app;
        _playerAppState = _app.getStateManager().getState(PlayerAppState.class);
        registerMessages();
    }

    @Override
    protected void onEnable()
    {
        initConnection();
    }

    @Override
    protected void onDisable()
    {
        if (_clientInstance != null)
        {
            _messageTimer.cancel();
            _clientInstance.close();
        }
    }

    @Override
    protected void cleanup(Application app)
    {
    }

    private void initConnection()
    {
        try
        {
            Log("Creating Client on port " + _port);

            
            _clientInstance = Network.connectToServer(_ip, _port);
            _clientInstance.addMessageListener(new ClientListener(), HelloMessage.class);
            _clientInstance.start();

            
            initTimer();

            Log("Server created succesfully");
        } catch (IOException ex)
        {
            Log(ex.getMessage());
        }
    }

    private void initTimer()
    {
        _messageTimer = new Timer();
        TimerTask tt = new TimerTask()
        {
            @Override
            public void run()
            {
                sendPlayerInfo();
            }
        };

        _messageTimer.scheduleAtFixedRate(tt, 0, _timerTick);
    }

    private void sendPlayerInfo()
    {
        if (_clientInstance.isConnected())
        {
            Vector3f coordsV3f = _playerAppState.getPlayerPosition();
            
            Gson gson = new Gson();
            ClientCoordinatesMessageData DataForJson = 
                    new ClientCoordinatesMessageData(coordsV3f.x, coordsV3f.y, coordsV3f.z);
            String serialized = gson.toJson(DataForJson);
            
            Message data = new HelloMessage(serialized);
            _clientInstance.send(data);
        }
    }

    private void Log(String msg)
    {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\t[LOG] {0}", msg);
    }

    private void registerMessages()
    {
        Serializer.registerClass(HelloMessage.class);
    }
}