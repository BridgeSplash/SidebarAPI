package net.bridgesplash.sidebar;

import net.bridgesplash.sidebar.sidebar.SidebarManager;

/**
 * API for handling sidebar operations.
 *
 * @author TropicalShadow
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public final class SidebarAPI {

    private static final SidebarManager SIDEBAR_MANAGER = new SidebarManager();

    private SidebarAPI() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets the singleton instance of the SidebarManager.
     *
     * @return the SidebarManager instance
     */
    public static SidebarManager getSidebarManager() {
        return SIDEBAR_MANAGER;
    }

}