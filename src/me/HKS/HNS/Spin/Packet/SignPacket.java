package me.HKS.HNS.Spin.Packet;

import io.netty.channel.*;
import me.HKS.HNS.Spin.Config;
import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.Main;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayInUpdateSign;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenSignEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SignPacket implements Listener {
    // TODO: after config is done, add a configurable money item for the inv

    /**
     * Schedules the opening of the inventory after the sign packet is received
     * @author HKS_HNS
     */
    void openInv(Player player, SaveItems saveItems, int i) {

        // Schedule the inventory open
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Main.getInstance, new Runnable() {
            @Override
            public void run() {
                // Open the inventory
                saveItems.OpenInv(player, i);

            }
        }, 1L);
    }

    /**
     * Sends the sign packet to the client and receives the response
     * @author HKS_HNS
     */
    public void getSignMessage(Player p, List < ItemStack > items, int i, SaveItems saveItems) {
        PacketPlayOutOpenSignEditor signPacket = new PacketPlayOutOpenSignEditor(new BlockPosition(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(signPacket);
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                if (packet instanceof PacketPlayInUpdateSign) {
                    PacketPlayInUpdateSign packetSignUpdate = (PacketPlayInUpdateSign) packet;
                    String[] strArray = packetSignUpdate.b();
                    String amount = strArray[0].replaceAll("[^0-9]", "");
                    if (!amount.equals("") && isNummer(amount)) {
                        ItemStack item = new ItemStack(Material.GOLD_INGOT);
                        // Name the item money
                        ItemMeta itemMetaMoney = Config.getMoney().getItemMeta();
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName(itemMetaMoney.getDisplayName());
                        // Money lore

                        List < String > lore = itemMetaMoney.getLore();
                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, lore.get(i).replaceAll("%amount%", amount));
                        }

                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                        if (Config.isStackIfMore()) {
                        int a = (int) Math.ceil((64 / (Config.getOnePer()*64) )* Float.parseFloat(amount));
                        if (a > 64) {
                            a = 64;
                        }
                            item.setAmount((int) a);

                        }
                        items.add(item);
                    }
                    channelHandlerContext.channel().pipeline().remove(this);
                    openInv(p, saveItems, i);
                }
                super.channelRead(channelHandlerContext, packet);

            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise promise) throws Exception {
                super.write(channelHandlerContext, packet, promise);
            }

        };

        ChannelPipeline pipeline = getChannel(p).pipeline();
        pipeline.addBefore("packet_handler", p.getName(), channelDuplexHandler);
    }

    /**
     * Gets the channel of the player
     * @author HKS_HNS
     */
    public Channel getChannel(Player p) {

        try {

            return ((CraftPlayer) p).getHandle().playerConnection.networkManager.channel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Checks if the string is a number
     * @author HKS_HNS
     */
    public boolean isNummer(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * NMS to get the class
     * @author HKS_HNS
     */
    public Class < ? > getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
    }

    /**
     * NMS to get the bukkit class
     * @author HKS_HNS
     */
    public Class < ? > getCraftBukkitClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
    }
    /**
     * Gets the server version
     * @author HKS_HNS
     */
    public String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    }

}