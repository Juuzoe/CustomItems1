# CustomItems1

CustomItems1 is a Java plugin made to sit alongside a specific server stack and extend it. It plugs into a set of common server plugins and uses their APIs so that custom items behave consistently across the server instead of being handled in separate configs or one-off systems. The exact jars this project was built against are listed in `requirements.txt`.

It was originally used in a Skyblock-style setup, where custom items have to “fit” into the rest of the server. They need to work with the island plugin, show up properly in shops, display placeholders cleanly, respect economy rules, and keep item metadata intact. This repo keeps that logic together in one place.

You can reuse parts of the code if it helps. The main thing worth taking from this project is the integration patterns, but you’ll likely want to replace the local jar setup with proper Maven dependencies in your own project.

## Features

Auto Mob Hopper collects drops from a chunk automatically and integrates with WildStacker spawners.

Auto Item Hopper applies the same idea to farming resources like sugar cane and cactus.

Auto XP Hopper bottles XP and supports claiming very large amounts from a single bottle.

Auto Sell Chest sells the chest contents on a timer (every ten seconds). It uses SuperiorSkyblock2 for island context and Vault for economy handling.

## Integrations

This project was written against these plugins and their APIs:

PlaceholderAPI-2.11.6.jar  
ProtocolLib.jar  
ShopGUIPlus-1.106.1.jar  
SuperiorSkyblock2-2025.1-b526.jar  
SuperiorSkyblockAPI-2025.1.jar  
Vault.jar  
WildStacker-2025.1.jar  
item-nbt-api-plugin-2.15.0.jar  

## Build and runtime

The project uses Maven. Build it with `mvn clean package` and the output jar will be in `target/`. It was originally set up using local jars rather than clean Maven dependencies, so if you’re forking it, the first step is usually replacing those jar references with proper Maven coordinates for the plugins you need.

Use JDK 17 for building. Run it on a compatible Spigot/Paper server; Paper is recommended for testing and debugging.
