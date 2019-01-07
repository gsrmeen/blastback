/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blastback.appstates;

import com.blastback.GameClient;
import com.blastback.controls.BulletControl;
import com.blastback.shared.messages.data.ShootEventArgs;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Eryk
 */
public class BulletFactoryAppState extends BaseAppState
{
    private static GameClient _app;
    private static AssetManager _assetManager;
    private static AudioNode Sound;

    

    @Override
    protected void initialize(Application app)
    {
        _app = (GameClient)app;
        _assetManager = _app.getAssetManager();
    }

    @Override
    protected void cleanup(Application app)
    {
    }

    @Override
    protected void onEnable()
    {
         
    }

    @Override
    protected void onDisable()
    {
        
    }
    
    public static BulletControl createBullet(Node root, PhysicsSpace space, ShootEventArgs eventArgs, boolean dummy)
    {
        Spatial bullet = _assetManager.loadModel("Models/Bullet.j3o");
        BulletControl bulletControl = new BulletControl(eventArgs.getWeaponInfo().getDamage(), eventArgs.getWeaponInfo().getSpeed(), 0.1f, dummy);
        bullet.addControl(bulletControl);
        bulletControl.setPhysicsLocation(eventArgs.getShotPosition());
        bulletControl.setPhysicsRotation(eventArgs.getShotRotation());
        bullet.setLocalTranslation(eventArgs.getShotPosition());
        bullet.setLocalRotation(eventArgs.getShotRotation());
        
        root.attachChild(bullet);
        space.add(bulletControl);
        space.addCollisionListener(bulletControl);
        bulletControl.setGravity(Vector3f.ZERO);
        bulletControl.setCcdMotionThreshold(0.02f);
        bulletControl.setCcdSweptSphereRadius(0.12f);
        
        ShootSound(eventArgs.getWeaponInfo().getSound());
        
        return bulletControl;
    }
    
    private static void ShootSound(String sound) {
        Sound = new AudioNode(_assetManager, sound , AudioData.DataType.Stream);
        Sound.setPositional(false);
        Sound.setVolume(5);
        _app.getRootNode().attachChild(Sound);
        Sound.play();
        _app.getRootNode().detachChild(Sound);
    }
       
    
}
