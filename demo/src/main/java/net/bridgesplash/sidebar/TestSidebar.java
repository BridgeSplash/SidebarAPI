package net.bridgesplash.sidebar;

import java.time.Duration;
import java.util.WeakHashMap;

import net.bridgesplash.sidebar.sidebar.CustomSidebar;
import net.bridgesplash.sidebar.state.State;
import net.kyori.adventure.text.Component;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.extras.lan.OpenToLANConfig;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test class for the sidebar API.
 */
public final class TestSidebar {

    private static final Logger log = LoggerFactory.getLogger(TestSidebar.class);
    private static final Tag<@NotNull State<Boolean>> SNEAKING_STATE_TAG = Tag.Transient("is_sneaking_state");
    private static final Tag<@NotNull State<Boolean>> OFFHAND_NOT_AIR = Tag.Transient("offhand_not_air");

    final MinecraftServer server;

    private TestSidebar() {
        server = MinecraftServer.init(new Auth.Online());

        final InstanceContainer container = MinecraftServer
                .getInstanceManager()
                .createInstanceContainer();
        container.setGenerator((unit ->
                unit.modifier().fillHeight(0, 69, Block.QUARTZ_BLOCK)));

        var globalHandler = MinecraftServer.getGlobalEventHandler();

        globalHandler.addListener(AsyncPlayerConfigurationEvent.class,
                (event) -> {
                    event.getPlayer().setRespawnPoint(new Pos(0, 69, 0));
                    event.setSpawningInstance(container);
                }).addListener(PlayerDisconnectEvent.class, (event) ->
                SidebarAPI.getSidebarManager().removeSidebar(event.getPlayer())
        );

        WeakHashMap<Player, CustomSidebar> playerSidebars = new WeakHashMap<>();

        final State<Integer> globalSneaks = new State<>(0);



        globalHandler.addListener(PlayerSpawnEvent.class, (event) -> {
            Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);

            CustomSidebar sidebar = playerSidebars.computeIfAbsent(player,
                    (t) -> new CustomSidebar(Component.text("Player: ").append(t.getName()))
            );


            final State<Boolean> isSneaking = new State<>(false);
            final State<Boolean> itemInOffhand = new State<>(false);

            player.setTag(SNEAKING_STATE_TAG, isSneaking);
            player.setTag(OFFHAND_NOT_AIR, itemInOffhand);

            sidebar.addState("sneaks", globalSneaks);
            sidebar.addState("is_sneaking", isSneaking);
            sidebar.addState("item_in_offhand", itemInOffhand);

            sidebar.setLine(
                    "sneaker_line",
                    "<ifstate:is_sneaking:true:'<green>text</green>':'<red>text</red>'/> <state:sneaks/>"
            );
            sidebar.setLine(
                    "item_offhand_line",
                    "<green>text</green> <ifstate:item_in_offhand:true:yessir:nope/>"
            );
            sidebar.setLine("item_in_offhand_line", "<green>text</green> <state:item_in_offhand/>");

            SidebarAPI.getSidebarManager().addSidebar(event.getPlayer(), sidebar);
        });

        globalHandler.addListener(PlayerStartSneakingEvent.class, (event) -> {
            globalSneaks.setPrev((prev) -> prev + 1);
            Player player = event.getPlayer();
            State<Boolean> state = player.getTag(SNEAKING_STATE_TAG);
            if (state == null) {
                log.error("Player {} has no sneaking state!", player.getName());
                return;
            }
            state.set(true);
        });

        globalHandler.addListener(PlayerStopSneakingEvent.class, (event) -> {
            Player player = event.getPlayer();
            State<Boolean> state = player.getTag(SNEAKING_STATE_TAG);
            if (state == null) {
                log.error("Player {} has no sneaking state!", player.getName());
                return;
            }
            state.set(false);
        });

        globalHandler.addListener(PlayerSwapItemEvent.class, (event) -> {
            Player player = event.getPlayer();
            ItemStack offhandItem = event.getOffHandItem();
            State<Boolean> itemInOffhand = player.getTag(OFFHAND_NOT_AIR);
            if (itemInOffhand == null) {
                log.error("Player {} has no offhand item state!", player.getName());
                return;
            }
            itemInOffhand.set(!offhandItem.isAir());
        });


        OpenToLAN.open(new OpenToLANConfig().eventCallDelay(Duration.of(1, TimeUnit.DAY)));

    }


    void start() {
        server.start("0.0.0.0", 25565);
    }

    void shutdown(){

    }

    /**
     * Entrypoint for the demo.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("minestom.new-socket-write-lock", "true");
        MinecraftServer.setCompressionThreshold(0);

        TestSidebar sidebarTester = new TestSidebar();

        sidebarTester.start();
        MinecraftServer.getSchedulerManager().buildShutdownTask(sidebarTester::shutdown);
    }

}
