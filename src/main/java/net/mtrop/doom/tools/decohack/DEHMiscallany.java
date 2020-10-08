package net.mtrop.doom.tools.decohack;

import java.io.IOException;
import java.io.Writer;

import net.mtrop.doom.util.RangeUtils;

/**
 * All other miscellaneous DeHackEd stuff.
 * @author Matthew Tropiano
 */
public class DEHMiscallany implements DEHObject<DEHMiscallany>
{
	private boolean monsterInfightingEnabled;
	private int initialBullets;
	private int initialHealth;
	private int greenArmorClass;
	private int blueArmorClass;
	private int soulsphereHealth;
	private int maxSoulsphereHealth;
	private int megasphereHealth;
	private int godModeHealth;
	private int idfaArmor;
	private int idfaArmorClass;
	private int idkfaArmor;
	private int idkfaArmorClass;
	private int bfgCellsPerShot;
	private int maxHealth;
	private int maxArmor;
	
	/**
	 * Creates a new DEHMiscellany with default values.
	 */
	public DEHMiscallany()
	{
		setMonsterInfightingEnabled(false);
		setInitialBullets(50);
		setInitialHealth(100);
		setGreenArmorClass(1);
		setBlueArmorClass(2);
		setSoulsphereHealth(100);
		setMaxSoulsphereHealth(200);
		setGodModeHealth(100);
		setBFGCellsPerShot(40);
		setIDFAArmor(200);
		setIDFAArmorClass(2);
		setIDKFAArmor(200);
		setIDKFAArmorClass(2);
		setMaxArmor(200);
		setMaxHealth(200);
	}
	
	@Override
	public DEHMiscallany copyFrom(DEHMiscallany source) 
	{
		setMonsterInfightingEnabled(source.monsterInfightingEnabled);
		setInitialBullets(source.initialBullets);
		setInitialHealth(source.initialHealth);
		setGreenArmorClass(source.greenArmorClass);
		setBlueArmorClass(source.blueArmorClass);
		setSoulsphereHealth(source.soulsphereHealth);
		setMaxSoulsphereHealth(source.maxSoulsphereHealth);
		setGodModeHealth(source.godModeHealth);
		setBFGCellsPerShot(source.bfgCellsPerShot);
		setIDFAArmor(source.idfaArmor);
		setIDFAArmorClass(source.idfaArmorClass);
		setIDKFAArmor(source.idkfaArmor);
		setIDKFAArmorClass(source.idkfaArmorClass);
		setMaxArmor(source.maxArmor);
		setMaxHealth(source.maxHealth);
		return this;
	}
	
	/**
	 * Gets if monster infighting is enabled.
	 * @return true if so, false if not.
	 */
	public boolean isMonsterInfightingEnabled() 
	{
		return monsterInfightingEnabled;
	}
	
	/**
	 * Sets if monster infighting is enabled.
	 * @param monsterInfightingEnabled true if so, false if not.
	 * @return this object.
	 */
	public DEHMiscallany setMonsterInfightingEnabled(boolean monsterInfightingEnabled) 
	{
		this.monsterInfightingEnabled = monsterInfightingEnabled;
		return this;
	}
	
	/**
	 * @return the initial amount of player bullets.
	 */
	public int getInitialBullets() 
	{
		return initialBullets;
	}

	/**
	 * Sets the initial amount of player bullets.
	 * @param initialBullets the value.
	 * @return this object.
	 * @throws IllegalArgumentException if the value is in an invalid range.
	 */
	public DEHMiscallany setInitialBullets(int initialBullets) 
	{
		RangeUtils.checkRange("Initial bullets", 0, Integer.MAX_VALUE, initialBullets);
		this.initialBullets = initialBullets;
		return this;
	}
	
	/**
	 * @return the initial amount of player health.
	 */
	public int getInitialHealth() 
	{
		return initialHealth;
	}
	
	/**
	 * Sets the initial amount of player health.
	 * @param initialHealth the value.
	 * @return this object.
	 */
	public DEHMiscallany setInitialHealth(int initialHealth) 
	{
		RangeUtils.checkRange("Initial health", 0, Integer.MAX_VALUE, initialHealth);
		this.initialHealth = initialHealth;
		return this;
	}
	
