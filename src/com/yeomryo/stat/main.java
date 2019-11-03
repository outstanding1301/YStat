package com.yeomryo.stat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.yeomryo.stat.Event.EventManager;
import com.yeomryo.stat.Stat.Stat;
import com.yeomryo.stat.Stat.StatAPI;

public class main extends JavaPlugin{
	
	public static LinkedList<Stat> statlist = new LinkedList<>();
	public static HashMap<Player, Stat> playerStat = new HashMap<>();

	public static int coal;
	public static int iron;
	public static int gold;
	public static int reds;
	public static int lapis;
	public static int diamond;
	public static int emerald;
	
	public static int creeper;
	public static int skeletone;
	public static int spider;
	public static int zombie;
	public static int slime;
	public static int ghast;
	public static int pigman;
	public static int enderman;
	public static int silverfish;
	public static int blaze;
	public static int magmacube;
	public static int pig;
	public static int sheep;
	public static int cow;
	public static int chicken;
	public static int squid;
	public static int wolf;
	public static int ocelot;
	public static int villager;
	public static int player;
	
	public static double atk=0.1; // 스탯당 공격력
	public static double def=0.1; // 스탯당 방어력
	public static double hp=0.1; // 스탯당 체력증가
	public static double drain; // 스탯당 흡혈
	public static double dex; // 스탯당 민첩성
	public static double gen; // 스탯당 재생력
	
	public static double stun; // 스탯당 스턴확률
	public static double fire; // 스탯당 발화확률
	public static double knockback; // 스탯당 넉백확률
	public static double thorn; // 스탯당 반사확률
	public static double poison; // 스탯당 독확률
	public static double wither; // 스탯당 위더확률
	public static double crit; // 스탯당 크리티컬확률

	public static double mineluck; // 스탯당 광물행운
	public static double farmluck; // 스탯당 작물행운
	
	public static double mul; // 스탯당 멀미확률
	public static double sil; // 스탯당 실명확률
	public static double piro; // 스탯당 피로확률
	public static double nay; // 스탯당 나약함확률
	
	public void onEnable() {
		// 파일 불러옴
		loadFiles();
		
		
		// -------------------------
		System.out.println("염료의 스탯 플러그인 활성화");
		System.out.println(" ");
		System.out.println("확인된 플레이어 수 : "+statlist.size()+"\n");
		System.out.println("본 플러그인의 저작권은 모두 염료에게 있습니다.");
		
		getServer().getPluginManager().registerEvents(new EventManager(), this);

		for(Player p :Bukkit.getOnlinePlayers()){
			StatAPI.getStat(p);
			StatAPI.refresh(p);
		}
	}
	
