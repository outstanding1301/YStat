package com.yeomryo.stat.Stat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.yeomryo.stat.main;

public class StatAPI {
	public static Stat getStat(Player p){
		Stat s = new Stat();
		if(main.playerStat.containsKey(p.getPlayer())){ return main.playerStat.get(p);}
		if(!hasStat(p)){
			s.setPlayer(p.getName());
			try {
				s.saveFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Stat d : main.statlist){
			if(d.getPlayer().equalsIgnoreCase(p.getName())){
				main.playerStat.put(p, d);
				s.refresh();
				return d;
			}
		}
		s.refresh();
		main.statlist.add(s);
		main.playerStat.put(p, s);
		return s;
	}
	
	public static void refresh(Player p){
		Stat s = getStat(p);
		s.refresh();
	}
	
	public static boolean hasStat(Player p){
		for(Stat s : main.statlist){
			if(s.getPlayer().equalsIgnoreCase(p.getName()))
				return true;
		}
		return false;
	}
	public static void viewStatGUI(Player p){
		Stat s = getStat(p);
		Inventory iv = Bukkit.createInventory(p, 9*5, "내 스탯"); // 19
		iv.setItem(27, getICON(Material.EMERALD, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+"남은 스킬 포인트 : "+s.getSp()));
		iv.setItem(10, getICON(Material.IRON_SWORD, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 공격력 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getAtk()*main.atk+ChatColor.YELLOW+
				"("+main.atk+"x"+s.getAtk()+")",ChatColor.WHITE+"의 추가 피해를 줍니다."));
		
		iv.setItem(11, getICON(Material.DIAMOND_CHESTPLATE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 방어력 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+s.getDef()*main.def+ChatColor.YELLOW+
				"("+main.def+"x"+s.getDef()+")",ChatColor.WHITE+" 감소된 피해를 받습니다."));
		
		iv.setItem(12, getICON(Material.APPLE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 체력 ]", ChatColor.WHITE+""+ChatColor.GREEN+""+s.getHp()*main.hp+ChatColor.YELLOW+
				"("+main.hp+"x"+s.getHp()+")",ChatColor.WHITE+"의 추가 체력을 얻습니다."));
		
		iv.setItem(13, getICON(Material.NETHER_WARTS, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 흡혈 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getDrain()*main.drain+ChatColor.YELLOW+
				"("+main.drain+"x"+s.getDrain()+")%",ChatColor.WHITE+"의 확률로 체력을 회복합니다."));
		
		iv.setItem(14, getICON(Material.GOLD_BOOTS, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 민첩성 ]", ChatColor.WHITE+"이동속도가 "+ChatColor.GREEN+""+s.getDex()*main.dex+ChatColor.YELLOW+
				"("+main.dex+"x"+s.dex+")",ChatColor.WHITE+"만큼 빨라집니다."));
		
		iv.setItem(15, getICON(Material.GOLDEN_APPLE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 재생 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+s.getGen()*main.gen+ChatColor.YELLOW+
				"("+main.gen+"x"+s.getGen()+")%",ChatColor.WHITE+"의 확률로 재생효과를 얻습니다."));
		
		iv.setItem(16, getICON(Material.BONE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 스턴 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getStun()*main.stun+ChatColor.YELLOW+
				"("+main.stun+"x"+s.getStun()+")%",ChatColor.WHITE+"의 확률로 적을 스턴시킵니다."));
		
		iv.setItem(19, getICON(Material.FIREBALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 발화 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getFire()*main.fire+ChatColor.YELLOW+
				"("+main.fire+"x"+s.getFire()+")%",ChatColor.WHITE+"의 확률로 적에게 불을 붙입니다."));
		
		iv.setItem(20, getICON(Material.FIREWORK, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 넉백 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getKnockback()*main.knockback+ChatColor.YELLOW+
				"("+main.knockback+"x"+s.getKnockback()+")%",ChatColor.WHITE+"의 확률로 추가 피해를 줍니다."));
		
		iv.setItem(21, getICON(Material.FEATHER, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 반사 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+s.getThorn()*main.thorn+ChatColor.YELLOW+
				"("+main.thorn+"x"+s.getThorn()+")%",ChatColor.WHITE+"확률로 데미지를 반사합니다."));
		
		iv.setItem(22, getICON(Material.SLIME_BALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 독 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getPoison()*main.poison+ChatColor.YELLOW+
				"("+main.poison+"x"+s.getPoison()+")%",ChatColor.WHITE+"의 확률로 독 피해를 줍니다."));
		
		iv.setItem(23, getICON(Material.COAL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 위더 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getWither()*main.wither+ChatColor.YELLOW+
				"("+main.wither+"x"+s.getWither()+")%",ChatColor.WHITE+"의 확률로 위더효과를 줍니다."));
		
