/*******************************************************************************
 * Copyright (c) 2020-2021 Matt Tropiano
 * This program and the accompanying materials are made available under 
 * the terms of the MIT License, which accompanies this distribution.
 ******************************************************************************/
package net.mtrop.doom.tools.decohack.contexts;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import net.mtrop.doom.tools.common.Common;
import net.mtrop.doom.tools.decohack.data.DEHAmmo;
import net.mtrop.doom.tools.decohack.data.DEHMiscellany;
import net.mtrop.doom.tools.decohack.data.DEHObject;
import net.mtrop.doom.tools.decohack.data.DEHSound;
import net.mtrop.doom.tools.decohack.data.DEHState;
import net.mtrop.doom.tools.decohack.data.DEHThing;
import net.mtrop.doom.tools.decohack.data.DEHWeapon;
import net.mtrop.doom.tools.decohack.data.enums.DEHActionPointer;
import net.mtrop.doom.tools.decohack.data.enums.DEHActionPointerType;
import net.mtrop.doom.tools.decohack.data.enums.DEHFeatureLevel;
import net.mtrop.doom.tools.decohack.patches.DEHPatch;
import net.mtrop.doom.tools.struct.IntervalMap;

/**
 * Abstract patch context.
 * @author Matthew Tropiano
 * @param <P> DEH patch type.
 */
public abstract class AbstractPatchContext<P extends DEHPatch> implements DEHPatch
{
	private Map<Integer, DEHAmmo> ammo;
	private Map<Integer, DEHSound> sounds;
	private Map<Integer, DEHWeapon> weapons;
	private Map<Integer, DEHThing> things;
	private Map<Integer, DEHState> states;
	private Map<Integer, DEHActionPointer> pointers;
	private DEHMiscellany miscellany;

	private int freeStateCount;
	private int freePointerStateCount;
	private IntervalMap<Boolean> freeStates;
	private IntervalMap<Boolean> protectedStates;
	
	/**
	 * Shadows a DEH object from the source patch to the editable object,
	 * returning it if it has already been shadowed.
	 * @param <T> the object type.
	 * @param index the object index.
	 * @param targetMap the target map to put the object into.
	 * @param fetcher the fetcher function, called if not found.
	 * @return the object or null if not valid.
	 */
	@SuppressWarnings("unchecked")
	protected static <T extends DEHObject<T>> T shadow(int index, Map<Integer, T> targetMap, Function<Integer, T> fetcher)
	{
		T obj;
		if ((obj = targetMap.get(index)) == null)
		{
			T srcObj;
			if ((srcObj = fetcher.apply(index)) != null)
				targetMap.put(index, obj = ((T)Common.create(srcObj.getClass())).copyFrom(srcObj));
		}
		return obj;
	}

	/**
	 * Copies an object from the source patch to the editable object,
	 * returning it if it has already been copied.
	 * @param <T> the object type.
	 * @param key the object key.
	 * @param targetMap the target map to put the object into.
	 * @param fetcher the fetcher function, called if not found.
	 * @return the object or null if not valid.
	 */
	protected static <K, T> T copy(K key, Map<K, T> targetMap, Function<K, T> fetcher)
	{
		T obj;
		if ((obj = targetMap.get(key)) == null)
		{
			T srcObj;
			if ((srcObj = fetcher.apply(key)) != null)
				targetMap.put(key, obj = srcObj);
		}
		return obj;
	}

	/**
	 * Creates a new patch context.
	 */
	public AbstractPatchContext()
	{
		DEHPatch source = getSourcePatch();
		
		this.ammo = new TreeMap<>();
		this.sounds = new HashMap<>(128, 1f);
		this.weapons = new TreeMap<>();
		this.things = new HashMap<>(128, 1f);
		this.states = new HashMap<>(1024, 1f);
		this.pointers = new HashMap<>(512, 1f);
		
		this.miscellany = (new DEHMiscellany()).copyFrom(source.getMiscellany());
		
		this.freeStateCount = 0;
		this.freeStates = new IntervalMap<>(0, getSourcePatch().getStateCount() - 1, false);
		this.protectedStates = new IntervalMap<>(0, getSourcePatch().getStateCount() - 1, false);
		
		// Protect first two states from clear.
		setProtectedState(0, true); // NULL state. 
		setProtectedState(1, true); // Gunflash Light0.
	}
	
	/**
	 * Gets the original source patch state (for restoration or reference).
	 * <p><b>DO NOT ALTER THE CONTENTS OF THIS PATCH. THIS IS A REFERENCE STATE.</b> 
	 * @return the original source patch.
	 */
	public abstract P getSourcePatch();
	
