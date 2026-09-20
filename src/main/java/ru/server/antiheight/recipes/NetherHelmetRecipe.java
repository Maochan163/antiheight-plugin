package ru.server.antiheight.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.server.antiheight.items.CustomItemFactory;

/**
 * ВРЕМЕННЫЙ ТЕСТОВЫЙ рецепт незерского шлема на верстаке.
 * Один бедрок в правом верхнем углу — чтобы проверить, что система
 * крафтов плагина вообще работает на сервере, прежде чем возвращать
 * оригинальный сложный рецепт.
 */
public class NetherHelmetRecipe {

    public static void register(JavaPlugin plugin, CustomItemFactory itemFactory) {
        ItemStack result = itemFactory.createItem(CustomItemFactory.NETHER_HELMET_ID);

        NamespacedKey key = new NamespacedKey(plugin, "nether_helmet_craft");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("  B", "   ", "   ");
        recipe.setIngredient('B', Material.BEDROCK);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Рецепт незерского шлема зарегистрирован (ТЕСТОВЫЙ: бедрок в углу).");
    }
}
