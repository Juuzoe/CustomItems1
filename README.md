# CustomItems1

CustomItems1 is a Java plugin built for a server setup where multiple existing plugins work together, it can be considered an addon although as it was personalised, it works with outlined plugins in requirement.txt. 

This was written for a Skyblock-style server environment, where custom items aren’t just cosmetic. They need to behave correctly inside the island plugin, be purchasable or configurable through shop tools,  display clean placeholders, and keep their metadata intact. The goal of this repo is to keep that logic in one place instead of scattering it across configs and scripts.

If you wish to reuse any of the code, please do so. There are function calls for different plugins, so you can implement them in your own plugin, although you should change the Maven references, as here it was written from jars.

## Integrations

The plugin was developed around these plugins and their APIs. Depending on how you run the server, some may be required and others may just unlock extra features:

PlaceholderAPI-2.11.6.jar  
ProtocolLib.jar  
ShopGUIPlus-1.106.1.jar  
SuperiorSkyblock2-2025.1-b526.jar  
SuperiorSkyblockAPI-2025.1.jar  
Vault.jar  
WildStacker-2025.1.jar  
item-nbt-api-plugin-2.15.0.jar  

## Build and runtime

The project uses Maven. Build with it and find references to existing Maven references for all of the plugins; they are open-source. Use JDK 17.  

Run it on a compatible Spigot/Paper server. Paper is recommended for local testing because it’s easier to debug and generally behaves better under load.

## Notes

This repo is kept as a practical server plugin project. If you’re using it as a base, deal with maven issues first for bbg software and then proceed with the plugin.
