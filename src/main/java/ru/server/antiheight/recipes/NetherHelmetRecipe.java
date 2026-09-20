package ru.server.antiheight.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.server.antiheight.items.CustomItemFactory;

/**
 * ВРЕМЕННО УПРОЩЁННЫЙ рецепт для диагностики — только редстоун-факелы
 * по бокам верхнего ряда, остальное пусто.
 */
public class NetherHelmetRecipe {

    public static void register(JavaPlugin plugin, CustomItemFactory itemFactory) {
        ItemStack result = itemFactory.createItem(CustomItemFactory.NETHER_HELMET_ID);

        NamespacedKey key = new NamespacedKey(plugin, "nether_helmet_craft");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("R R", "   ", "   ");
        recipe.setIngredient('R', Material.REDSTONE_TORCH);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Рецепт незерского шлема зарегистрирован (ТЕСТ: только факелы).");
    }
}
