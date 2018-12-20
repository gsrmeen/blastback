/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blastback.listeners;

import com.blastback.shared.networking.data.ClientCoordinatesMessageData;
import com.blastback.shared.messages.HelloMessage;
import com.google.gson.Gson;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcin
 */
public class ServerListener implements MessageListener<HostedConnection>
{

    @Override
    public void messageReceived(HostedConnection source, Message message)
    {
        if (message instanceof HelloMessage)
        {
            Gson gson = new Gson();
            HelloMessage helloMessage = (HelloMessage) message;
            String data = helloMessage.getContent();

            ClientCoordinatesMessageData personInstance = gson.fromJson(data, ClientCoordinatesMessageData.class);
            
            System.out.println("Server received '" + personInstance.getCoordinates() + "' from client #" + source.getId());

        }
    }
    
    private void Log(String msg)
    {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\t[LOG] {0}", msg);
    }
    
}
