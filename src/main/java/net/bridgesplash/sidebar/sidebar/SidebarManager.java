package net.bridgesplash.sidebar.sidebar;

import java.util.WeakHashMap;
import net.minestom.server.entity.Player;

/**
 * Manages the sidebar instances.
 */
public class SidebarManager {


    private final WeakHashMap<Player, CustomSidebar> sidebars = new WeakHashMap<>();

    public void addSidebar(Player player, CustomSidebar sidebar) {
        sidebars.put(player, sidebar);
        sidebar.addViewer(player);
    }

    /**
     * Removes the sidebar for the given player.
     *
     * @param player the player to remove the sidebar for.
     *
     */
    public void removeSidebar(Player player) {
        CustomSidebar sidebar = sidebars.remove(player);
        if (sidebar != null) {
            sidebar.removeViewer(player);
        }
    }

    public CustomSidebar getSidebar(Player player) {
        return sidebars.get(player);
    }


}