	/**
	 * @return the Doom Version.
	 */
	public abstract int getVersion();
	
	@Override
	public DEHMiscellany getMiscellany() 
	{
		return miscellany;
	}

	@Override
	public int getAmmoCount() 
	{
		return getSourcePatch().getAmmoCount();
	}

	@Override
	public DEHAmmo getAmmo(int index) 
	{
		return shadow(index, ammo, (i) -> getSourcePatch().getAmmo(i));
	}

	@Override
	public int getSoundCount() 
	{
		return getSourcePatch().getSoundCount();
	}

	@Override
	public DEHSound getSound(int index)
	{
		return shadow(index, sounds, (i) -> getSourcePatch().getSound(i));
	}

	@Override
	public int getThingCount() 
	{
		return getSourcePatch().getThingCount();
	}

	@Override
	public DEHThing getThing(int index)
	{
		return shadow(index, things, (i) -> getSourcePatch().getThing(i));
	}

	@Override
	public int getWeaponCount()
	{
		return getSourcePatch().getWeaponCount();
	}

	@Override
	public DEHWeapon getWeapon(int index)
	{
		return shadow(index, weapons, (i) -> getSourcePatch().getWeapon(i));
	}

	@Override
	public int getStateCount()
	{
		return getSourcePatch().getStateCount();
	}

	@Override
	public DEHState getState(int index) 
	{
		return shadow(index, states, (i) -> getSourcePatch().getState(i));
	}

	@Override
	public Integer getStateActionPointerIndex(int stateIndex) 
	{
		return getSourcePatch().getStateActionPointerIndex(stateIndex);
	}

	@Override
	public int getActionPointerCount() 
	{
		return getSourcePatch().getActionPointerCount();
	}

	@Override
	public DEHActionPointer getActionPointer(int index)
	{
		return copy(index, pointers, (i) -> getSourcePatch().getActionPointer(i));
	}

	/**
	 * @return this patch context's supported action pointer type level.
	 */
	public abstract DEHActionPointerType getSupportedActionPointerType();

	/**
	 * @return this patch context's feature level.
	 */
	public abstract DEHFeatureLevel getSupportedFeatureLevel();

	/**
	 * Checks if the provided pointer type is supported by this one.
	 * @param type the provided type.
	 * @return true if so, false if not.
	 */
	public boolean supports(DEHActionPointerType type)
	{
		return getSupportedActionPointerType().supports(type);
	}

	/**
	 * Checks if the provided feature level is supported by this patch.
	 * @param level the provided level.
	 * @return true if so, false if not.
	 */
	public boolean supports(DEHFeatureLevel level)
	{
		return getSupportedFeatureLevel().supports(level);
	}

	/**
	 * Sets the pointer at an action pointer index.
	 * @param index the pointer index.
	 * @param pointer the pointer.
	 * @throws IndexOutOfBoundsException if index is out of bounds.
	 */
	public void setActionPointer(int index, DEHActionPointer pointer)
	{
		if (pointer == null)
			throw new IllegalArgumentException("Pointer cannot be null.");
		pointers.put(index, pointer);
	}

	/**
	 * Gets how many free states there are.
	 * @return the amount of states flagged as "free."
	 */
	public int getFreeStateCount() 
	{
		return freeStateCount;
	}
	
	/**
	 * Gets how many free pointer-having states there are.
	 * @return the amount of pointer-attached states flagged as "free."
	 */
	public int getFreePointerStateCount() 
	{
		return freePointerStateCount;
	}
	
	/**
	 * Gets if a state is flagged as "free".
	 * @param index the index.
	 * @return true if so, false if not.
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public boolean isFreeState(int index)
	{
		return freeStates.get(index);
	}
	
	/**
	 * Marks a state as "free" - painting thing/weapon states will be written to these.
	 * @param index the index to mark as free.
	 * @param state true to set as "free", false to unset.
	 * @throws IllegalStateException if the target state is protected.
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 * @see #protectState(int)
	 */
	public void setFreeState(int index, boolean state)
	{
		if (isProtectedState(index))
			throw new IllegalStateException("State " + index + " is a protected state.");
		boolean prev = freeStates.get(index); 
		freeStates.set(index, state);
		if (prev && !state)
		{
			freeStateCount--;
			if (getStateActionPointerIndex(index) != null)
				freePointerStateCount--;
		}
		else if (!prev && state)
		{
			freeStateCount++;
			if (getStateActionPointerIndex(index) != null)
				freePointerStateCount++;
		}
	}
	
