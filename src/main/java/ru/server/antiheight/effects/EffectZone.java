package ru.server.antiheight.effects;

import org.bukkit.potion.PotionEffect;

import java.util.List;

public class EffectZone {

    private final String id;
    private final boolean enabled;
    private final String world;
    private final int minY;
    private final int maxY;
    private final List<String> immunityItems; // configId предметов-иммунитетов; любой из списка снимает эффект
    private final List<PotionEffect> effects;

    public EffectZone(String id, boolean enabled, String world, int minY, int maxY,
                       List<String> immunityItems, List<PotionEffect> effects) {
        this.id = id;
        this.enabled = enabled;
        this.world = world;
        this.minY = minY;
        this.maxY = maxY;
        this.immunityItems = immunityItems;
        this.effects = effects;
    }

    public boolean matches(String worldName, double y) {
        return enabled && world.equalsIgnoreCase(worldName) && y >= minY && y <= maxY;
    }

    public String getId() {
        return id;
    }

    public List<String> getImmunityItems() {
        return immunityItems;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }
}
