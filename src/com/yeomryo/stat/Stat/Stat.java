package com.yeomryo.stat.Stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.yeomryo.country.Country.CountryAPI;
import com.yeomryo.stat.main;

public class Stat {
	
	String player;
	
	int level;
	int exp;
	
	int atk; // 공격력
	int def; // 방어력
	int hp; // 체력증가
	int drain; // 흡혈
	int dex; // 민첩성
	int gen; // 재생력
	
	int stun; // 특정확률 스턴
	int fire; // 발화
	int knockback; // 넉백
	int thorn; // 반사
	int poison; // 독
	int wither; // 위더
	int crit; // 크리티컬


	int mineluck; // 광물행운
	int farmluck; // 작물행운
	
	int mul; // 멀미
	int sil; // 실명
	int piro; // 피로
	int nay; // 나약함
	
	int sp; // 스탯포인트
	
	
	//----- 증가량  -----
	//----- 닫으면 0으로 초기화-----

	int atkp; // 공격력
	int defp; // 방어력
	int hpp; // 체력증가
	int drainp; // 흡혈
	int dexp; // 민첩성
	int genp; // 재생력
	
	int stunp; // 특정확률 스턴
	int firep; // 발화
	int knockbackp; // 넉백
	int thornp; // 반사
	int poisonp; // 독
	int witherp; // 위더
	int critp; // 크리티컬


	int mineluckp; // 광물행운
	int farmluckp; // 작물행운
	
	int mulp; // 멀미
	int silp; // 실명
	int pirop; // 피로
	int nayp; // 나약함
	
	int spp; // 스탯포인트
	
	public Stat(){
		this.level = 1;
	}
	
