package net.tylers1066.beaming;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(p.hasMetadata("beaming") && p.getMetadata("beaming").get(0).asBoolean()) {
            e.setDeathMessage(BeamingPlugin.PREFIX + p.getDisplayName() + " beamed to their ship");
            p.setMetadata("beaming", new FixedMetadataValue(BeamingPlugin.getInstance(),false));
        }

        if(!Config.EnableRespawn) {
            return;
        }

        if(Config.EnableRespawnMainHand) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().setHeldItemSlot(0);
                    p.getInventory().setItemInMainHand(Config.RespawnMainHand);
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 5L);
        }
        if(Config.EnableRespawnOffHand) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().setItemInOffHand(Config.RespawnOffHand);
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 5L);
        }
        if(Config.EnableRespawnStrength) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Config.StrengthDuration, Config.StrengthAmplitude));
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 10L);
        }
        if(Config.EnableRespawnSpeed) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Config.SpeedDuration, Config.SpeedAmplitude));
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 10L);
        }
        if(Config.EnableRespawnResistance) {
            p.setMetadata("BeamingRespawn", new FixedMetadataValue(BeamingPlugin.getInstance(), null));

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Config.ResistanceDuration, Config.ResistanceAmplitude));
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 10L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.removeMetadata("BeamingRespawn", BeamingPlugin.getInstance());
                }
            }.runTaskLater(BeamingPlugin.getInstance(), Config.ResistanceDuration);
        }
    }
}
