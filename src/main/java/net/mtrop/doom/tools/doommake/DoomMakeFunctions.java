package net.mtrop.doom.tools.doommake;

import static com.blackrook.rookscript.lang.ScriptFunctionUsage.type;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.blackrook.rookscript.ScriptInstance;
import com.blackrook.rookscript.ScriptValue;
import com.blackrook.rookscript.ScriptValue.Type;
import com.blackrook.rookscript.lang.ScriptFunctionType;
import com.blackrook.rookscript.lang.ScriptFunctionUsage;
import com.blackrook.rookscript.resolvers.ScriptFunctionResolver;
import com.blackrook.rookscript.resolvers.hostfunction.EnumFunctionResolver;
import com.blackrook.rookscript.struct.PatternUtils;

import net.mtrop.doom.struct.io.IOUtils;
import net.mtrop.doom.tools.common.Common;

/**
 * Script functions specific to DoomMake.
 * @author Matthew Tropiano
 */
public enum DoomMakeFunctions implements ScriptFunctionType
{
	CLEANDIR(2)
	{
		@Override
		protected Usage usage() 
		{
			return ScriptFunctionUsage.create()
				.instructions(
					"Recursively deletes all files and directories inside a target directory."
				)
				.parameter("path", 
					type(Type.STRING, "Path to directory."),
					type(Type.OBJECTREF, "File", "Path to directory.")
				)
				.parameter("deleteTop", 
					type(Type.BOOLEAN, "If true, also delete the provided directory as well.")
				)
				.returns(
					type(Type.NULL, "If [path] is null."),
					type(Type.BOOLEAN, "True if the directory existed and was cleaned, false otherwise."),
					type(Type.ERROR, "Security", "If the OS is preventing the read.")
				)
			;
		}
		
		@Override
		public boolean execute(ScriptInstance scriptInstance, ScriptValue returnValue)
		{
			ScriptValue temp = CACHEVALUE1.get();
			try 
			{
				scriptInstance.popStackValue(temp);
				boolean deleteTop = temp.asBoolean();
				
				File file = popFile(scriptInstance, temp);
				
				try {
					if (file == null)
						returnValue.setNull();
					else if (!file.exists())
						returnValue.set(false);
					else
						returnValue.set(Common.cleanDirectory(file, deleteTop));
				} catch (SecurityException e) {
					returnValue.setError("Security", e.getMessage(), e.getLocalizedMessage());
				}
				return true;
			}
			finally
			{
				temp.setNull();
			}
		}
	},

	COPYFILE(3)
	{
		@Override
		protected Usage usage()
		{
			return ScriptFunctionUsage.create()
				.instructions(
					"Copies a file from one path to another, optionally creating directories for it. " +
					"If the destination file exists, it is overwritten."
				)
				.parameter("srcFile", 
					type(Type.STRING, "Path to source file."),
					type(Type.OBJECTREF, "File", "Path to source file.")
				)
				.parameter("destFile", 
					type(Type.STRING, "Path to destination file."),
					type(Type.OBJECTREF, "File", "Path to destination file.")
				)
				.parameter("createDirs", 
					type(Type.BOOLEAN, "If true, create the directories for the destination, if not made.")
				)
				.returns(
					type(Type.NULL, "If either file is null."),
					type(Type.BOOLEAN, "True if the file was copied successfully, false if not."),
					type(Type.ERROR, "BadFile", "If the source file does not exist."),
					type(Type.ERROR, "IOError", "If a read or write error occurs."),
					type(Type.ERROR, "Security", "If the OS is preventing the read or write.")
				)
			;
		}

		@Override
		public boolean execute(ScriptInstance scriptInstance, ScriptValue returnValue)
		{
			ScriptValue temp = CACHEVALUE1.get();
			try 
			{
				scriptInstance.popStackValue(temp);
				boolean createDirs = temp.asBoolean();
				
				File destFile = popFile(scriptInstance, temp);
				File srcFile = popFile(scriptInstance, temp);

				if (srcFile == null || destFile == null)
				{
					returnValue.setNull();
					return true;
				}
				
				copyFile(srcFile, destFile, createDirs, returnValue);
				return true;
			}
			finally
			{
				temp.setNull();
			}
		}
	},
	
