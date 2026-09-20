package ru.server.antiheight.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.server.antiheight.items.CustomItemFactory;

/**
 * Крафт незерского шлема на верстаке.
 *
 * Схема (по макету от владельца):
 *   [Redstone Torch] [Furnace]     [Redstone Torch]
 *   [Resin Clump]    [Player Head] [Resin Clump]
 *   [Blue Ice]        [Wet Sponge] [Blue Ice]
 */
public class NetherHelmetRecipe {

    public static void register(JavaPlugin plugin, CustomItemFactory itemFactory) {
        ItemStack result = itemFactory.createItem(CustomItemFactory.NETHER_HELMET_ID);

        NamespacedKey key = new NamespacedKey(plugin, "nether_helmet_craft");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("RFR", "CHC", "IWI");

        recipe.setIngredient('R', Material.REDSTONE_TORCH);
        recipe.setIngredient('F', Material.FURNACE);
        recipe.setIngredient('C', Material.RESIN_CLUMP);
        // ВРЕМЕННО для диагностики: булыжник вместо головы игрока.
        // Если крафт заработает с булыжником — значит проблема была именно в PLAYER_HEAD.
        recipe.setIngredient('H', Material.COBBLESTONE);
        recipe.setIngredient('I', Material.BLUE_ICE);
        recipe.setIngredient('W', Material.WET_SPONGE);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Рецепт незерского шлема зарегистрирован.");
    }
}
