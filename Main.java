package FrNomad.Cart.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	ChatColor NoticeChatcolor = ChatColor.AQUA;
	
	double trainspeed = 0.4D;
	
	double accel = 5.0D;
	
	private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}]");
	private String format(String msg) {
		if(Bukkit.getVersion().contains("1.17")) {
			Matcher match = pattern.matcher(msg);
			while(match.find()) {
				String color = msg.substring(match.start(), match.end());
				msg = msg.replace(color, ChatColor.of(color) + "");
				match = pattern.matcher(msg);
			}
		}
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	
	@Override
	public void onEnable() {
		getCommand("minecartspeed").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		PluginDescriptionFile pdfile = this.getDescription();
		System.out.println(NoticeChatcolor + "==================================================");
		System.out.println(NoticeChatcolor + "[Test] ������ : FrNomad");
		System.out.println(NoticeChatcolor + "[Test] " + pdfile.getName() + " " + pdfile.getVersion() + " ������ ���� �Ϸ��.");
		System.out.println(NoticeChatcolor + "==================================================");
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfile = this.getDescription();
		System.out.println(NoticeChatcolor + "==================================================");
		System.out.println(NoticeChatcolor + "[Test] " + pdfile.getName() + " " + pdfile.getVersion() + " ������ ����.");
		System.out.println(NoticeChatcolor + "==================================================");
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("minecartspeed")) {
			if(args.length < 1) {
				s.sendMessage(format("&c�����⸦ �Է��Ͻʽÿ�."));
				return false;
			}
			else if(args[0].equalsIgnoreCase("now")) {
				double speed = (trainspeed / 0.4D) * 28.8D;
				s.sendMessage(format("&6���� ����īƮ�� �ӷ��� &a" + speed + "km/h &6�Դϴ�."));
				s.sendMessage(format("&6���� ����īƮ�� �ӷ��� &a" + trainspeed + "m/tick &6�Դϴ�."));
			}
			else {
				try {
					double speed = Double.parseDouble(args[0]);
					if(speed < 0) {
						s.sendMessage(format("&c�ӷ��� ������ �� �� �����ϴ�."));
					}
					else {
						trainspeed = (speed / 28.8D) * 0.4D;
						s.sendMessage(format("&6����īƮ�� �ӷ��� &a" + speed + "km/h &6�� �����Ǿ����ϴ�."));
					}
				} catch(NumberFormatException e) {
					s.sendMessage(format("&c�˸��� ���� �Է��Ͻʽÿ�."));
				}
			}
		}
		return false;
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleCreate(VehicleCreateEvent event) {
        if(event.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart) event.getVehicle();
            cart.setMaxSpeed(trainspeed);
        }
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleMove(VehicleMoveEvent event) {
		if(event.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart) event.getVehicle();
			cart.setMaxSpeed(trainspeed);
			cart.setVelocity(event.getVehicle().getVelocity().multiply(accel));
		}
	}
	
}
