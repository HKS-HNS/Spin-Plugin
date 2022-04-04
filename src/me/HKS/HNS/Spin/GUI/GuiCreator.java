package me.HKS.HNS.Spin.GUI;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiCreator {

    /**
     * 
     * Creates a Inventory
     * 
     * @param Name - this is for the name from the Inventory
     * 
     * @param Slots - is the count of slots in a inventory 
     * 
     * @return returns the inventory if the slots aren't bigger then a double chest
     */
    public Inventory CreateInventory(String Name, int Slots) {
        Inventory inv;
        inv = Bukkit.createInventory(null, getsize(Slots), Name);
        return inv;
    }

    // Creates the inventory and tests if it is possible to create
    public int getsize(int Slots) {
        int size = 9;
        while (true) {
            if (size > 54) {
                return -1;
            }

            if (Slots <= size) {
                return size;
            }

            size += 9;
        }
    }

    // Creates an item for the Gui
    public ItemStack createGuiItem(final Material material, final String name, final String...lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

}