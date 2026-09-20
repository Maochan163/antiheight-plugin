package ru.server.antiheight;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import ru.server.antiheight.commands.AdminCommand;
import ru.server.antiheight.effects.EffectZoneManager;
import ru.server.antiheight.items.CustomItemFactory;
import ru.server.antiheight.listeners.AutoMaskListener;
import ru.server.antiheight.recipes.NetherHelmetRecipe;

public class AntiHeightPlugin extends JavaPlugin {

    private static AntiHeightPlugin instance;

    private NamespacedKey customItemKey;
    private CustomItemFactory itemFactory;
    private EffectZoneManager zoneManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.customItemKey = new NamespacedKey(this, "antiheight_item_id");
        this.itemFactory = new CustomItemFactory(this, customItemKey);
        this.zoneManager = new EffectZoneManager(this, itemFactory);

        // Регистрируем крафт незерского шлема на верстаке
        NetherHelmetRecipe.register(this, itemFactory);

        // Периодическая проверка игроков и наложение эффектов
        zoneManager.start();

        // Команды
        AdminCommand adminCommand = new AdminCommand(this, itemFactory);
        getCommand("antiheight").setExecutor(adminCommand);
        getCommand("givemask").setExecutor(adminCommand);
        getCommand("givenetherhelmet").setExecutor(adminCommand);

        // Автозамена обычной головы игрока на маску от высоты
        getServer().getPluginManager().registerEvents(new AutoMaskListener(itemFactory), this);

        getLogger().info("AntiHeight включен. Зон эффектов загружено: " + zoneManager.getZoneCount());
    }

    @Override
    public void onDisable() {
        if (zoneManager != null) {
            zoneManager.stop();
        }
    }

    public static AntiHeightPlugin getInstance() {
        return instance;
    }

    public NamespacedKey getCustomItemKey() {
        return customItemKey;
    }

    public CustomItemFactory getItemFactory() {
        return itemFactory;
    }

    public EffectZoneManager getZoneManager() {
        return zoneManager;
    }

    /**
     * Перезагрузка конфига и зон "на лету" без рестарта сервера.
     */
    public void reloadPlugin() {
        reloadConfig();
        zoneManager.reloadZones();
    }
}