	/**
	 * @return the green armor class.
	 */
	public int getGreenArmorClass() 
	{
		return greenArmorClass;
	}
	
	public DEHMiscallany setGreenArmorClass(int greenArmorClass)
	{
		RangeUtils.checkRange("Green armor class", 0, Integer.MAX_VALUE, greenArmorClass);
		this.greenArmorClass = greenArmorClass;
		return this;
	}
	
	public int getBlueArmorClass() 
	{
		return blueArmorClass;
	}
	
	public DEHMiscallany setBlueArmorClass(int blueArmorClass) 
	{
		RangeUtils.checkRange("Blue armor class", 0, Integer.MAX_VALUE, blueArmorClass);
		this.blueArmorClass = blueArmorClass;
		return this;
	}
	
	public int getSoulsphereHealth()
	{
		return soulsphereHealth;
	}
	
	public DEHMiscallany setSoulsphereHealth(int soulsphereHealth)
	{
		RangeUtils.checkRange("Soulsphere health", 0, 255, blueArmorClass);
		this.soulsphereHealth = soulsphereHealth;
		return this;
	}
	
	public int getMaxSoulsphereHealth() 
	{
		return maxSoulsphereHealth;
	}
	
	public DEHMiscallany setMaxSoulsphereHealth(int maxSoulsphereHealth) 
	{
		RangeUtils.checkRange("Max soulsphere health", 0, Integer.MAX_VALUE, maxSoulsphereHealth);
		this.maxSoulsphereHealth = maxSoulsphereHealth;
		return this;
	}
	
	public int getMegasphereHealth() 
	{
		return megasphereHealth;
	}
	
	public DEHMiscallany setMegasphereHealth(int megasphereHealth) 
	{
		RangeUtils.checkRange("Megasphere health", 0, Integer.MAX_VALUE, megasphereHealth);
		this.megasphereHealth = megasphereHealth;
		return this;
	}
	
	public int getGodModeHealth() 
	{
		return godModeHealth;
	}
	
	public DEHMiscallany setGodModeHealth(int godModeHealth) 
	{
		RangeUtils.checkRange("God mode health", 0, Integer.MAX_VALUE, godModeHealth);
		this.godModeHealth = godModeHealth;
		return this;
	}
	
	public int getBFGCellsPerShot() 
	{
		return bfgCellsPerShot;
	}
	
	public DEHMiscallany setBFGCellsPerShot(int bfgCellsPerShot) 
	{
		RangeUtils.checkRange("BFG Cells Per Shot", 0, 255, bfgCellsPerShot);
		this.bfgCellsPerShot = bfgCellsPerShot;
		return this;
	}
	
	public int getIDFAArmor()
	{
		return idfaArmor;
	}
	
	public DEHMiscallany setIDFAArmor(int idfaArmor)
	{
		RangeUtils.checkRange("IDFA Armor", 0, Integer.MAX_VALUE, idfaArmor);
		this.idfaArmor = idfaArmor;
		return this;
	}
	
	public int getIDFAArmorClass() 
	{
		return idfaArmorClass;
	}
	
	public DEHMiscallany setIDFAArmorClass(int idfaArmorClass) 
	{
		RangeUtils.checkRange("IDFA Armor Class", 0, Integer.MAX_VALUE, idfaArmorClass);
		this.idfaArmorClass = idfaArmorClass;
		return this;
	}
	
	public int getIDKFAArmor() 
	{
		return idkfaArmor;
	}
	
	public DEHMiscallany setIDKFAArmor(int idkfaArmor) 
	{
		RangeUtils.checkRange("IDKFA Armor", 0, Integer.MAX_VALUE, idkfaArmor);
		this.idkfaArmor = idkfaArmor;
		return this;
	}
	
	public int getIDKFAArmorClass()
	{
		return idkfaArmorClass;
	}
	
	public DEHMiscallany setIDKFAArmorClass(int idkfaArmorClass)
	{
		RangeUtils.checkRange("IDKFA Armor Class", 0, Integer.MAX_VALUE, idkfaArmorClass);
		this.idkfaArmorClass = idkfaArmorClass;
		return this;
	}
	
	public int getMaxArmor() 
	{
		return maxArmor;
	}
	
