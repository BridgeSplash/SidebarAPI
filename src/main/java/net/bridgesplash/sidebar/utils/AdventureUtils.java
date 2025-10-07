package net.bridgesplash.sidebar.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;


/**
 * Utility class for handling Adventure-related operations.
 */
public final class AdventureUtils {

    private AdventureUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts an object to a {@link Component} for rendering.
     * Supports String, primitives, Component, ComponentLike, and falls back to toString().
     *
     * @param value the value to convert
     * @return the rendered Component
     */
    public static Component toComponent(Object value) {
        if (value instanceof String stringValue) {
            return Component.text(stringValue);
        } else if (value instanceof Integer integerValue) {
            return Component.text(integerValue);
        } else if (value instanceof Boolean booleanValue) {
            return Component.text(booleanValue);
        } else if (value instanceof Component componentLike) {
            return componentLike;
        } else if (value instanceof ComponentLike componentLikeValue) {
            return componentLikeValue.asComponent();
        } else if (value != null) {
            return Component.text(value.toString());
        } else {
            return Component.empty();
        }
    }



}
