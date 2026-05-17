package de.maximanu.lobbySystem.commands;

import de.maximanu.lobbySystem.LobbySystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LobbySystemCommand implements CommandExecutor, TabCompleter { // Implement TabCompleter
   private final LobbySystem plugin;

   public LobbySystemCommand(LobbySystem plugin) {
      this.plugin = plugin;
   }

   @Override // Override-Annotation für onCommand
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (args.length == 0) {
         sender.sendMessage(this.plugin.getMessageService().component("usage.lobbysystem", "Usage: /lobbysystem reload"));
         return true;
      } else if (args[0].equalsIgnoreCase("reload")) {
         if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.isOp() && !p.hasPermission("lobbysystem.reload")) {
               p.sendMessage(this.plugin.getMessageService().component("errors.no-permission.reload", "&cYou don't have permission to reload the config."));
               return true;
            }
         } else if (sender instanceof ConsoleCommandSender) {
            // Keine spezielle Berechtigungsprüfung für die Konsole, da sie standardmäßig alle Rechte hat
         }

         this.plugin.reloadPluginConfig();
         this.plugin.getPlayerListener().refreshAllPlayers();
         sender.sendMessage(this.plugin.getMessageService().component("info.config-reloaded", "&aConfig reloaded."));
         return true;
      } else {
         sender.sendMessage(this.plugin.getMessageService().component("usage.lobbysystem", "Usage: /lobbysystem reload"));
         return true;
      }
   }

   @Override // Implementierung der TabCompleter-Methode
   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      if (args.length == 1) {
         List<String> completions = new ArrayList<>();
         String input = args[0].toLowerCase();
         if ("reload".startsWith(input)) {
            completions.add("reload");
         }
         return completions;
      }
      return Collections.emptyList(); // Keine weiteren Vervollständigungen für mehr als ein Argument
   }
}