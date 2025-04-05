package com.angie.customChest.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customchest.admin")) return Collections.emptyList();

        if (args.length == 1) {
            List<String> sub = new ArrayList<>();
            sub.add("열기");
            sub.add("리로드");
            return sub;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("열기")) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                names.add(p.getName());
            }
            return names;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("열기")) {
            List<String> numbers = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                numbers.add(String.valueOf(i));
            }
            return numbers;
        }

        return Collections.emptyList();
    }
}
