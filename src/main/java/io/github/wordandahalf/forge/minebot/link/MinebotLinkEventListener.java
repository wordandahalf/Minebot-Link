package io.github.wordandahalf.forge.minebot.link;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import io.github.wordandahalf.forge.minebot.Minebot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MinebotLinkEventListener
{	
	@SubscribeEvent
	public void onServerChatEvent(ServerChatEvent event)
	{
		JsonObject json = new JsonObject();
		json.addProperty("author", event.getUsername());
		json.addProperty("message", event.getMessage());

		byte[] data = json.toString().getBytes(StandardCharsets.UTF_8);

		DatagramPacket packet = new DatagramPacket(data, data.length, Minebot.BOT_ADDRESS, 6969);
		MinebotLinkNetworkManager.packetQueue.add(packet);
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent e)
	{		
		if(e.getEntity() instanceof EntityPlayerMP)
		{
			EntityPlayerMP killed = (EntityPlayerMP) e.getEntity();
			
			DamageSource d = e.getSource();
			Entity s = e.getSource().getImmediateSource();
			
			String killerName;
			String killVerb;
					
			if(s == null)
			{
				killerName = "";
			}
			else
			{
				if(s instanceof EntityArrow)
				{
					killerName = EntityList.getEntityString(((EntityArrow) s).shootingEntity);
				}
				else
				if(s instanceof EntityPlayerMP)
				{
					killerName = s.getName();
				}
				else
				{
					killerName = EntityList.getEntityString(s);
				}
			}

			switch (d.damageType) {
				case "outOfWorld":
					killVerb = "fell out of the world";
					break;
				case "onFire":
					killVerb = "was burnt to a crisp";
					break;
				case "fall":
					killVerb = "fell from a high place";
					break;
				case "explosion.player":
					killVerb = "was blown up";
					break;
				case "inWall":
					killVerb = "suffocated in a wall";
					break;
				case "mob":
					killVerb = "was slain";
					break;
				case "drown":
					killVerb = "drowned";
					break;
				default:
					System.out.println(d.damageType);
					killVerb = "died";
					break;
			}
			
			String msg = killed.getName() + " ";
			
			if(msg.equals("DirtyDan5435 "))
			{
				msg = "Dan is a fucking idiot and ";
			}
			
			if(killerName == null || killerName.equals(""))
			{
				msg += killVerb;
			}
			else
			{
				msg += killVerb + " by " + killerName;
			}

			JsonObject json = new JsonObject();
			json.addProperty("author", "Server");
			json.addProperty("message", msg);

			byte[] data = json.toString().getBytes(StandardCharsets.UTF_8);

			DatagramPacket packet = new DatagramPacket(data, data.length, Minebot.BOT_ADDRESS, 6969);
			MinebotLinkNetworkManager.packetQueue.add(packet);
		}
	}
}
