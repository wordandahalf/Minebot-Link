package io.github.wordandahalf.forge.minebot;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Minebot.MODID, name = Minebot.NAME, version = Minebot.VERSION, acceptableRemoteVersions = "*")
public class Minebot
{
    public static final String MODID = "minebot";
    public static final String NAME = "Minebot Forge Integration";
    public static final String VERSION = "1.0";
    
    public static InetAddress BOT_ADDRESS;
    
    private static Configuration CONFIG;
    
    @Mod.Instance(Minebot.MODID)
    public static Minebot instance = new Minebot();
    
    @SidedProxy(serverSide = "io.github.wordandahalf.forge.minebot.MinebotDedicatedServerProxy")
    public static CommonProxy proxy;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
    	CONFIG = new Configuration(e.getSuggestedConfigurationFile());
    	CONFIG.load();
    	
    	try
    	{
			BOT_ADDRESS = InetAddress.getByName(Minebot.CONFIG.getString("bot_ip", Configuration.CATEGORY_GENERAL, "127.0.0.1", "The IP of the Discord bot"));
		}
    	catch (UnknownHostException e1)
    	{
			e1.printStackTrace();
		}
    	
    	CONFIG.save();
    	
    	proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
    	proxy.init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
    	proxy.postInit();
    }
}