		iv.setItem(24, getICON(Material.SNOW_BALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 치명타 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getCrit()*main.crit+ChatColor.YELLOW+
				"("+main.crit+"x"+s.getCrit()+")%",ChatColor.WHITE+"의 확률로 2배의 피해를 줍니다."));
		
		iv.setItem(25, getICON(Material.WEB, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 나약함 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getNay()*main.nay+ChatColor.YELLOW+
				"("+main.nay+"x"+s.getNay()+")%",ChatColor.WHITE+"의 확률로 나약함을 줍니다."));
		
		iv.setItem(29, getICON(Material.DIAMOND_PICKAXE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 광물행운 ]", ChatColor.WHITE+"광물을 캘 시 "+ChatColor.GREEN+""+s.getMineluck()*main.mineluck+ChatColor.YELLOW+
				"("+main.mineluck+"x"+s.getMineluck()+")%",ChatColor.WHITE+"의 확률로 행운 효과를 받습니다."));
		
		iv.setItem(30, getICON(Material.IRON_HOE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 작물행운 ]", ChatColor.WHITE+"작물 채집 시 "+ChatColor.GREEN+""+s.getFarmluck()*main.farmluck+ChatColor.YELLOW+
				"("+main.farmluck+"x"+s.getFarmluck()+")%",ChatColor.WHITE+"의 확률로 행운 효과를 받습니다."));
		
		iv.setItem(31, getICON(Material.RAW_FISH, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 멀미 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getMul()*main.mul+ChatColor.YELLOW+
				"("+main.mul+"x"+s.getMul()+")%",ChatColor.WHITE+"의 확률로 멀미 효과를 줍니다."));
		
		iv.setItem(32, getICON(Material.STRING, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 실명 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getSil()*main.sil+ChatColor.YELLOW+
				"("+main.sil+"x"+s.getSil()+")%",ChatColor.WHITE+"의 확률로 실명 효과를 줍니다."));
		
		iv.setItem(33, getICON(Material.BRICK, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 피로 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getPiro()*main.piro+ChatColor.YELLOW+
				"("+main.piro+"x"+s.getPiro()+")%",ChatColor.WHITE+"의 확률로 피로 효과를 줍니다."));
		iv.setItem(44, getICON(Material.SIGN, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+"저장","§6변경된 사항을 저장합니다."));
		p.openInventory(iv);
	}
	
	
	
	public static void viewStatGUI(Player p, Player t){
		Stat s = getStat(p);
		Inventory iv = Bukkit.createInventory(p, 9*5, p.getName()+"의 스탯"); // 19
		iv.setItem(10, getICON(Material.IRON_SWORD, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 공격력 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getAtk()*main.atk+ChatColor.YELLOW+
				"("+main.atk+"x"+s.getAtk()+")",ChatColor.WHITE+"의 추가 피해를 줍니다."));
		
		iv.setItem(11, getICON(Material.DIAMOND_CHESTPLATE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 방어력 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+s.getDef()*main.def+ChatColor.YELLOW+
				"("+main.def+"x"+s.getDef()+")",ChatColor.WHITE+" 감소된 피해를 받습니다."));
		
		iv.setItem(12, getICON(Material.APPLE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 체력 ]", ChatColor.WHITE+""+ChatColor.GREEN+""+s.getHp()*main.hp+ChatColor.YELLOW+
				"("+main.hp+"x"+s.getHp()+")",ChatColor.WHITE+"의 추가 체력을 얻습니다."));
		
		iv.setItem(13, getICON(Material.NETHER_WARTS, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 흡혈 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getDrain()*main.drain+ChatColor.YELLOW+
				"("+main.drain+"x"+s.getDrain()+")%",ChatColor.WHITE+"의 확률로 체력을 회복합니다."));
		
		iv.setItem(14, getICON(Material.GOLD_BOOTS, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 민첩성 ]", ChatColor.WHITE+"이동속도가 "+ChatColor.GREEN+""+s.getDex()*main.dex+ChatColor.YELLOW+
				"("+main.dex+"x"+s.dex+")",ChatColor.WHITE+"만큼 빨라집니다."));
		
		iv.setItem(15, getICON(Material.GOLDEN_APPLE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 재생 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+s.getGen()*main.gen+ChatColor.YELLOW+
				"("+main.gen+"x"+s.getGen()+")%",ChatColor.WHITE+"의 확률로 재생효과를 얻습니다."));
		
		iv.setItem(16, getICON(Material.BONE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 스턴 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getStun()*main.stun+ChatColor.YELLOW+
				"("+main.stun+"x"+s.getStun()+")%",ChatColor.WHITE+"의 확률로 적을 스턴시킵니다."));
		
		iv.setItem(19, getICON(Material.FIREBALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 발화 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getFire()*main.fire+ChatColor.YELLOW+
				"("+main.fire+"x"+s.getFire()+")",ChatColor.WHITE+"초간 적에게 불을 붙입니다."));
		
		iv.setItem(20, getICON(Material.FIREWORK, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 넉백 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getKnockback()*main.knockback+ChatColor.YELLOW+
				"("+main.knockback+"x"+s.getKnockback()+")%",ChatColor.WHITE+"의 확률로 추가 피해를 줍니다."));
		
		iv.setItem(21, getICON(Material.FEATHER, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 반사 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+s.getThorn()*main.thorn+ChatColor.YELLOW+
				"("+main.thorn+"x"+s.getThorn()+")%",ChatColor.WHITE+"확률로 데미지를 반사합니다."));
		
		iv.setItem(22, getICON(Material.SLIME_BALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 독 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getPoison()*main.poison+ChatColor.YELLOW+
				"("+main.poison+"x"+s.getPoison()+")",ChatColor.WHITE+"초간 독 피해를 줍니다."));
		
		iv.setItem(23, getICON(Material.COAL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 위더 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getWither()*main.wither+ChatColor.YELLOW+
				"("+main.wither+"x"+s.getWither()+")",ChatColor.WHITE+"초간 위더효과를 줍니다."));
		
		iv.setItem(24, getICON(Material.SNOW_BALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 치명타 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getCrit()*main.crit+ChatColor.YELLOW+
				"("+main.crit+"x"+s.getCrit()+")%",ChatColor.WHITE+"의 확률로 2배의 피해를 줍니다."));
		
		iv.setItem(25, getICON(Material.WEB, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 나약함 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getNay()*main.nay+ChatColor.YELLOW+
				"("+main.nay+"x"+s.getNay()+")%",ChatColor.WHITE+"의 확률로 나약함을 줍니다."));
		
		iv.setItem(29, getICON(Material.DIAMOND_PICKAXE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 광물행운 ]", ChatColor.WHITE+"광물을 캘 시 "+ChatColor.GREEN+""+s.getMineluck()*main.mineluck+ChatColor.YELLOW+
				"("+main.mineluck+"x"+s.getMineluck()+")%",ChatColor.WHITE+"의 확률로 행운 효과를 받습니다."));
		
		iv.setItem(30, getICON(Material.IRON_HOE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 작물행운 ]", ChatColor.WHITE+"작물 채집 시 "+ChatColor.GREEN+""+s.getFarmluck()*main.farmluck+ChatColor.YELLOW+
				"("+main.farmluck+"x"+s.getFarmluck()+")%",ChatColor.WHITE+"의 확률로 행운 효과를 받습니다."));
		
		iv.setItem(31, getICON(Material.RAW_FISH, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 멀미 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getMul()*main.mul+ChatColor.YELLOW+
				"("+main.mul+"x"+s.getMul()+")",ChatColor.WHITE+"초간 멀미 효과를 줍니다."));
		
		iv.setItem(32, getICON(Material.STRING, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 실명 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getSil()*main.sil+ChatColor.YELLOW+
				"("+main.sil+"x"+s.getSil()+")",ChatColor.WHITE+"초간 실명 효과를 줍니다."));
		
		iv.setItem(33, getICON(Material.BRICK, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
				" [ 피로 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+s.getPiro()*main.piro+ChatColor.YELLOW+
				"("+main.piro+"x"+s.getPiro()+")",ChatColor.WHITE+"초간 피로 효과를 줍니다."));
		
		t.openInventory(iv);
	}

	public static ItemStack getICON(Material material, String name){
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack getICON(Material material, String name, String... lore){
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		LinkedList<String> l = new LinkedList<>();
		for(String s : lore)
			l.add(s);
		im.setLore(l);
		is.setItemMeta(im);
		return is;
	}

	public static void addEXP(Player p, int exp){
		Stat s = getStat(p);
		s.addExp(exp);
	}
	public static void setEXP(Player p, int exp){
		Stat s = getStat(p);
		s.setExp(exp);
	}
	public static int getEXP(Player p){
		Stat s = getStat(p);
		return s.getExp();
	}
	
	public static void addLVL(Player p, int level){
		Stat s = getStat(p);
		s.addLevel(level);
		
	}
	public static void setLVL(Player p, int level){
		Stat s = getStat(p);
		s.setLevel(level);
		
	}
	public static int getLVL(Player p){
		Stat s = getStat(p);
		return s.getLevel();
	}
}
