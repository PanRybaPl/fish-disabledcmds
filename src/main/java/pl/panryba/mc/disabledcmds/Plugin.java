/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.panryba.mc.disabledcmds;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author PanRyba.pl
 */
public class Plugin extends JavaPlugin implements Listener {

    private Set<String> limited;
    private Set<String> removed;
    
    @Override
    public void onEnable() {
        limited = new HashSet<>();
        removed = new HashSet<>();

        FileConfiguration config = getConfig();
        
        List<String> strings = config.getStringList("limited");
        if(limited != null) {
            for(String str : strings) {
                limited.add(str.toLowerCase());
            }
        }

        List<String> removedStrings = config.getStringList("removed");
        if(removedStrings != null) {
            for(String str : removedStrings) {
                removed.add(str.toLowerCase());
            }
        }
        
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0];
        if(!cmd.startsWith("/")) {
            return;
        }
        
        String preparedCmd = cmd.substring(1).toLowerCase();

        if(this.removed.contains(preparedCmd)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Nieznane polecenie");
            return;
        }

        if(!this.limited.contains(preparedCmd)) {
            return;
        }

        if(event.getPlayer().hasPermission("disabledcmds.suppress")) {
            return;
        }
        
        event.setCancelled(true);
        event.getPlayer().sendMessage("Nieznane polecenie");
    }
}
