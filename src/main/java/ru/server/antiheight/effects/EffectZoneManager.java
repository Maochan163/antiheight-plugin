package ru.server.antiheight.effects;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import ru.server.antiheight.items.CustomItemFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EffectZoneManager {

    private final JavaPlugin plugin;
    private final CustomItemFactory itemFactory;

    private List<EffectZone> zones = new ArrayList<>();
    private Set<String> exemptPlayers = new HashSet<>();
    private int checkIntervalTicks = 20;

    private BukkitTask task;

    public EffectZoneManager(JavaPlugin plugin, CustomItemFactory itemFactory) {
        this.plugin = plugin;
        this.itemFactory = itemFactory;
        reloadZones();
    }

    public void reloadZones() {
        List<EffectZone> loaded = new ArrayList<>();

        checkIntervalTicks = plugin.getConfig().getInt("check-interval-ticks", 20);

        exemptPlayers = new HashSet<>();
        for (String name : plugin.getConfig().getStringList("exempt-players")) {
            exemptPlayers.add(name.toLowerCase());
        }

        ConfigurationSection zonesSection = plugin.getConfig().getConfigurationSection("zones");
        if (zonesSection != null) {
            for (String zoneId : zonesSection.getKeys(false)) {
                ConfigurationSection zc = zonesSection.getConfigurationSection(zoneId);
                if (zc == null) continue;

                boolean enabled = zc.getBoolean("enabled", true);
                String world = zc.getString("world", "world");
                int minY = zc.getInt("min-y", 0);
                int maxY = zc.getInt("max-y", 320);

                // Поддерживаем и старый формат "immunity-item: xxx" (одна строка),
                // и новый "immunity-items: [xxx, yyy]" (список) — чтобы не ломать
                // уже настроенные конфиги.
                List<String> immunityItems = new ArrayList<>();
                if (zc.isList("immunity-items")) {
                    immunityItems.addAll(zc.getStringList("immunity-items"));
                } else if (zc.isString("immunity-item")) {
                    immunityItems.add(zc.getString("immunity-item"));
                }

                List<PotionEffect> effects = new ArrayList<>();
                List<?> rawEffects = zc.getList("effects");
                if (rawEffects != null) {
                    for (Object o : rawEffects) {
                        if (!(o instanceof java.util.Map<?, ?> map)) continue;
                        String typeName = String.valueOf(map.get("type"));
                        int amplifier = map.get("amplifier") != null
                                ? Integer.parseInt(String.valueOf(map.get("amplifier"))) : 0;
                        int durationSeconds = map.get("duration-seconds") != null
                                ? Integer.parseInt(String.valueOf(map.get("duration-seconds"))) : 8;

                        PotionEffectType type = Registry.EFFECT.get(
                                NamespacedKey.minecraft(typeName.toLowerCase()));
                        if (type == null) {
                            plugin.getLogger().warning("Неизвестный тип эффекта '" + typeName
                                    + "' в зоне " + zoneId + ", пропускаю");
                            continue;
                        }
                        effects.add(new PotionEffect(type, durationSeconds * 20, amplifier, true, true));
                    }
                }

                loaded.add(new EffectZone(zoneId, enabled, world, minY, maxY, immunityItems, effects));
            }
        }

        this.zones = loaded;
        plugin.getLogger().info("Загружено зон эффектов: " + zones.size());
    }

    public void start() {
        stop();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, checkIntervalTicks, checkIntervalTicks);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("antiheight.exempt")) continue;
            if (exemptPlayers.contains(player.getName().toLowerCase())) continue;

            String worldName = player.getWorld().getName();
            double y = player.getLocation().getY();

            for (EffectZone zone : zones) {
                if (!zone.matches(worldName, y)) continue;

                if (hasImmunity(player, zone.getImmunityItems())) continue;

                for (PotionEffect effect : zone.getEffects()) {
                    player.addPotionEffect(effect);
                }
            }
        }
    }

    private boolean hasImmunity(Player player, List<String> immunityItemIds) {
        if (immunityItemIds == null || immunityItemIds.isEmpty()) {
            return false;
        }
        ItemStack helmet = player.getInventory().getHelmet();
        String helmetId = itemFactory.getCustomItemId(helmet);
        if (helmetId == null) {
            return false;
        }
        return immunityItemIds.contains(helmetId);
    }

    public int getZoneCount() {
        return zones.size();
    }
}
