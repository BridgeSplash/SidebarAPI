package net.bridgesplash.sidebar.sidebar;

import java.util.WeakHashMap;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the sidebar instances.
 */
public final class SidebarManager {

    private final WeakHashMap<Player, CustomSidebar> sidebars = new WeakHashMap<>();

    /**
     * Constructs a new SidebarManager.
     */
    public SidebarManager() {
    }

    /**
     * Adds a sidebar for the given player.
     *
     * @param player Player to add
     * @param sidebar Sidebar to register player with
     */
    public void addSidebar(Player player, CustomSidebar sidebar) {
        sidebars.put(player, sidebar);
        sidebar.addViewer(player);
    }

    /**
     * Removes the sidebar for the given player.
     *
     * @param player the player to remove the sidebar for.
     */
    public void removeSidebar(Player player) {
        CustomSidebar sidebar = sidebars.remove(player);
        if (sidebar != null) {
            sidebar.removeViewer(player);
        }
    }

    /**
     * Gets the sidebar for the given player.
     *
     * @param player player to get sidebar for
     * @return CustomSidebar sidebar if it exists, null otherwise
     */
    @Nullable
    public CustomSidebar getSidebar(Player player) {
        return sidebars.get(player);
    }


}
