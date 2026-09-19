package ru.server.antiheight.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.server.antiheight.AntiHeightPlugin;
import ru.server.antiheight.items.CustomItemFactory;

public class AdminCommand implements CommandExecutor {

    private final AntiHeightPlugin plugin;
    private final CustomItemFactory itemFactory;

    public AdminCommand(AntiHeightPlugin plugin, CustomItemFactory itemFactory) {
        this.plugin = plugin;
        this.itemFactory = itemFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (label.toLowerCase()) {
            case "antiheight" -> {
                if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadPlugin();
                    sender.sendMessage(Component.text("§aКонфиг AntiHeight перезагружен."));
                    return true;
                }
                sender.sendMessage(Component.text("§7/antiheight reload"));
                return true;
            }
            case "givemask" -> {
                giveItem(sender, args, CustomItemFactory.HEIGHT_MASK_ID);
                return true;
            }
            case "givenetherhelmet" -> {
                giveItem(sender, args, CustomItemFactory.NETHER_HELMET_ID);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void giveItem(CommandSender sender, String[] args, String itemId) {
        Player target;
        if (args.length > 0) {
            target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("§cИгрок не найден: " + args[0]));
                return;
            }
        } else if (sender instanceof Player p) {
            target = p;
        } else {
            sender.sendMessage(Component.text("§cУкажи ник игрока: /" + itemId + " <ник>"));
            return;
        }

        ItemStack item = itemFactory.createItem(itemId);
        target.getInventory().addItem(item);
        sender.sendMessage(Component.text("§aВыдано: " + itemId + " -> " + target.getName()));
    }
}
