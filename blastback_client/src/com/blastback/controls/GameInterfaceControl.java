package com.blastback.controls;

import com.blastback.shared.messages.data.PlayerStateInfo;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GameInterfaceControl extends AbstractControl
{

    private final Nifty _niftyInstance;
    private Element _scoreboardPopup;
    private Element _connectionLostPopup;

    private final DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("HH:mm");

    public GameInterfaceControl(Nifty niftyInstance)
    {
        this._niftyInstance = niftyInstance;
    }

    public void displayScoreboard(boolean value)
    {
        if (_scoreboardPopup == null)
        {
            _scoreboardPopup = _niftyInstance.createPopup("popup_scoreboard");
        }

        if (value)
        {
            _niftyInstance.showPopup(_niftyInstance.getCurrentScreen(), _scoreboardPopup.getId(), null);
        } else
        {
            _niftyInstance.closePopup(_scoreboardPopup.getId());
        }
    }

    public void displayConnectionLostNotification(boolean value)
    {
        if (_connectionLostPopup == null)
        {
           _connectionLostPopup = _niftyInstance.createPopup("popup_connection_lost");
        }
        
        
        if (value)
        {
            _niftyInstance.showPopup(_niftyInstance.getCurrentScreen(), _connectionLostPopup.getId(), null);
        } else
        {
            _niftyInstance.closePopup(_connectionLostPopup.getId());
        }
    }

    public void updateScoreboard(List<PlayerStateInfo> playerStates)
    {
        int scoreboardCapacity = 10;
        int playersToDisplay = Math.min(playerStates.size(), scoreboardCapacity);
        for (int i = 1; i <= scoreboardCapacity; i++)
        {
            String name = "";
            String kills = "";
            String deaths = "";

            if (i <= playersToDisplay)
            {
                PlayerStateInfo info = playerStates.get(i - 1);
                name = info.getIdentityData().getUsername();
                kills = Integer.toString(info.getMatchStats().getScore());
                deaths = Integer.toString(info.getMatchStats().getDeaths());
            }

            Element nameLabel = _niftyInstance.getCurrentScreen().findElementById("text_pl" + i + "_name");
            Element killsLabel = _niftyInstance.getCurrentScreen().findElementById("text_pl" + i + "_kills");
            Element deathsLabel = _niftyInstance.getCurrentScreen().findElementById("text_pl" + i + "_deaths");

            if (nameLabel != null && killsLabel != null && deathsLabel != null)
            {
                nameLabel.getRenderer(TextRenderer.class).setText(name);
                killsLabel.getRenderer(TextRenderer.class).setText(kills);
                deathsLabel.getRenderer(TextRenderer.class).setText(deaths);
            }
        }
    }

    /**
     * Add text notifying user, that there was a kill (only in hud-screen).
     *
     * @param killer nick of player who killed
     * @param killed nick of player who was killed
     */
    public void displayKillEvent(String killer, String killed)
    {
        ListBox listBox = _niftyInstance.getCurrentScreen().findNiftyControl("listbox_kill_messages", ListBox.class);
        if (listBox != null)
        {
            int maxItems = listBox.getDisplayItemCount();
            if (listBox.getItems().size() == maxItems)
            {
                listBox.removeItemByIndex(4);
            }

            String killMessage = "[" + LocalDateTime.now().format(_formatter) + "] " + killed + " was slain by " + killer;
            listBox.insertItem(killMessage, 0);
        }
    }

    public void displayRoundStarted()
    {
        ListBox listBox = _niftyInstance.getCurrentScreen().findNiftyControl("listbox_kill_messages", ListBox.class);
        if (listBox != null)
        {
            int maxItems = listBox.getDisplayItemCount();
            if (listBox.getItems().size() == maxItems)
            {
                listBox.removeItemByIndex(4);
            }

            String roundMessage = "[" + LocalDateTime.now().format(_formatter) + "] Round started" ;
            listBox.insertItem(roundMessage, 0);
        }
    }
    
    public void displayRoundEnded(String winner)
    {
        ListBox listBox = _niftyInstance.getCurrentScreen().findNiftyControl("listbox_kill_messages", ListBox.class);
        if (listBox != null)
        {
            int maxItems = listBox.getDisplayItemCount();
            if (listBox.getItems().size() == maxItems)
            {
                listBox.removeItemByIndex(4);
            }

            String roundMessage = "[" + LocalDateTime.now().format(_formatter) + "] Round ended. "+winner+" wins!";
            listBox.insertItem(roundMessage, 0);
        }
    }
    
    
    
    /**
     * Update ammo label (only in hud-screen).
     *
     * @param currentAmmo amount of currently owned ammo
     * @param maxAmmo max amount of ammo
     */
    public void updateAmmo(int currentAmmo, int maxAmmo)
    {
        Element ammoLabel = _niftyInstance.getCurrentScreen().findElementById("text_ammo");
        if (ammoLabel != null)
        {
            String ammoCount = currentAmmo + " / " + maxAmmo;
            ammoLabel.getRenderer(TextRenderer.class).setText(ammoCount);
        }
    }

    /**
     * Update hp label and red/green panels (only in hud-screen).
     *
     * @param currentHp hp in <0,1>
     */
    public void updateHealthBar(float currentHp)
    {
        Element hpLabel = _niftyInstance.getCurrentScreen().findElementById("text_hp_percentage");
        Element greenPanel = _niftyInstance.getCurrentScreen().findElementById("panel_health_green");
        Element containerPanel = _niftyInstance.getCurrentScreen().findElementById("panel_health_bar");

        if (hpLabel != null && greenPanel != null && containerPanel != null)
        {
            int containerWidth = containerPanel.getWidth();
            int greenWidth = (int) (containerWidth * currentHp);
            int hpValue = (int) (currentHp * 100);
            String hpCaption = hpValue + "%";

            hpLabel.getRenderer(TextRenderer.class).setText(hpCaption);
            greenPanel.setWidth(greenWidth);
        }
    }

    /**
     * Update timer label (only in hud-screen).
     *
     * @param timeLeft time left in seconds
     */
    public void updateTimer(int timeLeft)
    {
        Element timerLabel = _niftyInstance.getCurrentScreen().findElementById("text_timer");
        if (timerLabel != null)
        {
            timerLabel.getRenderer(TextRenderer.class).setText(formatSeconds(timeLeft));
        }
    }

    /**
     * Update waepon label and thumbnail (only in hud-screen).
     *
     * @param weapon weapon id from WeaponControl.weapons
     */
    public void updateWeapon(int weapon)
    {
        Element weaponLabel = _niftyInstance.getCurrentScreen().findElementById("text_weapon_type");
        Element weaponImage = _niftyInstance.getCurrentScreen().findElementById("image_weapon");

        if (weaponImage == null || weaponLabel == null)
        {
            return;
        }

        String weaponCaption = "";
        String imagePath = "Interface/Images/";
        switch (weapon)
        {
            case 1:
                weaponCaption = "Pistol";
                imagePath += "pistol_thumb.png";
                break;
            case 2:
                weaponCaption = "RPG";
                imagePath += "rpg_thumb.png";
                break;
            case 3:
                weaponCaption = "Uzi";
                imagePath += "uzi_thumb.png";
                break;
        }

        NiftyImage image = _niftyInstance.getRenderEngine().createImage(_niftyInstance.getCurrentScreen(), imagePath, true);
        weaponImage.getRenderer(ImageRenderer.class).setImage(image);
        weaponLabel.getRenderer(TextRenderer.class).setText(weaponCaption);
    }

    private String formatSeconds(int secondsValue)
    {
        int minutes = secondsValue / 60;
        int seconds = secondsValue % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void controlUpdate(float tpf)
    {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
    }

}