	@Override
	public void onDisable() {
		for(Stat s : statlist){
			try {
				s.saveFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	public void test(){
		getConfig().set("exp.mine.coal", 1);
		getConfig().set("exp.mine.iron", 1);
		getConfig().set("exp.mine.gold", 1);
		getConfig().set("exp.mine.redstone", 1);
		getConfig().set("exp.mine.lapis", 1);
		getConfig().set("exp.mine.diamond", 1);
		getConfig().set("exp.mine.emerald", 1);

		getConfig().set("stats.atk", 0.5);
		getConfig().set("stats.def", 0.5);
		getConfig().set("stats.hp", 0.5);
		getConfig().set("stats.drain", 0.5);
		getConfig().set("stats.dex", 0.5);
		getConfig().set("stats.gen", 0.5);
		getConfig().set("stats.stun", 0.5);
		getConfig().set("stats.fire", 0.5);
		getConfig().set("stats.knockback", 0.5);
		getConfig().set("stats.thorn", 0.5);
		getConfig().set("stats.poison", 0.5);
		getConfig().set("stats.wither", 0.5);
		getConfig().set("stats.critical", 0.5);
		getConfig().set("stats.mineluck", 0.5);
		getConfig().set("stats.farmluck", 0.5);
		getConfig().set("stats.mosick", 0.5);
		getConfig().set("stats.blind", 0.5);
		getConfig().set("stats.tired", 0.5);
		getConfig().set("stats.weakness", 0.5);
		saveConfig();
	}
	*/
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("스탯") || label.equalsIgnoreCase("stat")){
			if(args.length == 0){
				if(sender instanceof Player){
					Player p = (Player)sender;
					StatAPI.viewStatGUI(p);
					return true;
				}
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("로드") || args[0].equalsIgnoreCase("load")){
					loadConfig();
					sender.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.GREEN+"config 파일을 갱신합니다...");
				}
			}
		}
		if(label.equalsIgnoreCase("yrcmd")){
			if(sender instanceof Player){
				if(sender.isOp()){
					Player p = (Player)sender;
					p.getInventory().addItem(StatAPI.getICON(Material.PAPER, "§6[ §f스탯권 §6]§f"));
					return true;
				}
			}
		}
		return true;
	}
	
	public void loadConfig(){
		this.coal = getConfig().getInt("exp.mine.coal");
		this.iron = getConfig().getInt("exp.mine.iron");
		this.gold = getConfig().getInt("exp.mine.gold");
		this.reds = getConfig().getInt("exp.mine.redstone");
		this.lapis = getConfig().getInt("exp.mine.lapis");
		this.diamond = getConfig().getInt("exp.mine.diamond");
		this.emerald = getConfig().getInt("exp.mine.emerald");

		this.creeper = getConfig().getInt("exp.mob.creeper");
		this.skeletone = getConfig().getInt("exp.mob.skeletone");
		this.spider = getConfig().getInt("exp.mob.spider");
		this.zombie = getConfig().getInt("exp.mob.zombie");
		this.slime = getConfig().getInt("exp.mob.slime");
		this.ghast = getConfig().getInt("exp.mob.ghast");
		this.pigman = getConfig().getInt("exp.mob.pigman");
		this.enderman = getConfig().getInt("exp.mob.enderman");
		this.silverfish = getConfig().getInt("exp.mob.silverfish");
		this.blaze = getConfig().getInt("exp.mob.blaze");
		this.magmacube = getConfig().getInt("exp.mob.magmacube");
		this.pig = getConfig().getInt("exp.mob.pig");
		this.sheep = getConfig().getInt("exp.mob.sheep");
		this.cow = getConfig().getInt("exp.mob.cow");
		this.chicken = getConfig().getInt("exp.mob.chicken");
		this.squid = getConfig().getInt("exp.mob.squid");
		this.wolf = getConfig().getInt("exp.mob.wolf");
		this.ocelot = getConfig().getInt("exp.mob.ocelot");
		this.villager = getConfig().getInt("exp.mob.villager");
		this.player = getConfig().getInt("exp.mob.player");

		this.atk = getConfig().getDouble("stats.atk");
		this.def = getConfig().getDouble("stats.def");
		this.hp = getConfig().getDouble("stats.hp");
		this.drain = getConfig().getDouble("stats.drain");
		this.dex = getConfig().getDouble("stats.dex");
		this.gen = getConfig().getDouble("stats.gen");
		this.stun = getConfig().getDouble("stats.stun");
		this.fire = getConfig().getDouble("stats.fire");
		this.knockback = getConfig().getDouble("stats.knockback");
		this.thorn = getConfig().getDouble("stats.thorn");
		this.poison = getConfig().getDouble("stats.poison");
		this.wither = getConfig().getDouble("stats.wither");
		this.crit = getConfig().getDouble("stats.critical");
		this.mineluck = getConfig().getDouble("stats.mineluck");
		this.farmluck = getConfig().getDouble("stats.farmluck");
		this.mul = getConfig().getDouble("stats.mosick");
		this.sil = getConfig().getDouble("stats.blind");
		this.piro = getConfig().getDouble("stats.tired");
		this.nay = getConfig().getDouble("stats.weakness");
	}
	
	public void loadUserData(){
		statlist.clear();
		File f = new File("yeomryo\\stat");
		if(!f.exists())
			f.mkdirs();
		File files[] = f.listFiles();
		for(int i = 0;i<files.length;++i){
			Stat c = new Stat();
				try {
					c.loadFile(files[i].getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statlist.add(c);
		}
	}
	
	public void loadFiles(){
		this.getConfig().options().copyDefaults(true);
		loadConfig();
		this.saveConfig();
		loadUserData();
	}
}
