package net.bridgesplash.sidebar.state.types;

import net.kyori.adventure.text.Component;

public sealed interface CustomType permits CustomType.StringValue, CustomType.IntValue {
    record StringValue(String value) implements CustomType {
        @Override
        public Component toComponent() {
            return Component.text(value);
        }

        @Override
        public CustomType add(CustomType other) {
            return new StringValue(this.value + ((StringValue) other).value);
        }
    }
    record IntValue(int value) implements CustomType {
        @Override
        public Component toComponent() {
            return Component.text(value);
        }

        @Override
        public CustomType add(CustomType other) {
            return new IntValue(this.value + ((IntValue) other).value);
        }
    }


    Component toComponent();

    CustomType add(CustomType other);

}