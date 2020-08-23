package io.github.wordandahalf.forge.minebot.link;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinebotLinkNetworkManager
{
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static DatagramSocket server;
    protected static volatile Queue<DatagramPacket> packetQueue = new LinkedList<DatagramPacket>();

    public static void start()
    {
        try
        {
            server = new DatagramSocket(6969);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            return;
        }

        executorService.execute(new LinkUdpReceive());
        executorService.execute(new LinkUdpSend());
    }

    public static void stop()
    {
        executorService.shutdown();
    }

    private static final class LinkUdpReceive implements Runnable
    {
        // Chat messages cannot be longer than 256 bytes
        // The other 256 bytes are for the name, ranks, etc.
        private final byte[] buffer = new byte[8192];

        @Override
        public void run()
        {
            while(!Thread.currentThread().isInterrupted())
            {
            	try
            	{
	                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	                
	                try
	                {
	                    server.receive(packet);
	                }
	                catch (IOException e)
	                {
	                    e.printStackTrace();
	                }

	                String data = new String(packet.getData(), StandardCharsets.UTF_8);
	                JsonObject json = new JsonParser().parse(data.trim()).getAsJsonObject();
	                MinebotLinkManager.distributeMessage(json);
	                
	                Arrays.fill(buffer, (byte) 0);
	             
	                Thread.sleep(33);
            	}
            	catch(InterruptedException e)
            	{
            		Thread.currentThread().interrupt();
            	}
            }
        }
    }

    private static final class LinkUdpSend implements Runnable
    {
        @Override
        public void run()
        {
            while(!Thread.currentThread().isInterrupted())
            {
            	try
            	{
	                while(!packetQueue.isEmpty())
	                {
	                    DatagramPacket packet = packetQueue.remove();
		
	                    try
	                    {
	                        server.send(packet);
	                    }
	                    catch (IOException e)
	                    {
	                        e.printStackTrace();
	                    }
    				}
	                
	                Thread.sleep(33);
            	}
            	catch (InterruptedException e)
            	{
            		Thread.currentThread().interrupt();
				}
            }
        }
    }
}

