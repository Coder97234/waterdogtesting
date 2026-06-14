import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class MyProxyPlugin extends Plugin implements Listener {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
        getLogger().info("MyProxyPlugin enabled");
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        String message = event.getMessage();
        if (!message.startsWith(".")) {
            return;
        }

        event.setCancelled(true);
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String[] args = message.substring(1).trim().split("\\s+");
        String command = args.length > 0 ? args[0].toLowerCase() : "";

        switch (command) {
            case "help":
                sendHelp(player);
                break;
            case "ping":
                sendPing(player);
                break;
            case "server":
                handleServer(player, args);
                break;
            case "fly":
                sendMessage(player, ChatColor.YELLOW + "Fly is managed by the game server, not the proxy.");
                break;
            case "speed":
                sendMessage(player, ChatColor.YELLOW + "Speed is managed by the game server, not the proxy.");
                break;
            case "hub":
                connectHub(player);
                break;
            case "tps":
                sendTPS(player);
                break;
            case "players":
                sendPlayers(player);
                break;
            default:
                sendMessage(player, ChatColor.RED + "Unknown command. Type .help for a command list.");
                break;
        }
    }

    private void sendHelp(ProxiedPlayer player) {
        sendMessage(player, ChatColor.GREEN + "MyProxyPlugin commands:");
        sendMessage(player, ChatColor.YELLOW + ".help " + ChatColor.WHITE + "- Show this help message");
        sendMessage(player, ChatColor.YELLOW + ".ping " + ChatColor.WHITE + "- Display your proxy latency");
        sendMessage(player, ChatColor.YELLOW + ".server " + ChatColor.WHITE + "- Show or change your current proxy server");
        sendMessage(player, ChatColor.YELLOW + ".fly " + ChatColor.WHITE + "- Proxy notice for fly mode");
        sendMessage(player, ChatColor.YELLOW + ".speed " + ChatColor.WHITE + "- Proxy notice for speed mode");
        sendMessage(player, ChatColor.YELLOW + ".hub " + ChatColor.WHITE + "- Connect to a hub server if available");
        sendMessage(player, ChatColor.YELLOW + ".tps " + ChatColor.WHITE + "- Show proxy TPS information");
        sendMessage(player, ChatColor.YELLOW + ".players " + ChatColor.WHITE + "- Show online player count");
    }

    private void sendPing(ProxiedPlayer player) {
        sendMessage(player, ChatColor.AQUA + "Ping: " + ChatColor.WHITE + player.getPing() + "ms");
    }

    private void handleServer(ProxiedPlayer player, String[] args) {
        if (args.length == 1) {
            if (player.getServer() != null && player.getServer().getInfo() != null) {
                sendMessage(player, ChatColor.GREEN + "Current server: " + ChatColor.WHITE + player.getServer().getInfo().getName());
            } else {
                sendMessage(player, ChatColor.RED + "You are not connected to a server yet.");
            }
            return;
        }

        String target = args[1];
        ServerInfo server = ProxyServer.getInstance().getServerInfo(target);
        if (server != null) {
            player.connect(server);
            sendMessage(player, ChatColor.GREEN + "Connecting to " + ChatColor.WHITE + server.getName() + ChatColor.GREEN + "..." );
        } else {
            sendMessage(player, ChatColor.RED + "Server not found: " + ChatColor.WHITE + target);
            if (args.length == 2 && target.equalsIgnoreCase("list")) {
                sendServerList(player);
            }
        }
    }

    private void connectHub(ProxiedPlayer player) {
        ServerInfo hub = ProxyServer.getInstance().getServerInfo("hub");
        if (hub != null) {
            player.connect(hub);
            sendMessage(player, ChatColor.GREEN + "Sending you to hub server...");
        } else {
            sendMessage(player, ChatColor.RED + "Hub server is not configured on the proxy.");
        }
    }

    private void sendTPS(ProxiedPlayer player) {
        sendMessage(player, ChatColor.YELLOW + "Proxy TPS information is not available in WaterdogPE plugins.");
    }

    private void sendPlayers(ProxiedPlayer player) {
        int count = ProxyServer.getInstance().getPlayers().size();
        sendMessage(player, ChatColor.AQUA + "Online players: " + ChatColor.WHITE + count);
    }

    private void sendServerList(ProxiedPlayer player) {
        sendMessage(player, ChatColor.GREEN + "Configured servers:");
        ProxyServer.getInstance().getServers().forEach((name, server) -> {
            sendMessage(player, ChatColor.YELLOW + "- " + ChatColor.WHITE + name + ChatColor.GRAY + " (" + server.getAddress().getHostString() + ":" + server.getAddress().getPort() + ")");
        });
    }

    private void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(message));
    }
}
