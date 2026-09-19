package ru.server.antiheight.effects;

import org.bukkit.potion.PotionEffect;

import java.util.List;

public class EffectZone {

    private final String id;
    private final boolean enabled;
    private final String world;
    private final int minY;
    private final int maxY;
    private final String immunityItem; // configId предмета-иммунитета, или "none"
    private final List<PotionEffect> effects;

    public EffectZone(String id, boolean enabled, String world, int minY, int maxY,
                       String immunityItem, List<PotionEffect> effects) {
        this.id = id;
        this.enabled = enabled;
        this.world = world;
        this.minY = minY;
        this.maxY = maxY;
        this.immunityItem = immunityItem;
        this.effects = effects;
    }

    public boolean matches(String worldName, double y) {
        return enabled && world.equalsIgnoreCase(worldName) && y >= minY && y <= maxY;
    }

    public String getId() {
        return id;
    }

    public String getImmunityItem() {
        return immunityItem;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }
}
