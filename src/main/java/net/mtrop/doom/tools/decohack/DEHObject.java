package net.mtrop.doom.tools.decohack;

import java.io.IOException;
import java.io.Writer;

/**
 * Describes all DeHackEd objects and how to write them.
 * @author Matthew Tropiano
 * @param <SELF> this object's class.
 */
public interface DEHObject<SELF>
{
	/**
	 * Copies this object's values.
	 * @param source the source object.
	 * @return this object.
	 */
	SELF copyFrom(SELF source);
	
	/**
	 * Writes this object to a DeHackEd file stream.
	 * @param writer the writer to write to.
	 * @param original the original object to compare to for writing changed fields.
	 * @throws IOException if a write error occurs.
	 */
	void writeObject(Writer writer, SELF original) throws IOException;
	
}
