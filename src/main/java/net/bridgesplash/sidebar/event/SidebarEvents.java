package net.bridgesplash.sidebar.event;

import net.bridgesplash.sidebar.SidebarAPI;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event class for handling sidebar events.
 *
 * @author TropicalShadow
 */
public final class SidebarEvents {

    private static final EventNode<@NotNull Event> EVENT_NODE = EventNode.all("sidebar-api-events");

    private SidebarEvents() {

    }

    /**
     * Registers all events for the sidebar API.
     *
     * @param eventNode the event node to register the events to
     */
    public static void registerEvents(EventNode<@NotNull Event> eventNode) {
        EVENT_NODE.addListener(PlayerDisconnectEvent.class, event -> {
            // Remove the sidebar when the player disconnects
            SidebarAPI.getSidebarManager().removeSidebar(event.getPlayer());
        });

        eventNode.addChild(EVENT_NODE);
    }

    /**
     * Unregisters all events for the sidebar API.
     *
     * @param eventNode parent event node
     */
    private static void unregisterEvents(EventNode<@NotNull Event> eventNode) {
        eventNode.removeChild(EVENT_NODE);
    }

}