	public DEHMiscallany setMaxArmor(int maxArmor) 
	{
		RangeUtils.checkRange("Max Armor", 0, Integer.MAX_VALUE, maxArmor);
		this.maxArmor = maxArmor;
		return this;
	}
	
	public int getMaxHealth() 
	{
		return maxHealth;
	}
	
	public DEHMiscallany setMaxHealth(int maxHealth) 
	{
		RangeUtils.checkRange("Max health", 0, Integer.MAX_VALUE, maxHealth);
		this.maxHealth = maxHealth;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof DEHMiscallany)
			return equals((DEHMiscallany)obj);
		return super.equals(obj);
	}
	
	public boolean equals(DEHMiscallany obj) 
	{
		return monsterInfightingEnabled == obj.monsterInfightingEnabled
			&& initialBullets == obj.initialBullets
			&& initialHealth == obj.initialHealth
			&& greenArmorClass == obj.greenArmorClass
			&& blueArmorClass == obj.blueArmorClass
			&& soulsphereHealth == obj.soulsphereHealth
			&& maxSoulsphereHealth == obj.maxSoulsphereHealth
			&& megasphereHealth == obj.megasphereHealth
			&& godModeHealth == obj.godModeHealth
			&& idfaArmor == obj.idfaArmor
			&& idfaArmorClass == obj.idfaArmorClass
			&& idkfaArmor == obj.idkfaArmor
			&& idkfaArmorClass == obj.idkfaArmorClass
			&& bfgCellsPerShot == obj.bfgCellsPerShot
			&& maxHealth == obj.maxHealth
			&& maxArmor == obj.maxArmor
		;
	}	
		
	@Override
	public void writeObject(Writer writer, DEHMiscallany misc) throws IOException
	{
		if (monsterInfightingEnabled != misc.monsterInfightingEnabled)
			writer.append("Monsters Infight = ").append(String.valueOf(221)).append('\n');
		if (initialBullets != misc.initialBullets)
			writer.append("Initial Bullets = ").append(String.valueOf(initialBullets)).append('\n');
		if (initialHealth != misc.initialHealth)
			writer.append("Initial Health = ").append(String.valueOf(initialHealth)).append('\n');
		if (greenArmorClass != misc.greenArmorClass)
			writer.append("Green Armor Class = ").append(String.valueOf(greenArmorClass)).append('\n');
		if (blueArmorClass != misc.blueArmorClass)
			writer.append("Blue Armor Class = ").append(String.valueOf(blueArmorClass)).append('\n');
		if (soulsphereHealth != misc.soulsphereHealth)
			writer.append("Soulsphere Health = ").append(String.valueOf(soulsphereHealth)).append('\n');
		if (maxSoulsphereHealth != misc.maxSoulsphereHealth)
			writer.append("Max Soulsphere = ").append(String.valueOf(maxSoulsphereHealth)).append('\n');
		if (megasphereHealth != misc.megasphereHealth)
			writer.append("Megasphere Health = ").append(String.valueOf(megasphereHealth)).append('\n');
		if (godModeHealth != misc.godModeHealth)
			writer.append("God Mode Health = ").append(String.valueOf(godModeHealth)).append('\n');
		if (idfaArmor != misc.idfaArmor)
			writer.append("IDFA Armor = ").append(String.valueOf(idfaArmor)).append('\n');
		if (idfaArmorClass != misc.idfaArmorClass)
			writer.append("IDFA Armor Class = ").append(String.valueOf(idfaArmorClass)).append('\n');
		if (idkfaArmor != misc.idkfaArmor)
			writer.append("IDKFA Armor = ").append(String.valueOf(idkfaArmor)).append('\n');
		if (idkfaArmorClass != misc.idkfaArmorClass)
			writer.append("IDKFA Armor Class = ").append(String.valueOf(idkfaArmorClass)).append('\n');
		if (bfgCellsPerShot != misc.bfgCellsPerShot)
			writer.append("BFG Cells/Shot = ").append(String.valueOf(bfgCellsPerShot)).append('\n');
		if (maxHealth != misc.maxHealth)
			writer.append("Max Health = ").append(String.valueOf(maxHealth)).append('\n');
		if (maxArmor != misc.maxArmor)
			writer.append("Max Armor = ").append(String.valueOf(maxArmor)).append('\n');
		writer.flush();
	}
	
}