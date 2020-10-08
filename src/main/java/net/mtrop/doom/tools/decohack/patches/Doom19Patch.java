package net.mtrop.doom.tools.decohack.patches;

import net.mtrop.doom.tools.decohack.DEHActionPointer;
import net.mtrop.doom.tools.decohack.DEHAmmo;
import net.mtrop.doom.tools.decohack.DEHMiscallany;
import net.mtrop.doom.tools.decohack.DEHPatch;
import net.mtrop.doom.tools.decohack.DEHSound;
import net.mtrop.doom.tools.decohack.DEHState;
import net.mtrop.doom.tools.decohack.DEHThing;
import net.mtrop.doom.tools.decohack.DEHWeapon;
import net.mtrop.doom.tools.decohack.DEHWeapon.Ammo;

import static net.mtrop.doom.tools.decohack.patches.StateConstants.*;

/**
 * Patch implementation for Doom 1.9.
 * @author Matthew Tropiano
 */
public class Doom19Patch implements DEHPatch
{
	public static final int AMMO_BULLETS = 0;
	public static final int AMMO_SHELLS  = 1;
	public static final int AMMO_CELLS   = 2;
	public static final int AMMO_ROCKETS = 3;

	public static final int WEAPON_FIST           = 0;
	public static final int WEAPON_PISTOL         = 1;
	public static final int WEAPON_SHOTGUN        = 2;
	public static final int WEAPON_CHAINGUN       = 3;
	public static final int WEAPON_ROCKETLAUNCHER = 4;
	public static final int WEAPON_PLASMA         = 5;
	public static final int WEAPON_BFG            = 6;
	public static final int WEAPON_CHAINSAW       = 7;
	public static final int WEAPON_SUPERSHOTGUN   = 8;

	private static final DEHMiscallany DEHMISC = new DEHMiscallany();
	
	private static final DEHAmmo[] DEHAMMO = 
	{
		(new DEHAmmo()).setName("Bullets")
			.setMax(200).setPickup(10),
		(new DEHAmmo()).setName("Shells")
			.setMax(50).setPickup(4),
		(new DEHAmmo()).setName("Cells")
			.setMax(300).setPickup(20),
		(new DEHAmmo()).setName("Rockets")
			.setMax(50).setPickup(1),
	};

	private static final DEHWeapon[] DEHWEAPON = 
	{
		DEHWeapon.create("Fist",            Ammo.INFINITE, S_PUNCHUP, S_PUNCHDOWN, S_PUNCH, S_PUNCH1, S_NULL),
		DEHWeapon.create("Pistol",          Ammo.BULLETS,  S_PISTOLUP, S_PISTOLDOWN, S_PISTOL, S_PISTOL1, S_PISTOLFLASH),
		DEHWeapon.create("Shotgun",         Ammo.SHELLS,   S_SGUNUP, S_SGUNDOWN, S_SGUN, S_SGUN1, S_SGUNFLASH1),
		DEHWeapon.create("Chaingun",        Ammo.BULLETS,  S_CHAINUP, S_CHAINDOWN, S_CHAIN, S_CHAIN1, S_CHAINFLASH1),
		DEHWeapon.create("Rocket launcher", Ammo.ROCKETS,  S_MISSILEUP, S_MISSILEDOWN, S_MISSILE, S_MISSILE1, S_MISSILEFLASH1),
		DEHWeapon.create("Plasma rifle",    Ammo.CELLS,    S_PLASMAUP, S_PLASMADOWN, S_PLASMA, S_PLASMA1, S_PLASMAFLASH1),
		DEHWeapon.create("BFG9000",         Ammo.CELLS,    S_BFGUP, S_BFGDOWN, S_BFG, S_BFG1, S_BFGFLASH1),
		DEHWeapon.create("Chainsaw",        Ammo.INFINITE, S_SAWUP, S_SAWDOWN, S_SAW, S_SAW1, S_NULL),
		DEHWeapon.create("Super-shotgun",   Ammo.SHELLS,   S_DSGUNUP, S_DSGUNDOWN, S_DSGUN, S_DSGUN1, S_DSGUNFLASH1)
	};

