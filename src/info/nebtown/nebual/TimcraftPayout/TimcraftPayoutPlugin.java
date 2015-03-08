package info.nebtown.nebual.TimcraftPayout;


import info.nebtown.nebual.TimcraftPayout.Commands.PayoutDonorsCommand;
import info.nebtown.nebual.TimcraftPayout.Exceptions.PluginNotFoundException;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.melonbrew.fe.Fe;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class TimcraftPayoutPlugin extends JavaPlugin {
	public PermissionManager pex;
	public static Economy economy = null;
	public static Permission permission = null;
	public static Fe fe = null;

	public void onEnable() {
		try {
			initializePermissionsEx();
			initializeEconomy();
		} catch (PluginNotFoundException e1) {
			Logger.getLogger("Minecraft").warning(e1.getMessage());
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		Logger.getLogger("Minecraft").info("----------------");
		Logger.getLogger("Minecraft").info("To pay all VIP's: payoutdonors");
		Logger.getLogger("Minecraft").info("----------------");

		getCommand("payoutdonors").setExecutor(new PayoutDonorsCommand(this));
	}
	
	private boolean initializePermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

	private void initializePermissionsEx() throws PluginNotFoundException {
		Plugin plugin = getPlugin("PermissionsEx");
		if (plugin != null) {
			this.pex = PermissionsEx.getPermissionManager();
		} else {
			throw new PluginNotFoundException("PermissionsEx");
		}
	}
	
	private boolean initializeEconomy()
    {
		fe = (Fe) getServer().getPluginManager().getPlugin("Fe");
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
        	Logger.getLogger("Minecraft").info("ERROR: Vault or economy not found!");
        }

        return (economy != null);
    }

	private Plugin getPlugin(String name) {
		return getServer().getPluginManager().getPlugin(name);
	}
}