	/**
	 * Gets if a state is flagged as "protected".
	 * @param index the index.
	 * @return true if so, false if not.
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public boolean isProtectedState(int index)
	{
		return protectedStates.get(index);
	}

	/**
	 * Marks a state as "protected" - attempting to free this state or alter it
	 * directly will throw an exception.
	 * @param index the index to mark as protected.
	 * @param state true to set as "protected", false to unset.
	 */
	public void setProtectedState(int index, boolean state)
	{
		protectedStates.set(index, state);
	}

	/**
	 * Searches through the states and flags them as "free" until it hits a "protected" or "free" state.
	 * The state traversal is through the "next state" indices on each state.
	 * @param startingStateIndex the state index to start from.
	 * @return the amount of states freed.
	 */
	public int freeConnectedStates(int startingStateIndex)
	{
		int out = 0;
		int index = startingStateIndex;
		while (!isProtectedState(index) && !isFreeState(index))
		{
			setFreeState(index, true);
			index = getState(index).getNextStateIndex();
			out++;
		}
		return out;
	}
	
	/**
	 * Flags each associated state in a thing as "free".
	 * Each starting state index is taken from the state that the corresponding thing uses.
	 * Each connected state (connected via next state indices) is freed until an already free state
	 * is reached or a protected state is reached. 
	 * @param thingIndex the thing slot index.
	 * @return the amount of states freed.
	 * @see #getThing(int)
	 * @see #setFreeState(int, boolean)
	 * @see #freeConnectedStates(int)
	 */
	public int freeThingStates(int thingIndex)
	{
		int out = 0;
		DEHThing thing = getThing(thingIndex);
		for (String label : thing.getLabels())
			out += freeConnectedStates(thing.getLabel(label));
		return out;
	}
	
	/**
	 * Flags each associated state in a weapon as "free".
	 * Each starting state index is taken from the state that the corresponding weapon uses.
	 * Each connected state (connected via next state indices) is freed until an already free state
	 * is reached or a protected state is reached. 
	 * @param weaponIndex the thing slot index.
	 * @return the amount of states freed.
	 * @see #getWeapon(int)
	 * @see #setFreeState(int, boolean)
	 * @see #freeConnectedStates(int)
	 */
	public int freeWeaponStates(int weaponIndex)
	{
		int out = 0;
		DEHWeapon weapon = getWeapon(weaponIndex);
		for (String label : weapon.getLabels())
			out += freeConnectedStates(weapon.getLabel(label));
		return out;
	}
	
	/**
	 * Searches linearly for the next free state in this context from a starting index.
	 * If the start index is free, it is returned. If a full search completes without finding
	 * a free index, <code>null</code> is returned.
	 * @param startingIndex the starting index.
	 * @return the next free state, or <code>null</code> if none found.
	 */
	public Integer findNextFreeState(int startingIndex)
	{
		return searchNextFreeState(startingIndex, (i) -> !isFillableState(i));
	}
	
	/**
	 * Searches linearly for the next free state WITH an action pointer in this context from a starting index.
	 * If the start index is free, it is returned. If a full search completes without finding
	 * a free index, <code>null</code> is returned.
	 * @param startingIndex the starting index.
	 * @return the next free state, or <code>null</code> if none found.
	 */
	public Integer findNextFreeActionPointerState(int startingIndex)
	{
		return searchNextFreeState(startingIndex, (i) -> 
			!(isFillableState(i) && getStateActionPointerIndex(i) != null)
		);
	}
	
	/**
	 * Searches linearly for the next free state WITHOUT an action pointer in this context from a starting index.
	 * If the start index is free, it is returned. If a full search completes without finding
	 * a free index, <code>null</code> is returned.
	 * @param startingIndex the starting index.
	 * @return the next free state, or <code>null</code> if none found.
	 */
	public Integer findNextFreeNonActionPointerState(int startingIndex)
	{
		return searchNextFreeState(startingIndex, (i) -> 
			!(isFillableState(i) && getStateActionPointerIndex(i) == null)
		);
	}

	/**
	 * Checks if a state is considered "fillable," which means it is both
	 * free and not protected.
	 * Equivalent to: <code>isFreeState(index) && !isProtectedState(index)</code>
	 * @param index the state index to check.
	 * @return true if so, false if not.
	 * @see #isFreeState(int)
	 * @see #isProtectedState(int)
	 */
	public boolean isFillableState(int index)
	{
		return isFreeState(index) && !isProtectedState(index);
	}

	// Search function for free states.
	private Integer searchNextFreeState(int startingIndex, Function<Integer, Boolean> func)
	{
		int i = startingIndex;
		while (func.apply(i))
		{
			i++;
			if (i >= getStateCount())
				i = 0;
			if (i == startingIndex)
				return null;
		}
		return i;
	}
	
}