	public Stat(Player p){
		this.player = p.getName();
		this.level = 1;
	}
	
	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		refresh();
	}
	public void addLevel(int level) {
		this.level += level;
		refresh();
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
		while(this.exp>=100+(level-1)*15){
			this.exp-=100+(level-1)*15;
			addLevel(1);
			addSp(1);
		}
		refresh();
	}
	
	
	
	public int getAtkp() {
		return atkp;
	}

	public void setAtkp(int atkp) {
		this.atkp = atkp;
	}

	public int getDefp() {
		return defp;
	}

	public void setDefp(int defp) {
		this.defp = defp;
	}

	public int getHpp() {
		return hpp;
	}

	public void setHpp(int hpp) {
		this.hpp = hpp;
	}

	public int getDrainp() {
		return drainp;
	}

	public void setDrainp(int drainp) {
		this.drainp = drainp;
	}

	public int getDexp() {
		return dexp;
	}

	public void setDexp(int dexp) {
		this.dexp = dexp;
	}

	public int getGenp() {
		return genp;
	}

	public void setGenp(int genp) {
		this.genp = genp;
	}

	public int getStunp() {
		return stunp;
	}

	public void setStunp(int stunp) {
		this.stunp = stunp;
	}

	public int getFirep() {
		return firep;
	}

	public void setFirep(int firep) {
		this.firep = firep;
	}

	public int getKnockbackp() {
		return knockbackp;
	}

	public void setKnockbackp(int knockbackp) {
		this.knockbackp = knockbackp;
	}

	public int getThornp() {
		return thornp;
	}

	public void setThornp(int thornp) {
		this.thornp = thornp;
	}

	public int getPoisonp() {
		return poisonp;
	}

	public void setPoisonp(int poisonp) {
		this.poisonp = poisonp;
	}

	public int getWitherp() {
		return witherp;
	}

	public void setWitherp(int witherp) {
		this.witherp = witherp;
	}

	public int getCritp() {
		return critp;
	}

	public void setCritp(int critp) {
		this.critp = critp;
	}

	public int getMineluckp() {
		return mineluckp;
	}

	public void setMineluckp(int mineluckp) {
		this.mineluckp = mineluckp;
	}

	public int getFarmluckp() {
		return farmluckp;
	}

	public void setFarmluckp(int farmluckp) {
		this.farmluckp = farmluckp;
	}

	public int getMulp() {
		return mulp;
	}

	public void setMulp(int mulp) {
		this.mulp = mulp;
	}

	public int getSilp() {
		return silp;
	}

	public void setSilp(int silp) {
		this.silp = silp;
	}

	public int getPirop() {
		return pirop;
	}

	public void setPirop(int pirop) {
		this.pirop = pirop;
	}

	public int getNayp() {
		return nayp;
	}

	public void setNayp(int nayp) {
		this.nayp = nayp;
	}

	public int getSpp() {
		return spp;
	}

	public void setSpp(int spp) {
		this.spp = spp;
	}

	public void refresh(){
		if(player != null){
			Player p = Bukkit.getPlayer(getPlayer());
			Stat s = this;
			ScoreboardManager sm = Bukkit.getScoreboardManager();
			Scoreboard sb = sm.getNewScoreboard();
			Objective dsp = sb.registerNewObjective("dsp", "x");
			dsp.setDisplaySlot(DisplaySlot.SIDEBAR);
			dsp.setDisplayName(ChatColor.YELLOW+"[ "+s.getPlayer()+" ]");
			if(CountryAPI.hasCountry(p)){
				Score sc3 = dsp.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA+"소속 국가 [ "+CountryAPI.getCountryName(p)+" ]"));
				sc3.setScore(2);
			}
			Score sc1 = dsp.getScore(Bukkit.getOfflinePlayer("LvL : "+ChatColor.GREEN+s.getLevel()));
			Score sc2 = dsp.getScore(Bukkit.getOfflinePlayer("Exp : "+ChatColor.GREEN+getDouble(((double)s.getExp()/(100+(level-1)*15))*100)+"%"));
			sc1.setScore(1);
			sc2.setScore(0);
			p.setScoreboard(sb);
			
			p.setMaxHealth((int) (20+this.getHp()*main.hp));
			p.setWalkSpeed((float)( 0.2+this.dex*main.dex ));
		}
	}
	
	public double getDouble(double d){
		int a = (int)(d*10);
		double s = (double)(a/10);
		return s;
	}
	
	public void addExp(int exp) {
		this.exp += exp;
		while(this.exp>=100+(level-1)*15){
			this.exp-=100+(level-1)*15;
			addLevel(1);
			addSp(1);
		}
		refresh();
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getDrain() {
		return drain;
	}

	public void setDrain(int drain) {
		this.drain = drain;
	}

	public int getDex() {
		return dex;
	}

	public void setDex(int dex) {
		this.dex = dex;
	}

	public int getGen() {
		return gen;
	}

	public void setGen(int gen) {
		this.gen = gen;
	}

	public int getStun() {
		return stun;
	}

	public void setStun(int stun) {
		this.stun = stun;
	}

	public int getFire() {
		return fire;
	}

	public void setFire(int fire) {
		this.fire = fire;
	}

	public int getKnockback() {
		return knockback;
	}

	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	public int getThorn() {
		return thorn;
	}

	public void setThorn(int thorn) {
		this.thorn = thorn;
	}

	public int getPoison() {
		return poison;
	}

	public void setPoison(int poison) {
		this.poison = poison;
	}

	public int getWither() {
		return wither;
	}

	public void setWither(int wither) {
		this.wither = wither;
	}

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getMineluck() {
		return mineluck;
	}

	public void setMineluck(int mineluck) {
		this.mineluck = mineluck;
	}

	public int getFarmluck() {
		return farmluck;
	}

	public void setFarmluck(int farmluck) {
		this.farmluck = farmluck;
	}
	
	public int getMul() {
		return mul;
	}

	public void setMul(int mul) {
		this.mul = mul;
	}

	public int getSil() {
		return sil;
	}

	public void setSil(int sil) {
		this.sil = sil;
	}

	public int getNay() {
		return nay;
	}

	public void setNay(int nay) {
		this.nay = nay;
	}
	public int getPiro() {
		return piro;
	}
	public void setPiro(int piro) {
		this.piro = piro;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	public void addSp(int sp) {
		this.sp += sp;
		refresh();
	}
	public void addSpp(int sp) {
		this.spp += sp;
	}

	public void saveFile() throws IOException{
		File f = new File("yeomryo\\stat");
		f.mkdirs();
		f = new File("yeomryo\\stat\\"+player+".yr");
		FileWriter fw = new FileWriter(f);
		String s=player+"*"+level+"*"+exp+"*"+atk+"*"+def+"*"+hp+"*"+drain+
		"*"+dex+"*"+gen+"*"+stun+"*"+fire+"*"+knockback+"*"+thorn+"*"+poison+"*"+
				wither+"*"+crit+"*"+mineluck+"*"+farmluck+"*"+mul+"*"+sil+"*"+piro+"*"+nay+"*"+sp;
		fw.write(s);
		fw.close();
		System.out.println(f.getPath());
	}
	
	public void loadFile(String path) throws IOException{
		File f = new File(path);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s=br.readLine();
		br.close();
		fr.close();
		this.player = s.split("\\*")[0];
		this.level = Integer.parseInt(s.split("\\*")[1]);
		this.exp = Integer.parseInt(s.split("\\*")[2]);
		this.atk = Integer.parseInt(s.split("\\*")[3]);
		this.def = Integer.parseInt(s.split("\\*")[4]);
		this.hp = Integer.parseInt(s.split("\\*")[5]);
		this.drain = Integer.parseInt(s.split("\\*")[6]);
		this.dex = Integer.parseInt(s.split("\\*")[7]);
		this.gen = Integer.parseInt(s.split("\\*")[8]);
		this.stun = Integer.parseInt(s.split("\\*")[9]);
		this.fire = Integer.parseInt(s.split("\\*")[10]);
		this.knockback = Integer.parseInt(s.split("\\*")[11]);
		this.thorn = Integer.parseInt(s.split("\\*")[12]);
		this.poison = Integer.parseInt(s.split("\\*")[13]);
		this.wither = Integer.parseInt(s.split("\\*")[14]);
		this.crit = Integer.parseInt(s.split("\\*")[15]);
		this.mineluck = Integer.parseInt(s.split("\\*")[16]);
		this.farmluck = Integer.parseInt(s.split("\\*")[17]);
		this.mul = Integer.parseInt(s.split("\\*")[18]);
		this.sil = Integer.parseInt(s.split("\\*")[19]);
		this.piro = Integer.parseInt(s.split("\\*")[20]);
		this.nay = Integer.parseInt(s.split("\\*")[21]);
		this.sp = Integer.parseInt(s.split("\\*")[22]);
	}
}
