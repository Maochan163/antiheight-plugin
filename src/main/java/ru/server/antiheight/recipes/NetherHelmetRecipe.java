package ru.server.antiheight.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.server.antiheight.items.CustomItemFactory;

/**
 * ТЕСТ №2: печь через RecipeChoice.MaterialChoice вместо прямого
 * setIngredient(char, Material) — проверяем, матчится ли так.
 */
public class NetherHelmetRecipe {

    public static void register(JavaPlugin plugin, CustomItemFactory itemFactory) {
        ItemStack result = itemFactory.createItem(CustomItemFactory.NETHER_HELMET_ID);

        NamespacedKey key = new NamespacedKey(plugin, "nether_helmet_craft");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("RFR", "   ", "   ");
        recipe.setIngredient('R', Material.REDSTONE_TORCH);
        recipe.setIngredient('F', new RecipeChoice.MaterialChoice(Material.FURNACE));

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Рецепт незерского шлема зарегистрирован (ТЕСТ: факел-печь(MaterialChoice)-факел).");
    }
}
