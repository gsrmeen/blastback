/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blastback.shared.observer;

/**
 *
 * @author Eryk
 * @param <T>
 */
public interface BlastbackEventListener<T>
{
    public void invoke (T e);
}
