// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.tools;

import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ShortCut;
import org.openstreetmap.josm.tools.PlatformHookUnixoid;
import org.openstreetmap.josm.Main;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

/**
  * see PlatformHook.java
  */
public class PlatformHookWindows extends PlatformHookUnixoid implements PlatformHook {
	public void preStartupHook(){
	}
	public void startupHook() {
	}
	public void openUrl(String url) throws IOException {
		Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
	}
	public void initShortCutGroups() {
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_NONE),    Integer.toString(-1));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_HOTKEY),  Integer.toString(KeyEvent.CTRL_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_MENU),    Integer.toString(KeyEvent.CTRL_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_EDIT),    Integer.toString(0));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_LAYER),   Integer.toString(KeyEvent.ALT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_DIRECT),  Integer.toString(0));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_DEFAULT+ShortCut.GROUP_MNEMONIC),Integer.toString(KeyEvent.ALT_DOWN_MASK));

		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_NONE),       Integer.toString(-1));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_HOTKEY),     Integer.toString(KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_MENU),       Integer.toString(KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_EDIT),       Integer.toString(KeyEvent.SHIFT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_LAYER),      Integer.toString(KeyEvent.ALT_DOWN_MASK  | KeyEvent.SHIFT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_DIRECT),     Integer.toString(KeyEvent.SHIFT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT1+ShortCut.GROUP_MNEMONIC),   Integer.toString(KeyEvent.ALT_DOWN_MASK));

		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_NONE),       Integer.toString(-1));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_HOTKEY),     Integer.toString(KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_MENU),       Integer.toString(KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_EDIT),       Integer.toString(KeyEvent.ALT_DOWN_MASK  | KeyEvent.SHIFT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_LAYER),      Integer.toString(KeyEvent.ALT_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_DIRECT),     Integer.toString(KeyEvent.CTRL_DOWN_MASK));
		Main.pref.put("shortcut.groups."+(ShortCut.GROUPS_ALT2+ShortCut.GROUP_MNEMONIC),   Integer.toString(KeyEvent.ALT_DOWN_MASK));
	}
	public void initSystemShortCuts() {
		// This list if far from complete!
		ShortCut.registerSystemCut("system:exit", "unused", KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK).setAutomatic(); // items with automatic shortcuts will not be added to the menu bar at all
		ShortCut.registerSystemCut("system:menuexit", "unused", KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
		ShortCut.registerSystemCut("system:copy", "unused", KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
		ShortCut.registerSystemCut("system:paste", "unused", KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
		ShortCut.registerSystemCut("system:cut", "unused", KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK);
		ShortCut.registerSystemCut("system:duplicate", "unused", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK); // not really system, but to avoid odd results
		ShortCut.registerSystemCut("system:help", "unused", KeyEvent.VK_F1, 0);
	}
}



