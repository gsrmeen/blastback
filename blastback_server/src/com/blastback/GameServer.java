package com.blastback;

import com.blastback.appstates.GameMatchAppState;
import com.blastback.appstates.ServerNetworkAppState;
import com.blastback.appstates.SimulationDataAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

public class GameServer extends SimpleApplication
{

    public GameServer()
    {
        super(new GameMatchAppState(180),
              new ServerNetworkAppState(),
              new SimulationDataAppState());
    }

    public static void main(String[] args)
    {
        GameServer app = new GameServer();
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp()
    {
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        // reacting to messages...
        
    }

    @Override
    public void destroy()
    {
        // disposing...
        super.destroy();
    }

}
