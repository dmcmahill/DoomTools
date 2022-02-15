/*******************************************************************************
 * Copyright (c) 2020-2021 Matt Tropiano
 * This program and the accompanying materials are made available under 
 * the terms of the MIT License, which accompanies this distribution.
 ******************************************************************************/
package net.mtrop.doom.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import net.mtrop.doom.WadFile;
import net.mtrop.doom.object.BinaryObject;
import net.mtrop.doom.struct.io.IOUtils;
import net.mtrop.doom.texture.CommonTextureList;
import net.mtrop.doom.texture.DoomTextureList;
import net.mtrop.doom.texture.PatchNames;
import net.mtrop.doom.texture.StrifeTextureList;
import net.mtrop.doom.texture.TextureSet;
import net.mtrop.doom.tools.common.Common;
import net.mtrop.doom.tools.common.ParseException;
import net.mtrop.doom.tools.common.Utility;
import net.mtrop.doom.util.NameUtils;
import net.mtrop.doom.util.TextureUtils;

/**
 * Main class for WADTex.
 * @author Matthew Tropiano
 */
public final class WADTexMain
{
	private static final String DOOM_VERSION = Common.getVersionString("doom");
	private static final String VERSION = Common.getVersionString("wadtex");

	private static final int ERROR_NONE = 0;
	private static final int ERROR_BAD_INPUTOUTPUT_FILE = 1;
	private static final int ERROR_BAD_PARSE = 2;
	private static final int ERROR_MISSING_DATA = 3;

	private static final String SWITCH_HELP = "--help";
	private static final String SWITCH_HELP2 = "-h";
	private static final String SWITCH_VERBOSE = "--verbose";
	private static final String SWITCH_VERBOSE2 = "-v";
	private static final String SWITCH_VERSION = "--version";
	private static final String SWITCH_EXPORT = "--export";
	private static final String SWITCH_EXPORT2 = "-x";
	private static final String SWITCH_IMPORT = "--import";
	private static final String SWITCH_IMPORT2 = "-i";
	private static final String SWITCH_ADDITIVE = "--add";
	private static final String SWITCH_ADDITIVE2 = "-a";
	private static final String SWITCH_STRIFE = "--strife";

	private static final String WADTEX_OUTPUT_HEADER = (new StringBuilder())
		.append("; File generated by WADTEX v").append(VERSION).append(" by Matt Tropiano").append('\n')
		.append("; This is also compatible with DEUTEX!")
	.toString();

	/**
	 * Program options.
	 */
	public static class Options
	{
		private PrintStream stdout;
		private PrintStream stderr;
		private boolean help;
		private boolean version;
		private boolean verbose;
		private boolean additive;
		private boolean strife;
		private Boolean exportMode;
		private File sourceFile;
		private File wadFile;
		
		private Options()
		{
			this.stdout = null;
			this.stderr = null;
			this.help = false;
			this.version = false;
			this.verbose = false;
			this.additive = false;
			this.strife = false;
			this.exportMode = null;
			this.sourceFile = null;
			this.wadFile = null;
		}
		
		public Options setStdout(OutputStream out) 
		{
			this.stdout = new PrintStream(out, true);;
			return this;
		}
		
		public Options setStderr(OutputStream err) 
		{
			this.stderr = new PrintStream(err, true);
			return this;
		}

		public Options setVerbose(boolean verbose)
		{
			this.verbose = verbose;
			return this;
		}
		
		public Options setAdditive(boolean additive)
		{
			this.additive = additive;
			return this;
		}
		
		public Options setStrife(boolean strife)
		{
			this.strife = strife;
			return this;
		}
		
		public Options setExportMode(Boolean exportMode) 
		{
			this.exportMode = exportMode;
			return this;
		}
		
		public Options setSourceFile(File sourceFile) 
		{
			this.sourceFile = sourceFile;
			return this;
		}
		
		public Options setWadFile(File wadFile) 
		{
			this.wadFile = wadFile;
			return this;
		}
		
	}
	
	/**
	 * Utility context.
	 */
	private static class Context
	{
		private Options options;
		
		private Context(Options options)
		{
			this.options = options;
		}

