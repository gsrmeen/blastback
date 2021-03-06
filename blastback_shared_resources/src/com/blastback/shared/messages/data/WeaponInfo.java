/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blastback.shared.messages.data;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Marcin
 */
public class WeaponInfo
{

    private final int _damage;
    private final float _speed;

    private int _ammoCapacity;
    private int _inMagazine;

    private int _burst = 1;
    private int _burstCounter;

    private float _bulletCooldown; //in ms
    private float _reloadTime; // in miliseconds       

    private Timer _reloading;
    private String _shootSound;
    private String _reloadSound;
    private String _model;

    public WeaponInfo()
    {
        _damage = 20;
        _speed = 20;
    }

    public WeaponInfo(int dmg, float speed, float bulletFrequency)
    {
        _damage = dmg;
        _speed = speed;
        _bulletCooldown = bulletFrequency;
    }

    public WeaponInfo(int dmg, float speed, float bulletFrequency, int ammoCap, float reloadTime, int burst, String shootSound, String reloadSound, String model)
    {
        _damage = dmg;
        _speed = speed;
        _ammoCapacity = ammoCap;
        _bulletCooldown = bulletFrequency;
        _inMagazine = ammoCap;
        _reloadTime = reloadTime;
        _burst = burst;
        _burstCounter = burst;
        _shootSound = shootSound;
        _reloadSound = reloadSound;
        _model = model;
    }

    public int getDamage()
    {
        return _damage;
    }

    public float getSpeed()
    {
        return _speed;
    }

    public int getAmmoCapacity()
    {
        return _ammoCapacity;
    }

    public int getCurrentAmmo()
    {
        return _inMagazine;
    }

    public float getReloadTime()
    {
        return _reloadTime;
    }

    public float getBulletCooldown()
    {
        return _bulletCooldown;
    }

    public String getShootSound()
    {
        return _shootSound;
    }

    public String getReloadSound()
    {
        return _reloadSound;
    }

    public String getModel()
    {
        return _model;
    }

    public boolean shoot(boolean keyPressed)
    {
        //reset bursts when the mouse key is lifted
        if (!keyPressed)
        {

            _burstCounter = _burst;
            return false;
        }

        //if eligible to shoot
        if (_reloading == null && _burstCounter > 0)
        {

            if (_inMagazine > 0)
            {
                _burstCounter--;
                _inMagazine--;

                return true;
            } else
            {
                reload();
                return false;
            }

        }
        return false;
    }

    public boolean reload()
    {
        if (_reloading == null)
        {
            _reloading = new Timer();
            TimerTask tt = new TimerTask()
            {
                @Override
                public void run()
                {
                    resetAmmo();
                }
            };
            _reloading.schedule(tt, (long) _reloadTime);
            return true;
        }
        return false;
    }

    private void resetAmmo()
    {
        _inMagazine = _ammoCapacity;
        _burstCounter = _burst;
        _reloading.cancel();
        _reloading = null;
    }

}
