package net.mtrop.doom.tools.doommake.gui.swing;

import static net.mtrop.doom.tools.struct.swing.SwingUtils.*;

import javax.swing.JFrame;

import net.mtrop.doom.tools.gui.swing.DoomMakeSettingsWindow;

public final class DoomMakeWindowTest 
{
	public static void main(String[] args) 
	{
		setSystemLAF();
		apply(new DoomMakeSettingsWindow(), (window) -> {
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
		});
	}
}
