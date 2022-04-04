package me.HKS.HNS.Spin.GUI.Settings;

import me.HKS.HNS.Spin.GUI.GuiCreator;
import me.HKS.HNS.Spin.Packet.SignPacket;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * SaveItems.java - For the GUI of the settings (SaveItems)
 * @author HKS
 */
public class SaveItems implements Listener {

    public static List < ItemStack > items = new ArrayList < > ();
    public static List < Inventory > saveInventory = new ArrayList < > ();
    public void OpenInv(Player p, int page) {
        GuiCreator createInv = new GuiCreator();
        Inventory inv = createInv.CreateInventory("§4Spin items Page " + (page + 1), 9 * 6);
        LoadItems(inv, page);
        saveInventory.add(inv);
        p.openInventory(inv);
        PlayerInventory pInv = p.getInventory();
        for (int i = 0; i < pInv.getSize(); i++) {
            if (pInv.getItem(i) != null) {
                pInv.setItem(i, ItemLoreAdd(pInv.getItem(i), "§a§lClick To Add"));
            }
        }

    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        // Test if the inventory is the one we want
        if (saveInventory.contains(e.getInventory())) {
            e.setCancelled(true);

            if (e.getClickedInventory() == null)
                return;
            // Saves the item and removes the lore from the item
            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                ItemStack a = new ItemStack(e.getCurrentItem());
                if (!a.hasItemMeta())
                    return;
                ItemStack itemAdd = ItemLoreRem(ItemLoreRem(a, "§4§lClick To Remove"), "§a§lClick To Add");
                if (itemAdd.hasItemMeta() && itemAdd.getItemMeta().hasLore()) {

                    if (!itemAdd.getItemMeta().getLore().get(0).equals("§4§lClick To Remove")) {
                        items.add(itemAdd);
                    } else {
                        System.out.println(itemAdd.getType() + " 83");
                    }
                } else {
                    items.add(itemAdd);
                }

                LoadItems(e.getInventory(), Integer.parseInt(e.getInventory().getName().replace("§4Spin items Page ", "")) - 1);

            } else if (e.getClickedInventory().equals(e.getInventory())) { // Removes the item
                if (e.getSlot() == e.getInventory().getSize() - 1 && e.getCurrentItem().equals(SetItemName(new ItemStack(Material.ARROW), "§3Next"))) {
                    OpenInv((Player) e.getWhoClicked(), Integer.parseInt(e.getInventory().getName().replace("§4Spin items Page ", "")));
                } else if (e.getSlot() == e.getInventory().getSize() - 9 && e.getCurrentItem().equals(SetItemName(new ItemStack(Material.ARROW), "§3Back"))) {
                    // If the player clicks on the back button it will go back to the previous page
                    OpenInv((Player) e.getWhoClicked(), Integer.parseInt(e.getInventory().getName().replace("§4Spin items Page ", "")) - 2);

                } else if (e.getSlot() == e.getInventory().getSize() - 5 && e.getCurrentItem().equals(SetItemName(new ItemStack(Material.GOLD_INGOT), "§eAdd Money"))) {
                    // If the player clicks on the add money button it will open a sign GUI
                    SignPacket getSignMsg = new SignPacket();
                    getSignMsg.getSignMessage((Player) e.getWhoClicked(), items, Integer.parseInt(e.getInventory().getName().replace("§4Spin items Page ", "")) - 1, this);
                } else {
                    // If the player clicks on the remove button it will remove the item
                    items.remove(ItemLoreRem(e.getCurrentItem(), "§4§lClick To Remove"));
                    LoadItems(e.getInventory(), Integer.parseInt(e.getInventory().getName().replace("§4Spin items Page ", "")) - 1);

                }
            }

        }

    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent e) {
        if (saveInventory.contains(e.getInventory())) {
            saveInventory.remove(e.getInventory());
            Player p = (Player) e.getPlayer();
            PlayerInventory pInv = p.getInventory();
            for (int i = 0; i < pInv.getSize(); i++) {
                if (pInv.getItem(i) != null) {
                    pInv.setItem(i, ItemLoreRem(pInv.getItem(i), "§a§lClick To Add"));

                }
            }
        }

    }

    @EventHandler
    public void OnPlayerPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p) {

            if (saveInventory.contains(p.getOpenInventory().getTopInventory())) {
                e.getItem().setItemStack(ItemLoreAdd(e.getItem().getItemStack(), "§a§lClick To Add"));
            }
        }

    }

    /**
     * This method will add the lore to the item
     * @param item
     * @param lore
     * @return
     */
    private ItemStack ItemLoreAdd(ItemStack item, String lore) {
        ItemStack nItem = new ItemStack(item);
        ItemMeta itemMeta = nItem.getItemMeta();

        if (itemMeta.hasLore() && itemMeta.getLore().contains(lore))
            return nItem;

        if (itemMeta.hasLore()) {
            List < String > a = itemMeta.getLore();
            a.add(lore);
            itemMeta.setLore(a);
        } else {
            itemMeta.setLore(List.of(lore));
        }
        nItem.setItemMeta(itemMeta);
        return nItem;
    }

    /**
     * This method will remove the lore from the item
     * @author HKS_HNS
     * @return
     */
    private ItemStack ItemLoreRem(ItemStack item, String lore) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null || !itemMeta.hasLore() || !itemMeta.getLore().contains(lore))
            return item;

        if (itemMeta.getLore().size() == 1) {

            itemMeta.setLore(null);
        } else {

            List < String > a = itemMeta.getLore();
            a.remove(lore);
            itemMeta.setLore(a);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * This method will set the name of the item
     * @param item
     * @param name
     * @return
     */
    private ItemStack SetItemName(ItemStack item, String name) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null)
            return item;

        itemMeta.setDisplayName(name);

        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * This method will reload the items in the inventory to display them correctly
     * @author HKS_HNS
     */
    private void LoadItems(Inventory inv, int page) {
        inv.clear();
        int negative = 0;
        for (int i = (inv.getSize() - 9); i < inv.getSize(); i++) {
            inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }
        inv.setItem(inv.getSize() - 5, SetItemName(new ItemStack(Material.GOLD_INGOT), "§eAdd Money"));

        if ((inv.getSize() - 9) * (page + 1) < items.size()) {

            inv.setItem(inv.getSize() - 1, SetItemName(new ItemStack(Material.ARROW), "§3Next"));
            //negative = inv.getSize() - items.size() - 9;

            negative = (-items.size() + (inv.getSize() - 9) + ((inv.getSize() - 9) * page));
        }

        if (page > 0) {
            inv.setItem(inv.getSize() - 9, SetItemName(new ItemStack(Material.ARROW), "§3Back"));
        }

        for (int i = 0; i < (items.size() - ((inv.getSize() - 9) * page) + negative); i++) {

            inv.setItem(i, ItemLoreAdd(items.get(i + (page * (inv.getSize() - 9))), "§4§lClick To Remove"));

        }
    }

}