	COPYDIR(4)
	{
		@Override
		protected Usage usage()
		{
			return ScriptFunctionUsage.create()
				.instructions(
					"Copies a series of files from one directory to another, replicating the tree in the destination. " +
					"If the destination file exists, it is overwritten."
				)
				.parameter("srcDir",
					type(Type.STRING, "Path to source directory (base path)."),
					type(Type.OBJECTREF, "File", "Path to source directory (base path).")
				)
				.parameter("destDir", 
					type(Type.STRING, "Path to destination directory."),
					type(Type.OBJECTREF, "File", "Path to destination directory.")
				)
				.parameter("recursive",
					type(Type.BOOLEAN, "If true, scan recursively.")
				)
				.parameter("regex",
					type(Type.NULL, "Include everything."),
					type(Type.STRING, "The pattern to match each file path against.")
				)
				.returns(
					type(Type.NULL, "If either file is null."),
					type(Type.LIST, "[STRING, ...]", "The list of copied/created files (destination)."),
					type(Type.ERROR, "BadFile", "If the source or destination directory does not exist."),
					type(Type.ERROR, "BadPattern", "If the input RegEx pattern is malformed."),
					type(Type.ERROR, "IOError", "If a read or write error occurs."),
					type(Type.ERROR, "Security", "If the OS is preventing the read or write.")
				)
			;
		}

		@Override
		public boolean execute(ScriptInstance scriptInstance, ScriptValue returnValue)
		{
			ScriptValue temp = CACHEVALUE1.get();
			try 
			{
				scriptInstance.popStackValue(temp);
				String regex = temp.isNull() ? null : temp.asString();
				scriptInstance.popStackValue(temp);
				boolean recursive = temp.asBoolean();
				File destDir = popFile(scriptInstance, temp);
				File srcDir = popFile(scriptInstance, temp);

				if (srcDir == null || destDir == null)
				{
					returnValue.setNull();
					return true;
				}

				if (!srcDir.isDirectory())
				{
					returnValue.setError("BadFile", "Source directory does not exist.");
					return true;
				}
				if (!destDir.isDirectory())
				{
					returnValue.setError("BadFile", "Destination directory does not exist.");
					return true;
				}
				
				FileFilter filter = ((f) -> true);
				if (regex != null)
				{
					try {
						final Pattern p = PatternUtils.get(regex);
						filter = ((f) -> p.matcher(f.getPath()).matches());
					} catch (PatternSyntaxException e) {
						returnValue.setError("BadPattern", e.getMessage(), e.getLocalizedMessage());
						return true;
					}
				}
				
				returnValue.setEmptyList(128);
				copyDir(srcDir, srcDir, destDir, recursive, filter, returnValue);
				return true;
			}
			finally
			{
				temp.setNull();
			}
		}
	},
	
	// TODO: Zip file list.
	
	;
	
	private final int parameterCount;
	private Usage usage;
	private DoomMakeFunctions(int parameterCount)
	{
		this.parameterCount = parameterCount;
		this.usage = null;
	}
	
	/**
	 * @return a function resolver that handles all of the functions in this enum.
	 */
	public static final ScriptFunctionResolver createResolver()
	{
		return new EnumFunctionResolver(DoomMakeFunctions.values());
	}

	@Override
	public int getParameterCount()
	{
		return parameterCount;
	}

	@Override
	public Usage getUsage()
	{
		if (usage == null)
			usage = usage();
		return usage;
	}
	
	protected abstract Usage usage();

	@Override
	public abstract boolean execute(ScriptInstance scriptInstance, ScriptValue returnValue);

	/**
	 * Pops a variable off the stack and, using a temp variable, extracts a File/String.
	 * @param scriptInstance the script instance.
	 * @param temp the temporary script value.
	 * @return a File object.
	 */
	private static File popFile(ScriptInstance scriptInstance, ScriptValue temp) 
	{
		scriptInstance.popStackValue(temp);
		if (temp.isNull())
			return null;
		else if (temp.isObjectRef(File.class))
			return temp.asObjectType(File.class);
		else
			return new File(temp.asString());
	}
	
	private static void copyDir(File base, File srcDir, File destDir, boolean recursive, FileFilter filter, ScriptValue returnValue)
	{
		for (File f : srcDir.listFiles())
		{
			String treeName = f.getPath().substring(base.getPath().length());
			if (f.isDirectory() && recursive)
				copyDir(base, f, destDir, recursive, filter, returnValue);
			else if (filter.accept(f))
				copyFile(f, new File(destDir.getPath() + treeName), true, returnValue);
			
			if (returnValue.isError())
				break;
		}
	}
	
	private static void copyFile(File srcFile, File destFile, boolean createDirs, ScriptValue returnValue) 
	{
		if (createDirs && !Common.createPathForFile(destFile))
		{
			return;
		}
		
		try (FileInputStream fis = new FileInputStream(srcFile); FileOutputStream fos = new FileOutputStream(destFile)) 
		{
			IOUtils.relay(fis, fos);
			returnValue.listAdd(destFile);
		} 
		catch (FileNotFoundException e) 
		{
			returnValue.setError("BadFile", e.getMessage(), e.getLocalizedMessage());
		}
		catch (IOException e) 
		{
			returnValue.setError("IOError", e.getMessage(), e.getLocalizedMessage());
		}
		catch (SecurityException e) 
		{
			returnValue.setError("Security", e.getMessage(), e.getLocalizedMessage());
		}
	}

	// Threadlocal "stack" values.
	private static final ThreadLocal<ScriptValue> CACHEVALUE1 = ThreadLocal.withInitial(()->ScriptValue.create(null));

}