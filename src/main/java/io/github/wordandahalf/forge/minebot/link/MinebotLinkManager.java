package io.github.wordandahalf.forge.minebot.link;

import com.google.gson.JsonObject;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MinebotLinkManager
{	
	public static void distributeMessage(JsonObject json)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(
			p -> {
				p.sendMessage(new TextComponentString("[§d" + json.get("author").getAsString() + "§r] " + json.get("message").getAsString()));
			}
		);
	}
}