		@SuppressWarnings("unchecked")
		private int call()
		{
			if (options.help)
			{
				splash(options.stdout);
				usage(options.stdout);
				options.stdout.println();
				help(options.stdout);
				return ERROR_NONE;
			}
			
			if (options.version)
			{
				splash(options.stdout);
				return ERROR_NONE;
			}
			
			if (options.wadFile == null)
			{
				options.stderr.println("ERROR: No WAD file specified.");
				usage(options.stdout);
				return ERROR_MISSING_DATA;
			}

			if (options.exportMode == null)
			{
				options.stderr.println("ERROR: Import or export mode not specified.");
				usage(options.stdout);
				return ERROR_MISSING_DATA;
			}

			if (options.sourceFile == null)
			{
				options.stderr.println("ERROR: No source file specified.");
				usage(options.stdout);
				return ERROR_MISSING_DATA;
			}

			WadFile wad = null;
			try 
			{
				if (!options.exportMode && !options.wadFile.exists())
					wad = WadFile.createWadFile(options.wadFile);
				else
					wad = new WadFile(options.wadFile);
			}
			catch (FileNotFoundException e)
			{
				options.stderr.printf("ERROR: File %s not found.\n", options.wadFile.getPath());
				return ERROR_BAD_INPUTOUTPUT_FILE;
			}
			catch (IOException e)
			{
				options.stderr.printf("ERROR: %s.\n", e.getLocalizedMessage());
				return ERROR_BAD_INPUTOUTPUT_FILE;
			}
			catch (SecurityException e)
			{
				options.stderr.printf("ERROR: File %s not readable (access denied).\n", options.wadFile.getPath());
				return ERROR_BAD_INPUTOUTPUT_FILE;
			}
		
			String streamName = null;
			BufferedReader reader = null;
			PrintWriter writer = null;
		
			try
			{
				PatchNames patchNames;
				boolean replacePatchNames = true;
				if ((patchNames = wad.getDataAs("PNAMES", PatchNames.class)) == null)
				{
					patchNames = new PatchNames();
					replacePatchNames = false;
				}
		
				String textureLumpName = NameUtils.toValidEntryName(Common.getFileNameWithoutExtension(options.sourceFile));
				
				CommonTextureList<?> textures;
				boolean replaceTextures;
				boolean strife;
				if ((options.additive || options.exportMode) && wad.contains(textureLumpName))
				{
					replaceTextures = true;
					byte[] data = wad.getData(textureLumpName);
					if (TextureUtils.isStrifeTextureData(data))
					{
						strife = true;
						textures = BinaryObject.create(StrifeTextureList.class, data);
					}
					else
					{
						strife = false;
						textures = BinaryObject.create(DoomTextureList.class, data);
					}
				}
				else
				{
					textures = new DoomTextureList();
					strife = false;
					replaceTextures = false;
				}
				
				// Force Strife format if specified.
				if (options.strife)
					strife = true;
				
				TextureSet textureSet;
				
				if (options.exportMode)
				{
					textureSet = new TextureSet(patchNames, textures);
		
					try
					{
						writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(options.sourceFile), Charset.forName("ASCII")), true);
						streamName = options.sourceFile.getPath();
					}
					catch (IOException e)
					{
						options.stderr.printf("ERROR: File %s not writable.\n", options.sourceFile.getPath());
						return ERROR_BAD_INPUTOUTPUT_FILE;
					}
					catch (SecurityException e)
					{
						options.stderr.printf("ERROR: File %s not writable (access denied).\n", options.sourceFile.getPath());
						return ERROR_BAD_INPUTOUTPUT_FILE;
					}
		
					Utility.writeDEUTEXFile(textureSet, WADTEX_OUTPUT_HEADER, writer);
					options.stdout.printf("Wrote `%s`.\n", streamName);
				}
				else // import mode
				{
					try
					{
						reader = new BufferedReader(new InputStreamReader(new FileInputStream(options.sourceFile)));
						streamName = options.sourceFile.getPath();
					}
					catch (FileNotFoundException e)
					{
						options.stderr.printf("ERROR: File %s not found.\n", options.sourceFile.getPath());
						return ERROR_BAD_INPUTOUTPUT_FILE;
					}
					catch (SecurityException e)
					{
						options.stderr.printf("ERROR: File %s not readable (access denied).\n", options.sourceFile.getPath());
						return ERROR_BAD_INPUTOUTPUT_FILE;
					}
		
					textureSet = Utility.readDEUTEXFile(reader, patchNames, textures);
					if (strife)
						textureSet.export(patchNames, (CommonTextureList<StrifeTextureList.Texture>)(textures = new StrifeTextureList(128)));
					else
						textureSet.export(patchNames, (CommonTextureList<DoomTextureList.Texture>)(textures = new DoomTextureList(128)));
		
					if (replacePatchNames)
					{
						wad.replaceEntry(wad.indexOf("PNAMES"), patchNames);
						if (options.verbose)
							options.stdout.printf("Replaced `PNAMES` in `%s`.\n", options.wadFile.getPath());
					}
					else
					{
						wad.addData("PNAMES", patchNames);
						if (options.verbose)
							options.stdout.printf("Added `PNAMES` to `%s`.\n", options.wadFile.getPath());
					}
					
					if (replaceTextures)
					{
						wad.replaceEntry(wad.indexOf(textureLumpName), textures);
						if (options.verbose)
							options.stdout.printf("Replaced `%s` in `%s`.\n", textureLumpName, options.wadFile.getPath());
					}
					else
					{
						wad.addData(textureLumpName, textures);
						if (options.verbose)
							options.stdout.printf("Added `%s` to `%s`.\n", textureLumpName, options.wadFile.getPath());
					}
					
					options.stdout.printf("Imported into `%s`.\n", options.wadFile.getPath());
				}
				
