package de.malfrador.nobellspam;

import io.papermc.paper.event.block.BellRingEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Bell;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class NoBellSpam extends JavaPlugin implements Listener {

    int bellCooldown = 10;
    NamespacedKey namespacedKey = new NamespacedKey(this, "cooldown");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        bellCooldown = getConfig().getInt("cooldown");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBellRing(BellRingEvent event) {
        if (!(event.getBlock().getState() instanceof Bell)) {
            return;
        }
        Bell bell = (Bell) event.getBlock().getState();
        PersistentDataContainer container = bell.getPersistentDataContainer();
        long now = System.currentTimeMillis();
        if (!container.has(namespacedKey, PersistentDataType.LONG)) {
            container.set(namespacedKey, PersistentDataType.LONG, now);
            bell.update();
            return;
        }
        long lastRang = bell.getPersistentDataContainer().get(namespacedKey, PersistentDataType.LONG);
        if ((lastRang + bellCooldown * 1000L) <= now) {
            container.set(namespacedKey, PersistentDataType.LONG, now);
            bell.update();
        } else {
            event.setCancelled(true);
        }
    }
}
