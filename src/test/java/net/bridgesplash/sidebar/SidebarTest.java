package net.bridgesplash.sidebar;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import net.bridgesplash.sidebar.sidebar.CustomSidebar;
import net.bridgesplash.sidebar.sidebar.SidebarManager;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;


public class SidebarTest {

    @Test
    public void testSidebarConstructionWithString() {
        CustomSidebar sidebar = new CustomSidebar("SidebarAPI Test");
        assertNotNull(sidebar);
    }

    @Test
    public void testSidebarConstructionWithComponent() {
        Component title = Component.text("SidebarAPI Test");
        CustomSidebar sidebar = new CustomSidebar(title);
        assertNotNull(sidebar);
    }

    @Test
    public void testSidebarApi() {
        SidebarManager manager = SidebarAPI.getSidebarManager();

    }

}