	private static final String[] DEHSTRINGS = 
	{
		"CODEC: Testing I/O address: %04x\n",
		"CODEC: Starting the \"busy\" test.\n",
		"CODEC: The \"busy\" test failed.\n",
		"CODEC: Passed the \"busy\" test.\n",
		"CODEC: Starting the version test.\n",
		"CODEC: Failed the version inspection.\n",
		"CODEC: Passed the version inspection.\n",
		"CODEC: Starting the misc. write test.\n",
		"CODEC: Failed the misc. write test.\n",
		"CODEC: Passed all I/O port inspections.\n",
		"=",
		"SNDSCAPE",
		"\\SNDSCAPE.INI",
		"r",
		"Port",
		"DMA",
		"IRQ",
		"WavePort",
		" out of GUS RAM:",
		"%d%c",
		"GF1PATCH110",
		"110",
		",%d",
		"Ultrasound @ Port:%03Xh, IRQ:%d,  DMA:%d\n",
		"Sizing GUS RAM:",
		"BANK%d:OK ",
		"BANK%d:N/A ",
		"\n",
		"ULTRADIR",
		"\\midi\\",
		"\r\n",
		"Loading GUS Patches\n",
		".pat",
		"%s",
		" - FAILED\n",
		" - OK\n",
		"ULTRASND",
		"NO MVSOUND.SYS\n",
		"PAS IRQ: %d  DMA:%d\n",
		"Pro Audio Spectrum is NOT version 1.01.\n",
		"ECHO Personal Sound System Enabled.\n",
		"BLASTER",
		"%x",
		"Pro Audio Spectrum 3D JAZZ\n",
		"\nDSP Version: %x.%02x\n",
		"        IRQ: %d\n",
		"        DMA: %d\n",
		"GENMIDI.OP2",
		"MixPg: ",
		"WAVE",
		"fmt ",
		"data",
		"MThd",
		"MTrk",
		"MUS\u001a",
		"DMXTRACE",
		"DMXOPTION",
		"-opl3",
		"-phase",
		"Bad I_UpdateBox (%i, %i, %i, %i)",
		"PLAYPAL",
		"0x%x\n",
		"-nomouse",
		"Mouse: not present\n",
		"Mouse: detected\n",
		"-nojoy",
		"joystick not found\n",
		"joystick found\n",
		"CENTER the joystick and press button 1:",
		"\nPush the joystick to the UPPER LEFT corner and press button 1:",
		"\nPush the joystick to the LOWER RIGHT corner and press button 1:",
		"\n",
		"INT_SetTimer0: %i is a bad value",
		"novideo",
		"-control",
		"Using external control API\n",
		"I_StartupDPMI\n",
		"I_StartupMouse\n",
		"I_StartupJoystick\n",
		"I_StartupKeyboard\n",
		"I_StartupSound\n",
		"ENDOOM",
		"DPMI memory: 0x%x",
		", 0x%x allocated for zone\n",
		"Insufficient memory!  You need to have at least 3.7 megabytes of total\n",
		"free memory available for DOOM to execute.  Reconfigure your CONFIG.SYS\n",
		"or AUTOEXEC.BAT to load fewer device drivers or TSR's.  We recommend\n",
		"creating a custom boot menu item in your CONFIG.SYS for optimum DOOMing.\n",
		"Please consult your DOS manual (\"Making more memory available\") for\n",
		"information on how to free up more memory for DOOM.\n\n",
		"DOOM aborted.\n",
		"-cdrom",
		"STCDROM",
		"STDISK",
		"I_AllocLow: DOS alloc of %i failed, %i free",
		"-net",
		"malloc() in I_InitNetwork() failed",
		"I_NetCmd when not in netgame",
		"I_StartupTimer()\n",
		"Can't register 35 Hz timer w/ DMX library",
		"d%c%s",
		"-nosound",
		"-nosfx",
		"-nomusic",
		"ENSONIQ\n",
		"Dude.  The ENSONIQ ain't responding.\n",
		"CODEC p=0x%x, d=%d\n",
		"CODEC.  The CODEC ain't responding.\n",
		"GUS\n",
		"GUS1\n",
		"Dude.  The GUS ain't responding.\n",
		"GUS2\n",
		"dmxgusc",
		"dmxgus",
		"cfg p=0x%x, i=%d, d=%d\n",
		"SB isn't responding at p=0x%x, i=%d, d=%d\n",
		"SB_Detect returned p=0x%x,i=%d,d=%d\n",
		"Adlib\n",
		"Dude.  The Adlib isn't responding.\n",
		"genmidi",
		"Midi\n",
		"cfg p=0x%x\n",
		"The MPU-401 isn't reponding @ p=0x%x.\n",
		"I_StartupSound: Hope you hear a pop.\n",
		"  Music device #%d & dmxCode=%d\n",
		"  Sfx device #%d & dmxCode=%d\n",
		"  calling DMX_Init\n",
		"  DMX_Init() returned %d\n",
		"CyberMan: Wrong mouse driver - no SWIFT support (AX=%04x).\n",
		"CyberMan: no SWIFT device connected.\n",
		"CyberMan: SWIFT device is not a CyberMan! (type=%d)\n",
		"CyberMan: CyberMan %d.%02d connected.\n",
		"Once you beat the big badasses and\nclean out the moon base you're supposed\nto win, aren't you? Aren't you? Where's\nyour fat reward and ticket home? What\nthe hell is this? It's not supposed to\nend this way!\n\nIt stinks like rotten meat, but looks\nlike the lost Deimos base.  Looks like\nyou're stuck on The Shores of Hell.\nThe only way out is through.\n\nTo continue the DOOM experience, play\nThe Shores of Hell and its amazing\nsequel, Inferno!\n",
		"You've done it! The hideous cyber-\ndemon lord that ruled the lost Deimos\nmoon base has been slain and you\nare triumphant! But ... where are\nyou? You clamber to the edge of the\nmoon and look down to see the awful\ntruth.\n\nDeimos floats above Hell itself!\nYou've never heard of anyone escaping\nfrom Hell, but you'll make the bastards\nsorry they ever heard of you! Quickly,\nyou rappel down to  the surface of\nHell.\n\nNow, it's on to the final chapter of\nDOOM! -- Inferno.",
		"The loathsome spiderdemon that\nmasterminded the invasion of the moon\nbases and caused so much death has had\nits ass kicked for all time.\n\nA hidden doorway opens and you enter.\nYou've proven too tough for Hell to\ncontain, and now Hell at last plays\nfair -- for you emerge from the door\nto see the green fields of Earth!\nHome at last.\n\nYou wonder what's been happening on\nEarth while you were battling evil\nunleashed. It's good that no Hell-\nspawn could have come through that\ndoor with you ...",
		"YOU HAVE ENTERED DEEPLY INTO THE INFESTED\nSTARPORT. BUT SOMETHING IS WRONG. THE\nMONSTERS HAVE BROUGHT THEIR OWN REALITY\nWITH THEM, AND THE STARPORT'S TECHNOLOGY\nIS BEING SUBVERTED BY THEIR PRESENCE.\n\nAHEAD, YOU SEE AN OUTPOST OF HELL, A\nFORTIFIED ZONE. IF YOU CAN GET PAST IT,\nYOU CAN PENETRATE INTO THE HAUNTED HEART\nOF THE STARBASE AND FIND THE CONTROLLING\nSWITCH WHICH HOLDS EARTH'S POPULATION\nHOSTAGE.",
		"YOU HAVE WON! YOUR VICTORY HAS ENABLED\nHUMANKIND TO EVACUATE EARTH AND ESCAPE\nTHE NIGHTMARE.  NOW YOU ARE THE ONLY\nHUMAN LEFT ON THE FACE OF THE PLANET.\nCANNIBAL MUTATIONS, CARNIVOROUS ALIENS,\nAND EVIL SPIRITS ARE YOUR ONLY NEIGHBORS.\nYOU SIT BACK AND WAIT FOR DEATH, CONTENT\nTHAT YOU HAVE SAVED YOUR SPECIES.\n\nBUT THEN, EARTH CONTROL BEAMS DOWN A\nMESSAGE FROM SPACE: \"SENSORS HAVE LOCATED\nTHE SOURCE OF THE ALIEN INVASION. IF YOU\nGO THERE, YOU MAY BE ABLE TO BLOCK THEIR\nENTRY.  THE ALIEN BASE IS IN THE HEART OF\nYOUR OWN HOME CITY, NOT FAR FROM THE\nSTARPORT.\" SLOWLY AND PAINFULLY YOU GET\nUP AND RETURN TO THE FRAY.",
		"YOU ARE AT THE CORRUPT HEART OF THE CITY,\nSURROUNDED BY THE CORPSES OF YOUR ENEMIES.\nYOU SEE NO WAY TO DESTROY THE CREATURES'\nENTRYWAY ON THIS SIDE, SO YOU CLENCH YOUR\nTEETH AND PLUNGE THROUGH IT.\n\nTHERE MUST BE A WAY TO CLOSE IT ON THE\nOTHER SIDE. WHAT DO YOU CARE IF YOU'VE\nGOT TO GO THROUGH HELL TO GET TO IT?",
		"THE HORRENDOUS VISAGE OF THE BIGGEST\nDEMON YOU'VE EVER SEEN CRUMBLES BEFORE\nYOU, AFTER YOU PUMP YOUR ROCKETS INTO\nHIS EXPOSED BRAIN. THE MONSTER SHRIVELS\nUP AND DIES, ITS THRASHING LIMBS\nDEVASTATING UNTOLD MILES OF HELL'S\nSURFACE.\n\nYOU'VE DONE IT. THE INVASION IS OVER.\nEARTH IS SAVED. HELL IS A WRECK. YOU\nWONDER WHERE BAD FOLKS WILL GO WHEN THEY\nDIE, NOW. WIPING THE SWEAT FROM YOUR\nFOREHEAD YOU BEGIN THE LONG TREK BACK\nHOME. REBUILDING EARTH OUGHT TO BE A\nLOT MORE FUN THAN RUINING IT WAS.\n",
		"CONGRATULATIONS, YOU'VE FOUND THE SECRET\nLEVEL! LOOKS LIKE IT'S BEEN BUILT BY\nHUMANS, RATHER THAN DEMONS. YOU WONDER\nWHO THE INMATES OF THIS CORNER OF HELL\nWILL BE.",
		"CONGRATULATIONS, YOU'VE FOUND THE\nSUPER SECRET LEVEL!  YOU'D BETTER\nBLAZE THROUGH THIS ONE!\n",
		"SLIME16",
		"RROCK14",
		"RROCK07",
		"RROCK17",
		"RROCK13",
		"RROCK19",
		"FLOOR4_8",
		"SFLR6_1",
		"MFLR8_4",
		"ZOMBIEMAN",
		"SHOTGUN GUY",
		"HEAVY WEAPON DUDE",
		"IMP",
		"DEMON",
		"LOST SOUL",
		"CACODEMON",
		"HELL KNIGHT",
		"BARON OF HELL",
		"ARACHNOTRON",
		"PAIN ELEMENTAL",
		"REVENANT",
		"MANCUBUS",
		"ARCH-VILE",
		"THE SPIDER MASTERMIND",
		"THE CYBERDEMON",
		"OUR HERO",
		"BOSSBACK",
		"PFUB2",
		"PFUB1",
		"END0",
		"END%i",
		"HELP2",
		"VICTORY2",
		"map01",
		"PLAYPAL",
		"M_PAUSE",
		"-debugfile",
		"debug%i.txt",
		"debug output to: %s\n",
		"w",
		"TITLEPIC",
		"demo1",
		"CREDIT",
		"demo2",
		"HELP2",
		"demo3",
		"default.cfg",
		"doom1.wad",
		"doom2f.wad",
		"doom2.wad",
		"doom.wad",
		"-shdev",
		"c:/localid/doom1.wad",
		"e:/doom/data/texture1.lmp",
		"e:/doom/data/pnames.lmp",
		"c:/localid/default.cfg",
		"-regdev",
		"c:/localid/doom.wad",
		"e:/doom/data/texture2.lmp",
		"-comdev",
		"c:/localid/doom2.wad",
		"e:/doom/cdata/texture1.lmp",
		"e:/doom/cdata/pnames.lmp",
		"French version\n",
		"Game mode indeterminate.\n",
		"rb",
		"\nNo such response file!",
		"Found response file %s!\n",
		"%d command-line args:\n",
		"%s\n",
		"-nomonsters",
		"-respawn",
		"-fast",
		"-devparm",
		"-altdeath",
		"-deathmatch",
		"                          DOOM System Startup v%i.%i                          ",
		"                         DOOM 2: Hell on Earth v%i.%i                           ",
		"\nP_Init: Checking cmd-line parameters...\n",
		"Development mode ON.\n",
		"-cdrom",
		"CD-ROM Version: default.cfg from c:\\doomdata\n",
		"c:\\doomdata",
		"c:/doomdata/default.cfg",
		"-turbo",
		"turbo scale: %i%%\n",
		"-wart",
		"~e:/doom/cdata/map0%i.wad",
		"~e:/doom/cdata/map%i.wad",
		"~e:/doom/E%cM%c.wad",
		"Warping to Episode %s, Map %s.\n",
		"-file",
		"-playdemo",
		"-timedemo",
		"%s.lmp",
		"Playing demo %s.lmp.\n",
		"-skill",
		"-episode",
		"-timer",
		"Levels will end after %d minute",
		"s",
		".\n",
		"-avg",
		"Austin Virtual Gaming: Levels will end after 20 minutes\n",
		"-warp",
		"V_Init: allocate screens.\n",
		"M_LoadDefaults: Load system defaults.\n",
		"Z_Init: Init zone memory allocation daemon. \n",
		"W_Init: Init WADfiles.\n",
		"\nYou cannot -file with the shareware version. Register!",
		"\nThis is not the registered version.",
		"===========================================================================\nATTENTION:  This version of DOOM has been modified.  If you would like to\nget a copy of the original game, call 1-800-IDGAMES or see the readme file.\n        You will not receive technical support for modified games.\n                      press enter to continue\n===========================================================================\n",
		"\tregistered version.\n",
		"===========================================================================\n             This version is NOT SHAREWARE, do not distribute!\n         Please report software piracy to the SPA: 1-800-388-PIR8\n===========================================================================\n",
		"\tshareware version.\n",
		"\tcommercial version.\n",
		"===========================================================================\n                            Do not distribute!\n         Please report software piracy to the SPA: 1-800-388-PIR8\n===========================================================================\n",
		"M_Init: Init miscellaneous info.\n",
		"R_Init: Init DOOM refresh daemon - ",
		"\nP_Init: Init Playloop state.\n",
		"I_Init: Setting up machine state.\n",
		"D_CheckNetGame: Checking network game status.\n",
		"S_Init: Setting up sound.\n",
		"HU_Init: Setting up heads up display.\n",
		"ST_Init: Init status bar.\n",
		"-statcopy",
		"External statistics registered.\n",
		"-record",
		"-loadgame",
		"c:\\doomdata\\doomsav%c.dsg",
		"doomsav%c.dsg",
		"ExpandTics: strange value %i at maketic %i",
		"Tried to transmit to another node",
		"send (%i + %i, R %i) [%i] ",
		"%i ",
		"\n",
		"bad packet length %i\n",
		"bad packet checksum\n",
		"setup packet\n",
		"get %i = (%i + %i, R %i)[%i] ",
		"Player 1 left the game",
		"Killed by network driver",
		"retransmit from %i\n",
		"out of order packet (%i + %i)\n",
		"missed tics from %i (%i - %i)\n",
		"NetUpdate: netbuffer->numtics > BACKUPTICS",
		"Network game synchronization aborted.",
		"listening for network start info...\n",
		"Different DOOM versions cannot play a net game!",
		"sending network start info...\n",
		"Doomcom buffer invalid!",
		"startskill %i  deathmatch: %i  startmap: %i  startepisode: %i\n",
		"player %i of %i (%i nodes)\n",
		"=======real: %i  avail: %i  game: %i\n",
		"TryRunTics: lowtic < gametic",
		"gametic>lowtic",
		"%s is turbo!",
		"consistency failure (%i should be %i)",
		"NET GAME",
		"Only %i deathmatch spots, 4 required",
		"map31",
		"version %i",
		"Bad savegame",
		"-cdrom",
		"c:\\doomdata\\doomsav%d.dsg",
		"doomsav%d.dsg",
		"Savegame buffer overrun",
		"game saved.",
		"SKY3",
		"SKY1",
		"SKY2",
		".lmp",
		"-maxdemo",
		"Demo is from a different game version!",
		"-nodraw",
		"-noblit",
		"timed %i gametics in %i realtics",
		"Z_CT at g_game.c:%i",
		"Demo %s recorded",
		"-cdrom",
		"c:\\doomdata\\doomsav%d.dsg",
		"doomsav%d.dsg",
		"empty slot",
		"M_LOADG",
		"M_LSLEFT",
		"M_LSCNTR",
		"M_LSRGHT",
		"you can't do load while in a net game!\n\npress a key.",
		"M_SAVEG",
		"_",
		"you can't save if you aren't playing!\n\npress a key.",
		"quicksave over your game named\n\n'%s'?\n\npress y or n.",
		"you can't quickload during a netgame!\n\npress a key.",
		"you haven't picked a quicksave slot yet!\n\npress a key.",
		"do you want to quickload the game named\n\n'%s'?\n\npress y or n.",
		"HELP2",
		"HELP1",
		"HELP",
		"M_SVOL",
		"M_DOOM",
		"M_NEWG",
		"M_SKILL",
		"you can't start a new game\nwhile in a network game.\n\npress a key.",
		"M_EPISOD",
		"are you sure? this skill level\nisn't even remotely fair.\n\npress y or n.",
		"this is the shareware version of doom.\n\nyou need to order the entire trilogy.\n\npress a key.",
		"M_OPTTTL",
		"Messages OFF",
		"Messages ON",
		"you can't end a netgame!\n\npress a key.",
		"are you sure you want to end the game?\n\npress y or n.",
		"%s\n\n(press y to quit to dos.)",
		"High detail",
		"Low detail",
		"M_THERML",
		"M_THERMM",
		"M_THERMR",
		"M_THERMO",
		"M_CELL1",
		"M_CELL2",
		"PLAYPAL",
		"Couldn't read file %s",
		"mouse_sensitivity",
		"sfx_volume",
		"music_volume",
		"show_messages",
		"key_right",
		"key_left",
		"key_up",
		"key_down",
		"key_strafeleft",
		"key_straferight",
		"key_fire",
		"key_use",
		"key_strafe",
		"key_speed",
		"use_mouse",
		"mouseb_fire",
		"mouseb_strafe",
		"mouseb_forward",
		"use_joystick",
		"joyb_fire",
		"joyb_strafe",
		"joyb_use",
		"joyb_speed",
		"screenblocks",
		"detaillevel",
		"snd_channels",
		"snd_musicdevice",
		"snd_sfxdevice",
		"snd_sbport",
		"snd_sbirq",
		"snd_sbdma",
		"snd_mport",
		"usegamma",
		"chatmacro0",
		"No",
		"chatmacro1",
		"I'm ready to kick butt!",
		"chatmacro2",
		"I'm OK.",
		"chatmacro3",
		"I'm not looking too good!",
		"chatmacro4",
		"Help!",
		"chatmacro5",
		"You suck!",
		"chatmacro6",
		"Next time, scumbag...",
		"chatmacro7",
		"Come here!",
		"chatmacro8",
		"I'll take care of it.",
		"chatmacro9",
		"Yes",
		"w",
		"%s\t\t%i\n",
		"%s\t\t\"%s\"\n",
		"-config",
		"\tdefault file: %s\n",
		"r",
		"%79s %[^\n]\n",
		"%x",
		"%i",
		"DOOM00.pcx",
		"M_ScreenShot: Couldn't create a PCX",
		"PLAYPAL",
		"screen shot",
		"AMMNUM%d",
		"Z_CT at am_map.c:%i",
		"Follow Mode ON",
		"Follow Mode OFF",
		"Grid ON",
		"Grid OFF",
		"Marked Spot",
		"%s %d",
		"All Marks Cleared",
		"fuck %d \r",
		"You need a blue key to activate this object",
		"You need a red key to activate this object",
		"You need a yellow key to activate this object",
		"You need a blue key to open this door",
		"You need a yellow key to open this door",
		"You need a red key to open this door",
		"Weird actor->movedir!",
		"P_NewChaseDir: called with no target",
		"P_GiveAmmo: bad type %i",
		"Picked up the armor.",
		"Picked up the MegaArmor!",
		"Picked up a health bonus.",
		"Picked up an armor bonus.",
		"Supercharge!",
		"MegaSphere!",
		"Picked up a blue keycard.",
		"Picked up a yellow keycard.",
		"Picked up a red keycard.",
		"Picked up a blue skull key.",
		"Picked up a yellow skull key.",
		"Picked up a red skull key.",
		"Picked up a stimpack.",
		"Picked up a medikit that you REALLY need!",
		"Picked up a medikit.",
		"Invulnerability!",
		"Berserk!",
		"Partial Invisibility",
		"Radiation Shielding Suit",
		"Computer Area Map",
		"Light Amplification Visor",
		"Picked up a clip.",
		"Picked up a box of bullets.",
		"Picked up a rocket.",
		"Picked up a box of rockets.",
		"Picked up an energy cell.",
		"Picked up an energy cell pack.",
		"Picked up 4 shotgun shells.",
		"Picked up a box of shotgun shells.",
		"Picked up a backpack full of ammo!",
		"You got the BFG9000!  Oh, yes.",
		"You got the chaingun!",
		"A chainsaw!  Find some meat!",
		"You got the rocket launcher!",
		"You got the plasma gun!",
		"You got the shotgun!",
		"You got the super shotgun!",
		"P_SpecialThing: Unknown gettable thing",
		"PTR_SlideTraverse: not a line?",
		"P_AddActivePlat: no more plats!",
		"P_RemoveActivePlat: can't find plat!",
		"P_GroupLines: miscounted",
		"map0%i",
		"map%i",
		"P_CrossSubsector: ss %i with numss = %i",
		"P_InitPicAnims: bad cycle from %s to %s",
		"P_PlayerInSpecialSector: unknown special %i",
		"texture2",
		"-avg",
		"-timer",
		"P_StartButton: no button slots left!",
		"P_SpawnMapThing: Unknown type %i at (%i, %i)",
		"Unknown tclass %i in savegame",
		"P_UnarchiveSpecials:Unknown tclass %i in savegame",
		"R_Subsector: ss %i with numss = %i",
		"Z_CT at r_data.c:%i",
		"R_GenerateLookup: column without a patch (%s)\n",
		"R_GenerateLookup: texture %i is >64k",
		"PNAMES",
		"TEXTURE1",
		"TEXTURE2",
		"S_START",
		"S_END",
		"[",
		" ",
		"         ]",
		"\b",
		"\b\b\b\b\b\b\b\b\b\b",
		".",
		"R_InitTextures: bad texture directory",
		"R_InitTextures: Missing patch in texture %s",
		"F_START",
		"F_END",
		"COLORMAP",
		"R_FlatNumForName: %s not found",
		"R_TextureNumForName: %s not found",
		"R_DrawFuzzColumn: %i to %i at %i",
		"R_DrawColumn: %i to %i at %i",
		"brdr_t",
		"brdr_b",
		"brdr_l",
		"brdr_r",
		"brdr_tl",
		"brdr_tr",
		"brdr_bl",
		"brdr_br",
		".",
		"F_SKY1",
		"R_MapPlane: %i, %i at %i",
		"R_FindPlane: no more visplanes",
		"R_DrawPlanes: drawsegs overflow (%i)",
		"R_DrawPlanes: visplane overflow (%i)",
		"R_DrawPlanes: opening overflow (%i)",
		"Z_CT at r_plane.c:%i",
		"Bad R_RenderWallRange: %i to %i",
		"R_InstallSpriteLump: Bad frame characters in lump %i",
		"R_InitSprites: Sprite %s frame %c has multip rot=0 lump",
		"R_InitSprites: Sprite %s frame %c has rotations and a rot=0 lump",
		"R_InitSprites: Sprite %s frame %c has rotations and a rot=0 lump",
		"R_InitSprites: Sprite %s : %c : %c has two lumps mapped to it",
		"R_InitSprites: No patches found for %s frame %c",
		"R_InitSprites: Sprite %s frame %c is missing rotations",
		"R_DrawSpriteRange: bad texturecolumn",
		"R_ProjectSprite: invalid sprite number %i ",
		"R_ProjectSprite: invalid sprite frame %i : %i ",
		"R_ProjectSprite: invalid sprite number %i ",
		"R_ProjectSprite: invalid sprite frame %i : %i ",
		"Filename base of %s >8 chars",
		"\tcouldn't open %s\n",
		"\tadding %s\n",
		"wad",
		"IWAD",
		"PWAD",
		"Wad file %s doesn't have IWAD or PWAD id\n",
		"Couldn't realloc lumpinfo",
		"W_Reload: couldn't open %s",
		"W_InitFiles: no files found",
		"Couldn't allocate lumpcache",
		"W_GetNumForName: %s not found!",
		"W_LumpLength: %i >= numlumps",
		"W_ReadLump: %i >= numlumps",
		"W_ReadLump: couldn't open %s",
		"W_ReadLump: only read %i of %i on lump %i",
		"W_CacheLumpNum: %i >= numlumps",
		"Z_CT at w_wad.c:%i",
		"w",
		"waddump.txt",
		"%s ",
		"    %c",
		"\n",
		"Bad V_CopyRect",
		"Bad V_DrawPatch",
		"Bad V_DrawPatchDirect",
		"Bad V_DrawBlock",
		"Z_Free: freed a pointer without ZONEID",
		"Z_Malloc: failed on allocation of %i bytes",
		"Z_Malloc: an owner is required for purgable blocks",
		"zone size: %i  location: %p\n",
		"tag range: %i to %i\n",
		"block:%p    size:%7i    user:%p    tag:%3i\n",
		"ERROR: block size does not touch the next block\n",
		"ERROR: next block doesn't have proper back link\n",
		"ERROR: two consecutive free blocks\n",
		"block:%p    size:%7i    user:%p    tag:%3i\n",
		"ERROR: block size does not touch the next block\n",
		"ERROR: next block doesn't have proper back link\n",
		"ERROR: two consecutive free blocks\n",
		"Z_CheckHeap: block size does not touch the next block\n",
		"Z_CheckHeap: next block doesn't have proper back link\n",
		"Z_CheckHeap: two consecutive free blocks\n",
		"Z_ChangeTag: freed a pointer without ZONEID",
		"Z_ChangeTag: an owner is required for purgable blocks",
		"Degreelessness Mode On",
		"Degreelessness Mode Off",
		"Ammo (no keys) Added",
		"Very Happy Ammo Added",
		"Music Change",
		"IMPOSSIBLE SELECTION",
		"No Clipping Mode ON",
		"No Clipping Mode OFF",
		"Power-up Toggled",
		"inVuln, Str, Inviso, Rad, Allmap, or Lite-amp",
		"... doesn't suck - GM",
		"ang=0x%x;x,y=(0x%x,0x%x)",
		"Changing Level...",
		"STTNUM%d",
		"STYSNUM%d",
		"STTPRCNT",
		"STKEYS%d",
		"STARMS",
		"STGNUM%d",
		"STFB%d",
		"STBAR",
		"STFST%d%d",
		"STFTR%d0",
		"STFTL%d0",
		"STFOUCH%d",
		"STFEVL%d",
		"STFKILL%d",
		"STFGOD0",
		"STFDEAD0",
		"PLAYPAL",
		"Z_CT at st_stuff.c:%i",
		"STTMINUS",
		"drawNum: n->y - ST_Y < 0",
		"updateMultIcon: y - ST_Y < 0",
		"updateBinIcon: y - ST_Y < 0",
		"No",
		"I'm ready to kick butt!",
		"I'm OK.",
		"I'm not looking too good!",
		"Help!",
		"You suck!",
		"Next time, scumbag...",
		"Come here!",
		"I'll take care of it.",
		"Yes",
		"Green: ",
		"Indigo: ",
		"Brown: ",
		"Red: ",
		"E1M1: Hangar",
		"E1M2: Nuclear Plant",
		"E1M3: Toxin Refinery",
		"E1M4: Command Control",
		"E1M5: Phobos Lab",
		"E1M6: Central Processing",
		"E1M7: Computer Station",
		"E1M8: Phobos Anomaly",
		"E1M9: Military Base",
		"E2M1: Deimos Anomaly",
		"E2M2: Containment Area",
		"E2M3: Refinery",
		"E2M4: Deimos Lab",
		"E2M5: Command Center",
		"E2M6: Halls of the Damned",
		"E2M7: Spawning Vats",
		"E2M8: Tower of Babel",
		"E2M9: Fortress of Mystery",
		"E3M1: Hell Keep",
		"E3M2: Slough of Despair",
		"E3M3: Pandemonium",
		"E3M4: House of Pain",
		"E3M5: Unholy Cathedral",
		"E3M6: Mt. Erebus",
		"E3M7: Limbo",
		"E3M8: Dis",
		"E3M9: Warrens",
		"NEWLEVEL",
		"level 1: entryway",
		"level 2: underhalls",
		"level 3: the gantlet",
		"level 4: the focus",
		"level 5: the waste tunnels",
		"level 6: the crusher",
		"level 7: dead simple",
		"level 8: tricks and traps",
		"level 9: the pit",
		"level 10: refueling base",
		"level 11: 'o' of destruction!",
		"level 12: the factory",
		"level 13: downtown",
		"level 14: the inmost dens",
		"level 15: industrial zone",
		"level 16: suburbs",
		"level 17: tenements",
		"level 18: the courtyard",
		"level 19: the citadel",
		"level 20: gotcha!",
		"level 21: nirvana",
		"level 22: the catacombs",
		"level 23: barrels o' fun",
		"level 24: the chasm",
		"level 25: bloodfalls",
		"level 26: the abandoned mines",
		"level 27: monster condo",
		"level 28: the spirit world",
		"level 29: the living end",
		"level 30: icon of sin",
		"level 31: wolfenstein",
		"level 32: grosse",
		"STCFN%.3d",
		"[Message unsent]",
		"You mumble to yourself",
		"Who's there?",
		"You scare yourself",
		"You start to rave",
		"You've lost it...",
		"Could not place patch on level %d",
		"INTERPIC",
		"WIMAP%d",
		"CWILV%2.2d",
		"WILV%d%d",
		"WIURH0",
		"WIURH1",
		"WISPLAT",
		"WIA%d%.2d%.2d",
		"WIMINUS",
		"WINUM%d",
		"WIPCNT",
		"WIF",
		"WIENTER",
		"WIOSTK",
		"WIOSTS",
		"WISCRT2",
		"WIOBJ",
		"WIOSTI",
		"WIFRGS",
		"WICOLON",
		"WITIME",
		"WISUCKS",
		"WIPAR",
		"WIKILRS",
		"WIVCTMS",
		"WIMSTT",
		"STFST01",
		"STFDEAD0",
		"STPB%d",
		"WIBP%d",
		"Z_CT at wi_stuff.c:%i",
		"wi_stuff.c",
		"wbs->epsd",
		"%s=%d in %s:%d",
		"wbs->last",
		"wbs->next",
		"wbs->pnum",
		"Attempt to set music volume at %d",
		"Z_CT at s_sound.c:%i",
		"Bad music number %d",
		"d_%s",
		"Attempt to set sfx volume at %d",
		"Bad sfx #: %d",
		"e1m1",
		"e1m2",
		"e1m3",
		"e1m4",
		"e1m5",
		"e1m6",
		"e1m7",
		"e1m8",
		"e1m9",
		"e2m1",
		"e2m2",
		"e2m3",
		"e2m4",
		"e2m5",
		"e2m6",
		"e2m7",
		"e2m8",
		"e2m9",
		"e3m1",
		"e3m2",
		"e3m3",
		"e3m4",
		"e3m5",
		"e3m6",
		"e3m7",
		"e3m8",
		"e3m9",
		"inter",
		"intro",
		"bunny",
		"victor",
		"introa",
		"runnin",
		"stalks",
		"countd",
		"betwee",
		"doom",
		"the_da",
		"shawn",
		"ddtblu",
		"in_cit",
		"dead",
		"stlks2",
		"theda2",
		"doom2",
		"ddtbl2",
		"runni2",
		"dead2",
		"stlks3",
		"romero",
		"shawn2",
		"messag",
		"count2",
		"ddtbl3",
		"ampie",
		"theda3",
		"adrian",
		"messg2",
		"romer2",
		"tense",
		"shawn3",
		"openin",
		"evil",
		"ultima",
		"read_m",
		"dm2ttl",
		"dm2int",
		"pistol",
		"shotgn",
		"sgcock",
		"dshtgn",
		"dbopn",
		"dbcls",
		"dbload",
		"plasma",
		"bfg",
		"sawup",
		"sawidl",
		"sawful",
		"sawhit",
		"rlaunc",
		"rxplod",
		"firsht",
		"firxpl",
		"pstart",
		"pstop",
		"doropn",
		"dorcls",
		"stnmov",
		"swtchn",
		"swtchx",
		"plpain",
		"dmpain",
		"popain",
		"vipain",
		"mnpain",
		"pepain",
		"slop",
		"itemup",
		"wpnup",
		"oof",
		"telept",
		"posit1",
		"posit2",
		"posit3",
		"bgsit1",
		"bgsit2",
		"sgtsit",
		"cacsit",
		"brssit",
		"cybsit",
		"spisit",
		"bspsit",
		"kntsit",
		"vilsit",
		"mansit",
		"pesit",
		"sklatk",
		"sgtatk",
		"skepch",
		"vilatk",
		"claw",
		"skeswg",
		"pldeth",
		"pdiehi",
		"podth1",
		"podth2",
		"podth3",
		"bgdth1",
		"bgdth2",
		"sgtdth",
		"cacdth",
		"skldth",
		"brsdth",
		"cybdth",
		"spidth",
		"bspdth",
		"vildth",
		"kntdth",
		"pedth",
		"skedth",
		"posact",
		"bgact",
		"dmact",
		"bspact",
		"bspwlk",
		"vilact",
		"noway",
		"barexp",
		"punch",
		"hoof",
		"metal",
		"chgun",
		"tink",
		"bdopn",
		"bdcls",
		"itmbk",
		"flame",
		"flamst",
		"getpow",
		"bospit",
		"boscub",
		"bossit",
		"bospn",
		"bosdth",
		"manatk",
		"mandth",
		"sssit",
		"ssdth",
		"keenpn",
		"keendt",
		"skeact",
		"skesit",
		"skeatk",
		"radio",
		"Bad list in dll_AddEndNode",
		"Bad list in dll_AddStartNode",
		"Bad list in dll_DelNode",
		"Empty list in dll_DelNode",
		"TROO",
		"SHTG",
		"PUNG",
		"PISG",
		"PISF",
		"SHTF",
		"SHT2",
		"CHGG",
		"CHGF",
		"MISG",
		"MISF",
		"SAWG",
		"PLSG",
		"PLSF",
		"BFGG",
		"BFGF",
		"BLUD",
		"PUFF",
		"BAL1",
		"BAL2",
		"PLSS",
		"PLSE",
		"MISL",
		"BFS1",
		"BFE1",
		"BFE2",
		"TFOG",
		"IFOG",
		"PLAY",
		"POSS",
		"SPOS",
		"VILE",
		"FIRE",
		"FATB",
		"FBXP",
		"SKEL",
		"MANF",
		"FATT",
		"CPOS",
		"SARG",
		"HEAD",
		"BAL7",
		"BOSS",
		"BOS2",
		"SKUL",
		"SPID",
		"BSPI",
		"APLS",
		"APBX",
		"CYBR",
		"PAIN",
		"SSWV",
		"KEEN",
		"BBRN",
		"BOSF",
		"ARM1",
		"ARM2",
		"BAR1",
		"BEXP",
		"FCAN",
		"BON1",
		"BON2",
		"BKEY",
		"RKEY",
		"YKEY",
		"BSKU",
		"RSKU",
		"YSKU",
		"STIM",
		"MEDI",
		"SOUL",
		"PINV",
		"PSTR",
		"PINS",
		"MEGA",
		"SUIT",
		"PMAP",
		"PVIS",
		"CLIP",
		"AMMO",
		"ROCK",
		"BROK",
		"CELL",
		"CELP",
		"SHEL",
		"SBOX",
		"BPAK",
		"BFUG",
		"MGUN",
		"CSAW",
		"LAUN",
		"PLAS",
		"SHOT",
		"SGN2",
		"COLU",
		"SMT2",
		"GOR1",
		"POL2",
		"POL5",
		"POL4",
		"POL3",
		"POL1",
		"POL6",
		"GOR2",
		"GOR3",
		"GOR4",
		"GOR5",
		"SMIT",
		"COL1",
		"COL2",
		"COL3",
		"COL4",
		"CAND",
		"CBRA",
		"COL6",
		"TRE1",
		"TRE2",
		"ELEC",
		"CEYE",
		"FSKU",
		"COL5",
		"TBLU",
		"TGRN",
		"TRED",
		"SMBT",
		"SMGT",
		"SMRT",
		"HDB1",
		"HDB2",
		"HDB3",
		"HDB4",
		"HDB5",
		"HDB6",
		"POB1",
		"POB2",
		"BRS1",
		"TLMP",
		"TLP2",
		"Floating-point support not loaded\r\n",
		"TZ"
	};
	
	// TODO: Finish this annoying stuff.
	
	// ======================================================================
	
	@Override
	public DEHMiscallany getMiscellany() 
	{
		return DEHMISC;
	}

	@Override
	public int getAmmoCount() 
	{
		return DEHAMMO.length;
	}

	@Override
	public DEHAmmo getAmmo(int index) 
	{
		return DEHAMMO[index];
	}

	@Override
	public int getStringCount() 
	{
		return DEHSTRINGS.length;
	}

	@Override
	public String getString(int index)
	{
		return DEHSTRINGS[index];
	}

	@Override
	public Integer getSoundStringIndex()
	{
		return 827;
	}

	@Override
	public Integer getSoundIndex(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getSpriteStringIndex()
	{
		return 939;
	}

	@Override
	public Integer getSpriteIndex(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSoundCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DEHSound getSound(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getThingCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DEHThing getThing(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWeaponCount()
	{
		return DEHWEAPON.length;
	}

	@Override
	public DEHWeapon getWeapon(int index)
	{
		return DEHWEAPON[index];
	}

	@Override
	public int getStateCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DEHState getState(int index) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getStateActionPointerIndex(int stateIndex) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getActionPointerCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DEHActionPointer getActionPointer(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
