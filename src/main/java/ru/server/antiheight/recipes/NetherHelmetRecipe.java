package ru.server.antiheight.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.server.antiheight.items.CustomItemFactory;

/**
 * ВРЕМЕННО УПРОЩЁННЫЙ рецепт для диагностики — весь верхний ряд:
 * факел, БЕДРОК (вместо печи, которая почему-то не матчится), факел.
 */
public class NetherHelmetRecipe {

    public static void register(JavaPlugin plugin, CustomItemFactory itemFactory) {
        ItemStack result = itemFactory.createItem(CustomItemFactory.NETHER_HELMET_ID);

        NamespacedKey key = new NamespacedKey(plugin, "nether_helmet_craft");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("RFR", "   ", "   ");
        recipe.setIngredient('R', Material.REDSTONE_TORCH);
        recipe.setIngredient('F', Material.BEDROCK);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Рецепт незерского шлема зарегистрирован (ТЕСТ: факел-бедрок-факел).");
    }
}
