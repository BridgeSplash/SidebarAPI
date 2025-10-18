package net.bridgesplash.sidebar.sidebar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import net.bridgesplash.sidebar.state.Hooks;
import net.bridgesplash.sidebar.state.State;
import net.bridgesplash.sidebar.state.StateManager;
import net.bridgesplash.sidebar.state.StateNode;
import net.bridgesplash.sidebar.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minestom.server.scoreboard.Sidebar;
import org.intellij.lang.annotations.Subst;


/**
 * Sidebar that supports states and MiniMessage rendering.
 *
 * @author TropicalShadow
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
public class CustomSidebar extends Sidebar {
    private final MiniMessage miniMessage = MiniMessage.builder()
            .editTags(t -> t.resolvers(getTagResolvers()))
            .build();
    private final StateManager stateManager = new StateManager(this);

    // state_id -> List<line_id>
    private final ConcurrentHashMap<String, List<String>> stateToLines = new ConcurrentHashMap<>();
    // line_id -> MiniMessage content
    private final ConcurrentHashMap<String, String> lineContents = new ConcurrentHashMap<>();

    /**
     * Constructs a CustomSidebar with a title as a {@link Component}.
     *
     * @param title the sidebar title
     */
    public CustomSidebar(Component title) {
        super(title);
    }

    /**
     * Constructs a CustomSidebar with a title as a String.
     *
     * @param title the sidebar title
     */
    public CustomSidebar(String title) {
        this(Component.text(title));
    }

    /**
     * Adds a line to the sidebar using MiniMessage content.
     *
     * @param key                the line key
     * @param miniMessageContent the MiniMessage string to render
     * @param index              the line index (0 = bottom)
     */
    public void setLine(String key, String miniMessageContent, int index) {
        lineContents.put(key, miniMessageContent);
        this.createLine(
                new ScoreboardLine(key, miniMessage.deserialize(miniMessageContent), index)
        );
        stateToLines.remove(key);
        parseNodeTreeForStateIds(miniMessageContent).forEach(stateKey -> {
            stateToLines.computeIfAbsent(stateKey, ignored -> new ArrayList<>()).add(key);
        });
    }

    /**
     * Adds a line to the sidebar using MiniMessage content.
     *
     * @param key                the line key
     * @param miniMessageContent the MiniMessage string to render
     */
    public void setLine(String key, String miniMessageContent) {
        setLine(key, miniMessageContent, 0);
    }

    List<String> parseNodeTreeForStateIds(String miniMessageContent) {
        RootNode rootNode = (RootNode) miniMessage.deserializeToTree(miniMessageContent);
        List<String> stateKeys = new ArrayList<>();
        for (ElementNode element : rootNode.children()) {
            collectStateKeys(element, stateKeys);
        }
        return stateKeys;
    }

    private void collectStateKeys(ElementNode element, List<String> outKeys) {
        if (!(element instanceof TagNode tagNode)) {
            return;
        }

        List<TagPart> parts = tagNode.parts();
        if (!parts.isEmpty()) {
            String tagName = parts.getFirst().value();
            StateNode node = StateNode.fromTagName(tagName);
            // ensure TagName is StateNode
            if (node != null) {
                if (parts.size() > 1) {
                    String tagId = parts.get(1).value();
                    if (!outKeys.contains(tagId)) {
                        outKeys.add(tagId);
                    }
                }
            }
        }
        for (ElementNode child : tagNode.children()) {
            collectStateKeys(child, outKeys);
        }
    }

    /**
     * Adds a state to the sidebar. The value can be any type;
     * it will be rendered as a {@link Component}.
     * The line will update automatically when the state changes.
     *
     * @param key   the state key
     * @param state the state to add
     */
    public void addState(String key, State<?> state) {
        stateManager.putState(key, state);

        Hooks.useEffect(() -> {
            stateToLines.getOrDefault(key, new ArrayList<>()).forEach(lineId -> {
                this.updateLineContent(lineId, miniMessage.deserialize(lineContents.get(lineId)));
            });
            return null;
        }, state);
    }

    /**
     * Returns the custom MiniMessage tag resolvers for state and conditional rendering.
     * <ul>
     *   <li><b>state</b>: Inserts the value of a state as a Component.</li>
     *   <li><b>ifState</b>: Conditionally inserts content if a state matches a value.</li>
     * </ul>
     *
     * @return the list of tag resolvers
     */
    public List<TagResolver> getTagResolvers() {
        return List.of(
                TagResolver.resolver(StateNode.STATE.getTagName(), (argumentQueue, context) -> {
                    String stateKey = argumentQueue.popOr("Missing state key").value();
                    State<?> state = stateManager.getState(stateKey);
                    if (state == null) {
                        return Tag.selfClosingInserting(
                                Component.text("Unknown state: " + stateKey)
                        );
                    }
                    Object value = state.get();
                    Component component = AdventureUtils.toComponent(value);
                    return Tag.selfClosingInserting(component);
                }),
                TagResolver.resolver(StateNode.PROGRESS.getTagName(), (argumentQueue, context) -> {
                    final String stateKey = argumentQueue
                            .popOr("Missing state key")
                            .value();
                    final String count = argumentQueue
                            .popOr("Missing count")
                            .value();
                    final String activeContent = argumentQueue
                            .popOr("Missing active content")
                            .value();
                    final String inactiveContent = argumentQueue
                            .popOr("Missing inactive content")
                            .value();

                    State<?> state = stateManager.getState(stateKey);
                    if (state == null) {
                        return Tag.selfClosingInserting(Component.text("Unknown state: " + stateKey));
                    }
                    Class<?> stateType = state.getType();
                    if (!stateType.equals(Float.class)) {
                        return Tag.selfClosingInserting(Component.text("Not a number state!"));
                    }
                    float value = (float) state.get();
                    value = Math.max(0f, Math.min(1f, value));
                    final int progress = (int) (value * 100);
                    final int amountOfBars = Integer.parseInt(count);
                    final int activeBars = progress / amountOfBars;
                    final int inactiveBars = amountOfBars - activeBars;

                    Component message = miniMessage.deserialize(
                            activeContent.repeat(activeBars) + inactiveContent.repeat(inactiveBars)
                    );

                    return Tag.selfClosingInserting(message);
                }),
                TagResolver.resolver(StateNode.IF_STATE.getTagName(), (argumentQueue, context) -> {
                    String stateKey = argumentQueue
                            .popOr("Missing state key")
                            .value();
                    String expectedValue = argumentQueue
                            .popOr("Missing expected value")
                            .value();
                    String ifExpected = argumentQueue
                            .popOr("Missing content for expected value")
                            .value();
                    String ifNotExpceted = argumentQueue
                            .popOr("Missing content for not expected value")
                            .value();
                    State<?> state = stateManager.getState(stateKey);
                    if (state == null) {
                        return Tag.selfClosingInserting(
                                Component.text("Unknown state: " + stateKey)
                        );
                    }
                    Object value = state.get();
                    if (value != null) {
                        boolean match = switch (value) {
                            case Boolean booleanValue -> Boolean.parseBoolean(expectedValue) == booleanValue;
                            case String stringValue -> expectedValue.equals(stringValue);
                            case Integer integerValue -> Integer.parseInt(expectedValue) == integerValue;
                            case Component componentValue -> componentValue.equals(AdventureUtils.toComponent(expectedValue));
                            case ComponentLike componentLikeValue -> componentLikeValue.asComponent()
                                    .equals(AdventureUtils.toComponent(expectedValue));
                            default -> expectedValue.equals(value.toString());
                        };

                        String content = match ? ifExpected : ifNotExpceted;
                        return Tag.selfClosingInserting(miniMessage.deserialize(content));
                    } else {
                        return Tag.inserting(Component.empty());
                    }
                }));
    }


}
