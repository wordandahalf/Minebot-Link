package io.github.wordandahalf.forge.minebot;

import io.github.wordandahalf.forge.minebot.link.MinebotLinkEventListener;
import io.github.wordandahalf.forge.minebot.link.MinebotLinkNetworkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class MinebotDedicatedServerProxy extends CommonProxy
{
	public static SimpleNetworkWrapper NET_WRAPPER;
	
	@Override
	public void preInit()
	{
		System.out.println("MinebotLink v0.0.1 initialized!");
	}

	@Override
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(new MinebotLinkEventListener());
	}

	@Override
	public void postInit()
	{
		MinebotLinkNetworkManager.start();
		NET_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("MinebotLink");
	}
}
