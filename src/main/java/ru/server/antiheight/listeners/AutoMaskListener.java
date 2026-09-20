package ru.server.antiheight.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import ru.server.antiheight.items.CustomItemFactory;

/**
 * Если игрок надевает ОБЫЧНУЮ голову игрока (player_head без нашей
 * PDC-метки, полученную например через /give или творческое меню),
 * плагин автоматически заменяет её на маску от высоты.
 *
 * Кастомные предметы плагина (height_mask, nether_helmet) не трогаем —
 * у них уже есть метка, и isCustomItem их не считает "обычной головой".
 */
public class AutoMaskListener implements Listener {

    private final CustomItemFactory itemFactory;

    public AutoMaskListener(CustomItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        // Нас интересует только слот шлема
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.HEAD) {
            return;
        }

        ItemStack newItem = event.getNewItem();
        if (newItem == null || newItem.getType() != Material.PLAYER_HEAD) {
            return;
        }

        // Если это уже наш кастомный предмет (маска или незерский шлем) — не трогаем
        if (itemFactory.getCustomItemId(newItem) != null) {
            return;
        }

        // Это обычная голова игрока без метки — заменяем на маску от высоты
        Player player = event.getPlayer();
        ItemStack mask = itemFactory.createItem(CustomItemFactory.HEIGHT_MASK_ID);

        // Сохраняем количество (на случай стака, хотя броня обычно стак 1)
        mask.setAmount(newItem.getAmount());

        player.getInventory().setHelmet(mask);
    }
}
