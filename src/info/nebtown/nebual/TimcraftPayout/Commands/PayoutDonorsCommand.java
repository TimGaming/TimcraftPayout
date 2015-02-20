package info.nebtown.nebual.TimcraftPayout.Commands;

import java.util.Set;
import java.util.logging.Logger;

import info.nebtown.nebual.TimcraftPayout.TimcraftPayoutPlugin;



import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;

public class PayoutDonorsCommand implements CommandExecutor {
	private final TimcraftPayoutPlugin plugin;

	public PayoutDonorsCommand(TimcraftPayoutPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Logger.getLogger("Minecraft").warning("This command can only be used by the console.");
			return true;
		}

		Set<PermissionUser> users = plugin.pex.getUsers();
		int numUsers = users.size();
		for(PermissionUser user : users) { // Iterate over all users PEX knows about
			int optionVal = user.getOwnOptionInteger("vip", null, 0); // Start off by checking the option "vip"
			int groupVal = 0;

			for(String group : user.getGroupsNames()) { // Iterate over all groups the user is in
				if(group.contains("VIP")) {
					if(group.equals("VIP")) groupVal = 250; // The group "VIP" counts as 150VIP
					else{
						groupVal = Integer.parseInt(group.substring(0,group.length()-3)); // Get the number from before VIP
					}
					break;
				}
			}

			if(groupVal > optionVal) optionVal = groupVal; // Use whichever one is larger
			if(optionVal > 14) {
				int amt = (optionVal / 15) * 50000; // 10VIP gets nothing, 15VIP gets 50k, 25VIP gets 50k, 30VIP gets 100k, etc.
				this.plugin.economy.depositPlayer(plugin.getServer().getPlayer(user.getName()), amt);
				//Logger.getLogger("Minecraft").info("Awarding "+amt+" ("+optionVal+") to "+user.getName()); //Debug print
			}

			numUsers--;
			if(numUsers%200 == 0) Logger.getLogger("Minecraft").info("Paying VIPs... ("+numUsers+") remaining");
		}

		Logger.getLogger("Minecraft").info("Payouts complete!");
		return true;
	}
}
