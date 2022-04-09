package me.HKS.HNS.Spin.GUI.Spin;

import me.HKS.HNS.Spin.Config;
import me.HKS.HNS.Spin.GUI.GuiCreator;
import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.Main;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SpinGui implements Listener {
    public static HashMap < UUID, Date > hasSpin = new HashMap < > ();
    public static List < Inventory > saveInventory = new ArrayList < > ();
    public static List < Inventory > saveInventory2 = new ArrayList < > ();
    public static HashMap < Inventory, Integer > random = new HashMap < > ();
    public static String guiTitle = "§aDaily Spin";
    public static ItemStack showcase = new ItemStack(Material.HOPPER);
    public static ItemStack skip = new ItemStack(Material.NETHER_STAR);
    public static boolean ignoreNoSpace = true;

    /**
     * Gets the random number and returns the itemStack and displays the item roll animation
     * @author HKS_HNS
     */
    public void OpenInv(Player p) {
        List < ItemStack > items2 = new ArrayList < > (SaveItems.items);
        if (items2.size() <= 3) {
            p.sendMessage(Config.getMessage("NoItems").replace("%prefix%", Config.getPrefix()));
            return;
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, items2.size());
        // Tests if the player has already spun
        if (!hasSpin.containsKey(p.getUniqueId())) {
            if (HasNoSpace(p, items2.get(randomNum))) {
                return;
            }
            hasSpin.put(p.getUniqueId(), new Date());
        } else if (hasSpin.get(p.getUniqueId()).getTime() + (1000 * 60 * 60 * 12) > new Date().getTime()) {
            int hour = Math.round((hasSpin.get(p.getUniqueId()).getTime() + (1000 * 60 * 60 * 12) - (new Date().getTime())) / 3600000);
            int min = Math.round((hasSpin.get(p.getUniqueId()).getTime() + (1000 * 60 * 60 * 12) - (new Date().getTime())) / 60000 - (hour * 60));
            p.sendMessage(Config.getMessage("AlreadySpun").replace("%hour%", String.valueOf(hour)).replace("%min%", String.valueOf(min)).replace("%prefix%", Config.getPrefix()));
            return;
        } else if (HasNoSpace(p, items2.get(randomNum))) {
            return;
        }
        hasSpin.put(p.getUniqueId(), new Date());
        GuiCreator createInv = new GuiCreator();
        Inventory inv = createInv.CreateInventory(guiTitle, 9 * 3);
        saveInventory.add(inv);
        saveInventory2.add(inv);
        random.put(inv, randomNum);
        p.openInventory(inv);
        inv.setItem(4, showcase);
        inv.setItem(22, skip);

        List < ItemStack > items = new ArrayList < > (items2);
        for (int i = -4; i < 1; i++) {
            inv.setItem(11 + i + 4, items.get(SetInSlotSub(items, items.size() - 1, -i)));
        }
        // The animation section:
        HashMap < Integer, Integer > SetItemsPlace = new HashMap < > ();
        for (int i = 0; i < items.size(); i++) {
            SetItemsPlace.put(i, i);
        }
        try {

            (new BukkitRunnable() {
                boolean isHasGiven = false;
                int i = 0;
                HashMap < Integer, Integer > setItemsPlace = SetItemsPlace;
                public void run() {

                    if (!saveInventory.contains(inv)) {
                        if (isHasGiven == false) {
                        giveItem(p, items2.get(randomNum));
                        isHasGiven = true; }
                        cancel();
                    }

                    for (int l = 0; l < setItemsPlace.size(); l++) {

                        if (setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) == 0) {
                            inv.setItem(11, items.get(l));
                        } else if (setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) == 1) {
                            inv.setItem(12, items.get(l));
                        } else if (setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) == 2) {
                            inv.setItem(13, items.get(l));
                            if (l == randomNum && i >= ((items.size() - 1) * 3)) {
                                System.out.println("Asd: " + i);
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                if (isHasGiven == false) {
                                    giveItem(p, items2.get(randomNum));
                                    isHasGiven = true; }
                                cancel();
                            }
                        } else if (setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) == 3) {
                            inv.setItem(14, items.get(l));
                        } else if (setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) == 4) {
                            inv.setItem(15, items.get(l));

                        }
                        if (setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) == setItemsPlace.size() - 1) {
                            setItemsPlace.put((Integer) setItemsPlace.keySet().toArray()[l], 0);

                        } else {
                            setItemsPlace.put((Integer) setItemsPlace.keySet().toArray()[l], setItemsPlace.get(setItemsPlace.keySet().toArray()[l]) + 1);
                        }

                    }
                    if (!saveInventory2.contains(inv)) {
                        inv.setItem(13 - 2, SaveItems.items.get(SetInSlotSub(SaveItems.items, randomNum, 2)));
                        inv.setItem(13 - 1, SaveItems.items.get(SetInSlotSub(SaveItems.items, randomNum, 1)));
                        inv.setItem(13, SaveItems.items.get(randomNum));
                        inv.setItem(13 + 1, SaveItems.items.get(SetInSlotInc(SaveItems.items, randomNum, 1)));
                        inv.setItem(13 + 2, SaveItems.items.get(SetInSlotInc(SaveItems.items, randomNum, 2)));
                        if (isHasGiven == false) {
                            giveItem(p, items2.get(randomNum));
                            isHasGiven = true; }
                        cancel();
                    }

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                    this.i++;
                }
            }).runTaskTimer(Main.getInstance(), 0L, 1L);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in the animation");
        }

    }

    /**
     * Checks if the item size is shorter than the inventory size
     * @author HKS_HNS
     */
    public int SetInSlotSub(List < ItemStack > items, int slot, int subtract) {
        int returnValue = slot - subtract;
        if (returnValue < 0) {
            returnValue = items.size() + returnValue;
        }
        return returnValue;
    }
    /**
     * Checks if the item size is bigger than the inventory size
     * @author HKS_HNS
     */
    public int SetInSlotInc(List < ItemStack > items, int slot, int increase) {
        int returnValue = slot + increase;
        if (returnValue > items.size() - 1) {
            returnValue = increase - 1;
        }
        return returnValue;
    }

    /**
     * Give the item to the player if it is not money
     * @author HKS_HNS
     */
    public void giveItem(Player p, ItemStack item) {
        if (isMoney(item, true)) {
            Economy a = Main.getEconomy();
            net.minecraft.server.v1_12_R1.ItemStack nmsMoney = CraftItemStack.asNMSCopy(item);
            NBTTagCompound aMoney = (nmsMoney.hasTag()) ? nmsMoney.getTag() : new NBTTagCompound();
            String amount = aMoney.getString("hhh9h8uh8hMoneyinhiuh");
            a.depositPlayer(p, Double.parseDouble(amount));
            p.sendMessage(Config.getMessage("WonMoney").replace("%balance%", amount + "").replace("%prefix%", Config.getPrefix()));
        } else {

            p.getInventory().addItem(item);
        }
    }

    /**
     * Sets the items DisplayName
     * @return
     */
    public ItemStack ItemStackRename(ItemStack item, String name) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Checks if the item is money
     * @return
     * @author HKS_HNS
     */
    public Boolean isMoney(ItemStack item, boolean ignoreNoS) {

        if (!item.hasItemMeta() || !ignoreNoS)
            return false;

        ItemMeta itemMeta = item.getItemMeta();
        ItemStack money = Config.getMoney();
        if (itemMeta.hasLore() && itemMeta.hasDisplayName() && item.getType() == money.getType()) {
            net.minecraft.server.v1_12_R1.ItemStack nmsMoney = CraftItemStack.asNMSCopy(item);
            NBTTagCompound aMoney = (nmsMoney.hasTag()) ? nmsMoney.getTag() : new NBTTagCompound();
            try {
                String amount = aMoney.getString("hhh9h8uh8hMoneyinhiuh");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {

        if (saveInventory.contains(e.getInventory())) {
            e.setCancelled(true);
            if (random.containsKey(e.getInventory()) && saveInventory2.contains(e.getInventory()) && e.getCurrentItem() != null && e.getCurrentItem().equals(skip) && e.getSlot() == 22) {
                saveInventory2.remove(e.getInventory());
                int randomNum = random.get(e.getInventory());
                random.remove(e.getInventory());
                e.getInventory().setItem(13 - 2, SaveItems.items.get(SetInSlotSub(SaveItems.items, randomNum, 2)));
                e.getInventory().setItem(13 - 1, SaveItems.items.get(SetInSlotSub(SaveItems.items, randomNum, 1)));
                e.getInventory().setItem(13, SaveItems.items.get(randomNum));
                e.getInventory().setItem(13 + 1, SaveItems.items.get(SetInSlotInc(SaveItems.items, randomNum, 1)));
                e.getInventory().setItem(13 + 2, SaveItems.items.get(SetInSlotInc(SaveItems.items, randomNum, 2)));
            }

        }
    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent e) {
        saveInventory.remove(e.getInventory());
        saveInventory2.remove(e.getInventory());
    }

    /**
     * Test if the player has enough space in the inventory
     * @author HKS_HNS
     */
    public boolean HasNoSpace(Player p, ItemStack item) {
        if (!isMoney(item, ignoreNoSpace)) {

            if (p.getInventory().firstEmpty() == -1) {
                p.sendMessage(Config.getMessage("NoSpace").replace("%prefix%", Config.getPrefix()));
                return true;
            }
        }
        return false;

    }

}