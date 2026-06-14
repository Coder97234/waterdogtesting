package me.myproxy;

import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.event.defaults.ChatReceivedEvent;

public class MyPlugin extends Plugin {
    @Override
    public void onEnable() {
        getLogger().info("Lumine-Style Commands Loaded!");
        getProxy().getEventManager().subscribe(ChatReceivedEvent.class, this::onChat);
    }

    public void onChat(ChatReceivedEvent event) {
        String msg = event.getMessage();
        if (!msg.startsWith(".")) return;
        
        event.setCancelled(true); // Don't show command in global chat
        var p = event.getPlayer();

        switch (msg.toLowerCase()) {
            case ".help":
                p.sendMessage("§e--- MyProxy Lumine Menu ---");
                p.sendMessage("§a.fly §f- Toggle Fly Packets");
                p.sendMessage("§a.speed §f- Toggle Speed Packets");
                p.sendMessage("§a.ping §f- Connection Status");
                p.sendMessage("§a.sharksmp §f- Switch to SharkSMP");
                break;
            case ".fly":
                p.sendMessage("§b[Proxy] §fFlight enabled (Visual Only)");
                break;
            case ".ping":
                p.sendMessage("§b[Proxy] §fYour Latency: §a" + p.getPing() + "ms");
                break;
            default:
                p.sendMessage("§c[!] Unknown Proxy Command.");
        }
    }
}
