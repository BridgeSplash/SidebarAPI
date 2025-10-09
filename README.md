# SidebarAPI

A lightweight, state-driven sidebar API built on Minestom and Kyori Adventure MiniMessage. It lets you define sidebar lines with reactive state tags so lines update automatically when state changes.

- Repository: https://github.com/BridgeSplash/SidebarAPI
- Maven repository (releases): https://repo.tesseract.club/releases

## Requirements
- Java 21+
- Minestom

## Installation

Add the Tesseract Maven repository and the SidebarAPI dependency to your build tool of choice.

### Gradle (Kotlin DSL)
```kotlin
repositories {
    maven("https://repo.tesseract.club/releases")
}

dependencies {
    implementation("net.bridgesplash:sidebar-api:<version>")
}
```

### Gradle (Groovy)
```groovy
repositories {
    maven { url "https://repo.tesseract.club/releases" }
}

dependencies {
    implementation "net.bridgesplash:sidebar-api:<version>"
}
```

### Maven
```xml
<repositories>
  <repository>
    <id>tesseract-releases</id>
    <url>https://repo.tesseract.club/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>net.bridgesplash</groupId>
    <artifactId>sidebar-api</artifactId>
    <version>&lt;version&gt;</version>
  </dependency>
</dependencies>
```

Replace <version> with the latest listed under the releases repository. If you are building from source and using the dev version, the version may be "dev".

## Getting started

SidebarAPI integrates with Minestom. At a high level:

- Create a CustomSidebar with a title.
- Register state objects with keys using addState.
- Use MiniMessage tags in your line content:
  - `<state:your_key/>` inserts the current value of a state.
  - `<ifstate:your_key:expected:whenTrue:whenFalse/>` conditionally renders content depending on whether the state value equals expected.
- Add the sidebar to a player with SidebarManager.

Example (abridged from the demo):

```text
import net.bridgesplash.sidebar.sidebar.CustomSidebar;
import net.bridgesplash.sidebar.state.State;
import net.bridgesplash.sidebar.SidebarAPI;
import net.kyori.adventure.text.Component;

// Create a sidebar
CustomSidebar sidebar = new CustomSidebar(Component.text("Player Sidebar"));

// Create some reactive state
State<Boolean> isSneaking = new State<>(false);
State<Integer> sneaks = new State<>(0);

// Register states with keys
sidebar.addState("is_sneaking", isSneaking);
sidebar.addState("sneaks", sneaks);

// Define lines using MiniMessage tags
sidebar.setLine("status_line", "&lt;ifstate:is_sneaking:true:'&lt;green&gt;Sneaking&lt;/green&gt;':'&lt;red&gt;Standing&lt;/red&gt;'/&gt;");
sidebar.setLine("count_line", "Sneak count: &lt;state:sneaks/&gt;");

// Attach to a player
SidebarAPI.getSidebarManager().addSidebar(player, sidebar);

// Later, update state and lines will re-render automatically
isSneaking.set(true);
sneaks.setPrev(prev -> prev + 1);
```

### Supported MiniMessage tags
- state: Inserts the value of a registered state as a Component. Example: `<state:health/>`
- ifstate: Renders conditional content. Example: `<ifstate:is_sneaking:true:'<green>yes</green>':'<red>no</red>'/>`

Values are compared intelligently against the expected value (Boolean, String, Integer, or Component-like values are supported).

## Demo
A runnable demo is included under demo/. See TestSidebar.java for a complete Minestom example that wires up player events and modifies states.

## License
This project is licensed under the MIT License. See LICENSE for details.
