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
		System.out.println(NoticeChatcolor + "[Test] 제작자 : FrNomad");
		System.out.println(NoticeChatcolor + "[Test] " + pdfile.getName() + " " + pdfile.getVersion() + " 버전이 실행 완료됨.");
		System.out.println(NoticeChatcolor + "==================================================");
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfile = this.getDescription();
		System.out.println(NoticeChatcolor + "==================================================");
		System.out.println(NoticeChatcolor + "[Test] " + pdfile.getName() + " " + pdfile.getVersion() + " 버전이 꺼짐.");
		System.out.println(NoticeChatcolor + "==================================================");
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("minecartspeed")) {
			if(args.length < 1) {
				s.sendMessage(format("&c빠르기를 입력하십시오."));
				return false;
			}
			else if(args[0].equalsIgnoreCase("now")) {
				double speed = (trainspeed / 0.4D) * 28.8D;
				s.sendMessage(format("&6현재 마인카트의 속력은 &a" + speed + "km/h &6입니다."));
				s.sendMessage(format("&6현재 마인카트의 속력은 &a" + trainspeed + "m/tick &6입니다."));
			}
			else {
				try {
					double speed = Double.parseDouble(args[0]);
					if(speed < 0) {
						s.sendMessage(format("&c속력은 음수가 될 수 없습니다."));
					}
					else {
						trainspeed = (speed / 28.8D) * 0.4D;
						s.sendMessage(format("&6마인카트의 속력이 &a" + speed + "km/h &6로 설정되었습니다."));
					}
				} catch(NumberFormatException e) {
					s.sendMessage(format("&c알맞은 수를 입력하십시오."));
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
