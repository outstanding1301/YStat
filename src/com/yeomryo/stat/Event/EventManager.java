package com.yeomryo.stat.Event;

import javax.swing.plaf.synth.SynthProgressBarUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.yeomryo.stat.main;
import com.yeomryo.stat.Stat.Stat;
import com.yeomryo.stat.Stat.StatAPI;

public class EventManager implements Listener{
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(!main.playerStat.containsKey(e.getPlayer())){
			StatAPI.getStat(e.getPlayer());
		}
		StatAPI.refresh(e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		if(main.playerStat.containsKey(e.getPlayer())){
			main.playerStat.remove(e.getPlayer());
		}
	}
	@EventHandler
	public void rclick(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand() != null){
				Player p = e.getPlayer();
				ItemStack is = p.getItemInHand();
				if(is.getType() == Material.PAPER){
					if(is.getItemMeta().hasDisplayName()){
						if(is.getItemMeta().getDisplayName().equalsIgnoreCase("§6[ §f스탯권 §6]§f")){
							Stat s = StatAPI.getStat(p);
							s.addSp(1);
							p.sendMessage("§6[ §f스탯 §6]§f 스탯포인트를 획득했습니다.");
							if(is.getAmount()==1){p.setItemInHand(new ItemStack(0)); p.updateInventory(); }
							else {is.setAmount(is.getAmount()-1);}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity){
			Player p = (Player)e.getDamager();
			LivingEntity t = (LivingEntity)e.getEntity();
			Stat s = StatAPI.getStat(p);
			
			if(Math.random() < s.getCrit()*0.01*main.crit) e.setDamage((e.getDamage()+(int)(s.getAtk()*main.atk))*2);
			else e.setDamage(e.getDamage()+(int)(s.getAtk()*main.atk));
			t.setFireTicks((int) (20*s.getFire()*main.fire));
			t.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (20*s.getPoison()*main.poison), 1));

			t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) (20*s.getWither()*main.wither), 1));

			t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (20*s.getSil()*main.sil), 1));

			t.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) (20*s.getMul()*main.mul), 1));

			t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, (int) (20*s.getPiro()*main.piro), 1));
			
			
			
			if(Math.random() < s.getStun()*0.01*main.stun){
				t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 50));
				t.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 50));
				t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 50));
			}
			if(Math.random() < s.getNay()*0.01*main.nay) t.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 3));
			if(Math.random() < s.getKnockback()*0.01*main.knockback){
				t.setVelocity(p.getVelocity().add(p.getLocation().toVector().subtract(t.getLocation()
							.toVector()).normalize()
							.multiply(-3)));
			}
			if(Math.random() < s.getDrain()*0.01*main.drain) p.setHealth((p.getHealth() + 2) < p.getMaxHealth() ? p.getHealth() + 2 : p.getMaxHealth());
		}
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			Stat s = StatAPI.getStat(p);
			
			
			if(Math.random() < s.getGen()*0.01*main.gen) p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 1));
			if(Math.random() < s.getThorn()*0.01*main.thorn && e.getDamager() instanceof Player){ ((Player)e.getDamager()).damage(e.getDamage(), p); e.setCancelled(true);}
			
			e.setDamage(e.getDamage()-(int)(s.getDef()*main.def) > 1 ? e.getDamage()-(int)(s.getDef()*main.def) : 1);
		}
	}
	/*
	@EventHandler(priority = EventPriority.HIGH)
	public static void onEntityDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			Stat s = StatAPI.getStat(p);
			e.setDamage(e.getDamage()-(int)(s.getDef()*main.def) > 1 ? e.getDamage()-(int)(s.getDef()*main.def) : 1);
		}
	}
	*/
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Stat s = StatAPI.getStat(e.getPlayer());
		if(e.getBlock().getType() == Material.COAL_ORE) s.addExp(main.coal);
		if(e.getBlock().getType() == Material.IRON_ORE) s.addExp(main.iron);
		if(e.getBlock().getType() == Material.GOLD_ORE) s.addExp(main.gold);
		if(e.getBlock().getType() == Material.REDSTONE_ORE) s.addExp(main.reds);
		if(e.getBlock().getType() == Material.LAPIS_ORE) s.addExp(main.lapis);
		if(e.getBlock().getType() == Material.DIAMOND_ORE) s.addExp(main.diamond);
		if(e.getBlock().getType() == Material.EMERALD_ORE) s.addExp(main.emerald);
		if(e.getPlayer().getItemInHand()!= null){
			if(e.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE ||
					e.getPlayer().getItemInHand().getType() == Material.GOLD_PICKAXE ||
					e.getPlayer().getItemInHand().getType() == Material.IRON_PICKAXE ||
					e.getPlayer().getItemInHand().getType() == Material.STONE_PICKAXE ||
					e.getPlayer().getItemInHand().getType() == Material.WOOD_PICKAXE){
				if(Math.random() < s.getMineluck()*0.01*main.mineluck){
					if(e.getBlock().getDrops().iterator().hasNext())
						e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), e.getBlock().getDrops().iterator().next());
				}
			}
			if(e.getBlock().getType() == Material.WHEAT ||
					e.getBlock().getType() == Material.MELON_BLOCK ||
					e.getBlock().getType() == Material.PUMPKIN ||
					e.getBlock().getType() == Material.CARROT ||
					e.getBlock().getType() == Material.COCOA||
					e.getBlock().getType() == Material.GRASS||
					e.getBlock().getType() == Material.LEAVES){
				if(Math.random() < s.getFarmluck()*0.01*main.farmluck){
					if(e.getBlock().getDrops().iterator().hasNext())
						e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), e.getBlock().getDrops().iterator().next());
				}
			}
			
		}
		s.refresh();
	}
	@EventHandler
	public void onKill(EntityDeathEvent e){
		if(e.getEntity().getKiller() != null){
			Stat s = StatAPI.getStat(e.getEntity().getKiller());
			if(e.getEntity().getType() == EntityType.CREEPER) s.addExp(main.creeper);
			if(e.getEntity().getType() == EntityType.SKELETON) s.addExp(main.skeletone);
			if(e.getEntity().getType() == EntityType.SPIDER || e.getEntity().getType() == EntityType.CAVE_SPIDER) s.addExp(main.spider);
			if(e.getEntity().getType() == EntityType.ZOMBIE) s.addExp(main.zombie);
			if(e.getEntity().getType() == EntityType.SLIME) s.addExp(main.slime);
			if(e.getEntity().getType() == EntityType.GHAST) s.addExp(main.ghast);
			if(e.getEntity().getType() == EntityType.PIG_ZOMBIE) s.addExp(main.pigman);
			if(e.getEntity().getType() == EntityType.ENDERMAN) s.addExp(main.enderman);
			if(e.getEntity().getType() == EntityType.SILVERFISH) s.addExp(main.silverfish);
			if(e.getEntity().getType() == EntityType.BLAZE) s.addExp(main.blaze);
			if(e.getEntity().getType() == EntityType.MAGMA_CUBE) s.addExp(main.magmacube);
			if(e.getEntity().getType() == EntityType.PIG) s.addExp(main.pig);
			if(e.getEntity().getType() == EntityType.SHEEP) s.addExp(main.sheep);
			if(e.getEntity().getType() == EntityType.COW || e.getEntity().getType() == EntityType.MUSHROOM_COW) s.addExp(main.cow);
			if(e.getEntity().getType() == EntityType.CHICKEN) s.addExp(main.chicken);
			if(e.getEntity().getType() == EntityType.SQUID) s.addExp(main.squid);
			if(e.getEntity().getType() == EntityType.WOLF) s.addExp(main.wolf);;
			if(e.getEntity().getType() == EntityType.OCELOT) s.addExp(main.ocelot);;
			if(e.getEntity().getType() == EntityType.VILLAGER) s.addExp(main.villager);;
			if(e.getEntity().getType() == EntityType.PLAYER) s.addExp(main.player);
			s.refresh();
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		
		if(e.getInventory().getName().equalsIgnoreCase("내 스탯")){
			Inventory iv = e.getInventory();
			ItemStack is = e.getCurrentItem();
			Player p = (Player)e.getInventory().getHolder();
			Stat s = StatAPI.getStat(p);
			if(e.isLeftClick()){
				if(is != null){
						if(is.getType() == Material.IRON_SWORD){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
							}else{
								s.setAtkp(s.getAtkp()+1);
								s.addSpp(1);
								
								iv.setItem(27, StatAPI.getICON(Material.EMERALD, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+"남은 스탯 포인트 : "+s.getSp()));
								iv.setItem(10, StatAPI.getICON(Material.IRON_SWORD, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 공격력 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getAtk()+s.getAtkp())*main.atk+ChatColor.YELLOW+
										"("+main.atk+"x("+s.getAtk()+"+"+s.getAtkp()+"))",ChatColor.WHITE+"의 추가 피해를 줍니다."));
								
							}
						}
						if(is.getType() == Material.DIAMOND_CHESTPLATE){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setDefp(s.getDefp()+1);
								s.addSpp(1);
								iv.setItem(11, StatAPI.getICON(Material.DIAMOND_CHESTPLATE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 방어력 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+(s.getDef()+s.getDefp())*main.def+ChatColor.YELLOW+
										"("+main.def+"x("+s.getDef()+"+"+s.getDefp()+"))",ChatColor.WHITE+" 감소된 피해를 받습니다."));
								
								
								
							}
						}
						if(is.getType() == Material.APPLE){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setHpp(s.getHpp()+1);
								s.addSpp(1);
								iv.setItem(12, StatAPI.getICON(Material.APPLE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 체력 ]", ChatColor.WHITE+""+ChatColor.GREEN+""+(s.getHp()+s.getHpp())*main.hp+ChatColor.YELLOW+
										"("+main.hp+"x("+s.getHp()+"+"+s.getHpp()+"))",ChatColor.WHITE+"의 추가 체력을 얻습니다."));
								
								
								
							}
						}
						if(is.getType() == Material.NETHER_WARTS){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setDrainp(s.getDrainp()+1);
								s.addSpp(1);
								iv.setItem(13, StatAPI.getICON(Material.NETHER_WARTS, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 흡혈 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getDrain()+s.getDrainp())*main.drain+ChatColor.YELLOW+
										"("+main.drain+"x("+s.getDrain()+"+"+s.getDrainp()+"))%",ChatColor.WHITE+"의 확률로 체력을 회복합니다."));
								
							}
						}
						if(is.getType() == Material.GOLD_BOOTS){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setDexp(s.getDexp()+1);
								s.addSpp(1);
								iv.setItem(14, StatAPI.getICON(Material.GOLD_BOOTS, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 민첩성 ]", ChatColor.WHITE+"이동속도가 "+ChatColor.GREEN+""+(s.getDex()+s.getDexp())*main.dex+ChatColor.YELLOW+
										"("+main.dex+"x("+s.getDex()+"+"+s.getDexp()+"))",ChatColor.WHITE+"만큼 빨라집니다."));
								
								
								
							}
						}
						if(is.getType() == Material.GOLDEN_APPLE){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setGenp(s.getGenp()+1);
								s.addSpp(1);
								
								iv.setItem(15, StatAPI.getICON(Material.GOLDEN_APPLE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 재생 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+(s.getGen()+s.getGenp())*main.gen+ChatColor.YELLOW+
										"("+main.gen+"x("+s.getGen()+"+"+s.getGenp()+"))%",ChatColor.WHITE+"의 확률로 재생효과를 얻습니다."));
								
								
							}
						}
						if(is.getType() == Material.BONE){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setStunp(s.getStunp()+1);
								s.addSpp(1);
								iv.setItem(16, StatAPI.getICON(Material.BONE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 스턴 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getStun()+s.getStunp())*main.stun+ChatColor.YELLOW+
										"("+main.stun+"x("+s.getStun()+"+"+s.getStunp()+"))%",ChatColor.WHITE+"의 확률로 적을 스턴시킵니다."));
								
								
								
							}
						}
						if(is.getType() == Material.FIREBALL){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setFirep(s.getFirep()+1);
								s.addSpp(1);
								iv.setItem(19, StatAPI.getICON(Material.FIREBALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 발화 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getFire()+s.getFirep())*main.fire+ChatColor.YELLOW+
										"("+main.fire+"x("+s.getFire()+"+"+s.getFirep()+"))",ChatColor.WHITE+"초간 적에게 불을 붙입니다."));
								
							}
						}
						if(is.getType() == Material.FIREWORK){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setKnockbackp(s.getKnockbackp()+1);
								s.addSpp(1);
								iv.setItem(20, StatAPI.getICON(Material.FIREWORK, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 넉백 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getKnockback()+s.getKnockbackp())*main.knockback+ChatColor.YELLOW+
										"("+main.knockback+"x("+s.getKnockback()+"+"+s.getKnockbackp()+"))%",ChatColor.WHITE+"의 확률로 넉백시킵니다."));
								
								
								
							}
						}
						if(is.getType() == Material.FEATHER){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setThornp(s.getThornp()+1);
								s.addSpp(1);
								iv.setItem(21, StatAPI.getICON(Material.FEATHER, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 반사 ]", ChatColor.WHITE+"피격 시 "+ChatColor.GREEN+""+(s.getThorn()+s.getThornp())*main.thorn+ChatColor.YELLOW+
										"("+main.thorn+"x("+s.getThorn()+"+"+s.getThornp()+"))%",ChatColor.WHITE+"확률로 데미지를 반사합니다."));
								
								
								
							}
						}
						if(is.getType() == Material.SLIME_BALL){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setPoisonp(s.getPoisonp()+1);
								s.addSpp(1);
								iv.setItem(22, StatAPI.getICON(Material.SLIME_BALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 독 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getPoison()+s.getPoisonp())*main.poison+ChatColor.YELLOW+
										"("+main.poison+"x("+s.getPoison()+"+"+s.getPoisonp()+"))",ChatColor.WHITE+"초간 독 피해를 줍니다."));
								
								
								
							}
						}
						if(is.getType() == Material.COAL){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setWitherp(s.getWitherp()+1);
								s.addSpp(1);
								iv.setItem(23, StatAPI.getICON(Material.COAL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 위더 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getWither()+s.getWitherp())*main.wither+ChatColor.YELLOW+
										"("+main.wither+"x("+s.getWither()+"+"+s.getWitherp()+"))",ChatColor.WHITE+"초간 위더효과를 줍니다."));
								
								
								
							}
						}
						if(is.getType() == Material.SNOW_BALL){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setCritp(s.getCritp()+1);
								s.addSpp(1);
								iv.setItem(24, StatAPI.getICON(Material.SNOW_BALL, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 치명타 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getCrit()+s.getCritp())*main.crit+ChatColor.YELLOW+
										"("+main.crit+"x("+s.getCrit()+"+"+s.getCritp()+"))%",ChatColor.WHITE+"의 확률로 2배의 피해를 줍니다."));
								
								
								
							}
						}
						if(is.getType() == Material.WEB){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setNayp(s.getNayp()+1);
								s.addSpp(1);
								iv.setItem(25, StatAPI.getICON(Material.WEB, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 나약함 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getNay()+s.getNayp())*main.nay+ChatColor.YELLOW+
										"("+main.nay+"x("+s.getNay()+"+"+s.getNayp()+"))%",ChatColor.WHITE+"의 확률로 나약함을 줍니다."));
								
								
								
							}
						}
						if(is.getType() == Material.DIAMOND_PICKAXE){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setMineluckp(s.getMineluckp()+1);
								s.addSpp(1);
								iv.setItem(29, StatAPI.getICON(Material.DIAMOND_PICKAXE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 광물행운 ]", ChatColor.WHITE+"광물을 캘 시 "+ChatColor.GREEN+""+(s.getMineluck()+s.getMineluckp())*main.mineluck+ChatColor.YELLOW+
										"("+main.mineluck+"x("+s.getMineluck()+"+"+s.getMineluckp()+"))%",ChatColor.WHITE+"의 확률로 행운 효과를 받습니다."));
								
								
								
							}
						}
						if(is.getType() == Material.IRON_HOE){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setFarmluckp(s.getFarmluckp()+1);
								s.addSpp(1);
								
								iv.setItem(30, StatAPI.getICON(Material.IRON_HOE, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 작물행운 ]", ChatColor.WHITE+"작물 채집 시 "+ChatColor.GREEN+""+(s.getFarmluck()+s.getFarmluckp())*main.farmluck+ChatColor.YELLOW+
										"("+main.farmluck+"x("+s.getFarmluck()+"+"+s.getFarmluckp()+"))%",ChatColor.WHITE+"의 확률로 행운 효과를 받습니다."));
								
								
							}
						}
						if(is.getType() == Material.RAW_FISH){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setMulp(s.getMulp()+1);
								s.addSpp(1);
								iv.setItem(31, StatAPI.getICON(Material.RAW_FISH, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 멀미 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getMul()+s.getMulp())*main.mul+ChatColor.YELLOW+
										"("+main.mul+"x("+s.getMul()+"+"+s.getMulp()+"))",ChatColor.WHITE+"초간 멀미 효과를 줍니다."));
								
								
								
							}
						}
						if(is.getType() == Material.STRING){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setSilp(s.getSilp()+1);
								s.addSpp(1);
								iv.setItem(32, StatAPI.getICON(Material.STRING, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 실명 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getSil()+s.getSilp())*main.sil+ChatColor.YELLOW+
										"("+main.sil+"x("+s.getSil()+"+"+s.getSilp()+"))",ChatColor.WHITE+"초간 실명 효과를 줍니다."));
								
								
								
							}
						}
						if(is.getType() == Material.BRICK){
							if(s.getSp()<=s.getSpp()){
								p.sendMessage("§6[ §f스탯 §6]§f "+ChatColor.RED+"스탯포인트가 부족합니다!");
								
							}else{
								s.setPirop(s.getPirop()+1);
								s.addSpp(1);
								iv.setItem(33, StatAPI.getICON(Material.BRICK, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+
										" [ 피로 ]", ChatColor.WHITE+"공격 시 "+ChatColor.GREEN+""+(s.getPiro()+s.getPirop())*main.piro+ChatColor.YELLOW+
										"("+main.piro+"x("+s.getPiro()+"+"+s.getPirop()+"))",ChatColor.WHITE+"초간 피로 효과를 줍니다."));
								
								
							}
						}
						if(is.getType() == Material.SIGN){
							s.setAtk(s.getAtk()+s.getAtkp());
							s.setCrit(s.getCrit()+s.getCritp());
							s.setDef(s.getDef()+s.getDefp());
							s.setDex(s.getDex()+s.getDexp());
							s.setDrain(s.getDrain()+s.getDrainp());
							s.setFarmluck(s.getFarmluck()+s.getFarmluckp());
							s.setFire(s.getFire()+s.getFirep());
							s.setGen(s.getGen()+s.getGenp());
							s.setHp(s.getHp()+s.getHpp());
							s.setKnockback(s.getKnockback()+s.getKnockbackp());
							s.setMineluck(s.getMineluck()+s.getMineluckp());
							s.setMul(s.getMul()+s.getMulp());
							s.setNay(s.getNay()+s.getNayp());
							s.setPiro(s.getPiro()+s.getPirop());
							s.setPoison(s.getPoison()+s.getPoisonp());
							s.setSil(s.getSil()+s.getSilp());
							s.setStun(s.getStun()+s.getStunp());
							s.setThorn(s.getThorn()+s.getThornp());
							s.setWither(s.getWither()+s.getWitherp());
							s.addSp(-s.getSpp());
							
							s.setAtkp(0);
							s.setCritp(0);
							s.setDefp(0);
							s.setDexp(0);
							s.setDrainp(0);
							s.setFarmluckp(0);
							s.setFirep(0);
							s.setGenp(0);
							s.setHpp(0);
							s.setKnockbackp(0);
							s.setMineluckp(0);
							s.setMulp(0);
							s.setNayp(0);
							s.setPirop(0);
							s.setPoisonp(0);
							s.setSilp(0);
							s.setThornp(0);
							s.setStunp(0);
							s.setWitherp(0);
							s.setSpp(0);
							s.refresh();
							p.sendMessage("§6[ §f스탯 §6]§f 저장되었습니다.");
							p.closeInventory();
						}
				}
			}
			iv.setItem(27, StatAPI.getICON(Material.EMERALD, ""+ChatColor.AQUA+ChatColor.AQUA+ChatColor.AQUA+"남은 스탯 포인트 : "+(s.getSp()-s.getSpp())));
			e.setCursor(null);
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void asffe(InventoryCloseEvent e){
		if(e.getInventory().getName().equalsIgnoreCase("내 스탯")){
			Inventory iv = e.getInventory();
			Player p = (Player)e.getInventory().getHolder();
			Stat s = StatAPI.getStat(p);
			s.setAtkp(0);
			s.setCritp(0);
			s.setDefp(0);
			s.setDexp(0);
			s.setDrainp(0);
			s.setFarmluckp(0);
			s.setFirep(0);
			s.setGenp(0);
			s.setHpp(0);
			s.setKnockbackp(0);
			s.setMineluckp(0);
			s.setMulp(0);
			s.setNayp(0);
			s.setPirop(0);
			s.setPoisonp(0);
			s.setSilp(0);
			s.setThornp(0);
			s.setStunp(0);
			s.setWitherp(0);
			s.setSpp(0);
			p.sendMessage("§6[ §f스탯 §6]§f 취소되었습니다.");
			s.refresh();
		}
	}
}