				return ERROR_NONE;
			}
			catch (IOException e)
			{
				options.stderr.printf("ERROR: %s\n", e.getLocalizedMessage());
				return ERROR_BAD_INPUTOUTPUT_FILE;
			}
			catch (ParseException e)
			{
				options.stderr.printf("ERROR: %s, %s\n", streamName, e.getLocalizedMessage());
				return ERROR_BAD_PARSE;
			}
			finally
			{
				IOUtils.close(reader);
				IOUtils.close(writer);
				IOUtils.close(wad);
			}
		}
		
	}
	
	/**
	 * Reads command line arguments and sets options.
	 * @param out the standard output print stream.
	 * @param err the standard error print stream. 
	 * @param args the argument args.
	 * @return the parsed options.
	 */
	public static Options options(PrintStream out, PrintStream err, String ... args)
	{
		Options options = new Options();
		options.stdout = out;
		options.stderr = err;

		final int STATE_START = 0;
		final int STATE_IMPORTEXPORT = 1;
		int state = STATE_START;
		
		int i = 0;
		while (i < args.length)
		{
			String arg = args[i];
			switch (state)
			{
				case STATE_START:
				{
					if (arg.equals(SWITCH_HELP) || arg.equals(SWITCH_HELP2))
						options.help = true;
					else if (arg.equals(SWITCH_VERBOSE) || arg.equals(SWITCH_VERBOSE2))
						options.verbose = true;
					else if (arg.equals(SWITCH_ADDITIVE) || arg.equals(SWITCH_ADDITIVE2))
						options.additive = true;
					else if (arg.equals(SWITCH_STRIFE))
						options.strife = true;
					else if (arg.equals(SWITCH_VERSION))
						options.version = true;
					else if (arg.equals(SWITCH_EXPORT) || arg.equals(SWITCH_EXPORT2))
					{
						state = STATE_IMPORTEXPORT;
						options.exportMode = true;
					}
					else if (arg.equals(SWITCH_IMPORT) || arg.equals(SWITCH_IMPORT2))
					{
						state = STATE_IMPORTEXPORT;
						options.exportMode = false;
					}
					else
						options.wadFile = new File(arg);
				}
				break;

				case STATE_IMPORTEXPORT:
				{
					options.sourceFile = new File(arg);
					state = STATE_START;
				}
				break;
			}
			i++;
		}
		
		return options;
	}
	
	/**
	 * Calls the utility using a set of options.
	 * @param options the options to call with.
	 * @return the error code.
	 */
	public static int call(Options options)
	{
		return (new Context(options)).call();
	}
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			splash(System.out);
			usage(System.out);
			System.exit(-1);
			return;
		}

		System.exit(call(options(System.out, System.err, args)));
	}

	/**
	 * Prints the splash.
	 * @param out the print stream to print to.
	 */
	private static void splash(PrintStream out)
	{
		out.println("WADTex v" + VERSION + " by Matt Tropiano (using DoomStruct v" + DOOM_VERSION + ")");
	}

	/**
	 * Prints the usage.
	 * @param out the print stream to print to.
	 */
	private static void usage(PrintStream out)
	{
		out.println("Usage: wadtex [--help | -h | --version] [file] [mode] [switches]");
	}

	/**
	 * Prints the help.
	 * @param out the print stream to print to.
	 */
	private static void help(PrintStream out)
	{
		out.println("    --help              Prints help and exits.");
		out.println("    -h");
		out.println();
		out.println("    --version           Prints version, and exits.");
		out.println();
		out.println("[file]:");
		out.println("    <filename>          The WAD file.");
		out.println();
		out.println("[mode]:");
	    out.println("    --export [dstfile]  Export mode.");
	    out.println("    -x [dstfile]        Exports PNAMES and texture lump named [dstfile] from");
	    out.println("                        [file] to [dstfile].");
		out.println();
	    out.println("    --import [srcfile]  Import mode.");
	    out.println("    -i [srcfile]        Imports a DEUTEX file from [srcfile] into [file]");
	    out.println("                        and adds/modifies PNAMES and the texture lump (name is");
	    out.println("                        taken from file name).");
	    out.println("                        WAD file is created if it doesn't exist.");
		out.println();
	    out.println("    --add               Additive mode. If specified on import, this will");
	    out.println("    -a                  append the new data to an existing texture lump.");
	    out.println("                        PNAMES may still be altered.");
		out.println();
	    out.println("    --strife            Force Strife format on import (conversion or new).");
		out.println();
		out.println("[switches]:");
		out.println("    --verbose           Prints verbose output.");
		out.println("    -v");
	}

}
