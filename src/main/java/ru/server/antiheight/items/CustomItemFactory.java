package ru.server.antiheight.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

/**
 * Создаёт и распознаёт кастомные предметы плагина (маски, шлемы).
 * Каждый такой предмет — player_head (или заглушка) с меткой в
 * PersistentDataContainer, поэтому его нельзя подделать обычной командой
 * /give с текстурой — метка ставится только этим кодом.
 */
public class CustomItemFactory {

    public static final String HEIGHT_MASK_ID = "height_mask";
    public static final String NETHER_HELMET_ID = "nether_helmet";

    private final JavaPlugin plugin;
    private final NamespacedKey itemIdKey;

    public CustomItemFactory(JavaPlugin plugin, NamespacedKey itemIdKey) {
        this.plugin = plugin;
        this.itemIdKey = itemIdKey;
    }

    /**
     * Создаёт кастомный предмет по ключу из config.yml (items.<id>).
     */
    public ItemStack createItem(String configId) {
        var section = plugin.getConfig().getConfigurationSection("items." + configId);
        if (section == null) {
            plugin.getLogger().warning("Нет настроек items." + configId + " в config.yml, использую заглушку");
            return fallbackItem(configId);
        }

        String displayName = section.getString("display-name", configId);
        List<String> lore = section.getStringList("lore");
        String textureValue = section.getString("texture-value", "");

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if (textureValue != null && !textureValue.isBlank()) {
            applyTexture(meta, textureValue);
        }

        meta.displayName(
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize(displayName)
                        .decoration(TextDecoration.ITALIC, false)
        );

        if (!lore.isEmpty()) {
            meta.lore(lore.stream()
                    .map(line -> LegacyComponentSerializer.legacyAmpersand()
                            .deserialize(line)
                            .decoration(TextDecoration.ITALIC, false))
                    .toList());
        }

        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, configId);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack fallbackItem(String configId) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text(configId).decoration(TextDecoration.ITALIC, false));
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, configId);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Применяет base64 texture value (формат Mojang textures API,
     * как на minecraft-heads.com) к голове через PlayerProfile.
     */
    private void applyTexture(SkullMeta meta, String base64Texture) {
        try {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
            profile.setProperty(new com.destroystokyo.paper.profile.ProfileProperty(
                    "textures", base64Texture));
            meta.setPlayerProfile(profile);
        } catch (Exception e) {
            plugin.getLogger().warning("Не удалось применить текстуру головы: " + e.getMessage());
        }
    }

    /**
     * Возвращает ID кастомного предмета (например "height_mask"),
     * или null, если предмет не является кастомным предметом плагина.
     */
    public String getCustomItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return item.getItemMeta()
                .getPersistentDataContainer()
                .get(itemIdKey, PersistentDataType.STRING);
    }

    public boolean isCustomItem(ItemStack item, String configId) {
        return configId.equals(getCustomItemId(item));
    }
}
