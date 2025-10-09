package net.bridgesplash.sidebar.state;

import lombok.RequiredArgsConstructor;

/**
 * Represents the different types of StateNodes.
 */
@RequiredArgsConstructor
public enum StateNode {
    STATE("state"),
    IF_STATE("ifstate")
    ;

    private final String tagName;


    /**
     * Gets the StateNode from the given tag name.
     *
     * @param tagName The tag name to get the StateNode for
     * @return The StateNode if it exists, null otherwise
     */
    public static StateNode fromTagName(String tagName) {
        for (StateNode node : values()) {
            if (node.tagName.equals(tagName)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Checks if the given tag name is a valid StateNode tag.
     *
     * @param tagName The tag name to check
     * @return True if the tag name is a valid StateNode tag, false otherwise
     */
    public static boolean isTag(String tagName) {
        for (StateNode node : values()) {
            if (node.tagName.equals(tagName)) {
                return true;
            }
        }
        return false;
    }
}