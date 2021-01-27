package pl.moderr.moderrkowo.core.commands.player.rynek;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.database.data.RynekItem;

import java.util.ArrayList;

public class RynekManager {

    public final String RynekGUI_Name = ColorUtils.color("&6Rynek graczy &7- &eStrona %s");
    public ArrayList<RynekItem> rynekItems = new ArrayList<>();

    public RynekManager(){
        Load();
    }

    public void Save(){
        //ModerrkowoDatabase.getInstance().setRynek(rynekItems);
    }

    private void Load() {
        rynekItems.clear();
        /*ModerrkowoDatabase.getInstance().getRynek(new CallbackRynek() {
            @Override
            public void onDone(ArrayList<RynekItem> input) {
                rynekItems = input;
            }

            @Override
            public void onFail(Exception e) {

            }
        });*/
        rynekItems.add(new RynekItem(Bukkit.getPlayer("MODERR").getUniqueId(), new ItemStack(Material.DIAMOND, 3), 3));
    }


    public ItemStack getItemOfItemRynek(RynekItem item){
        ItemStack i = item.item.clone();
        ItemMeta meta = i.getItemMeta();
        assert meta != null;
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        if(lore == null){
            lore = new ArrayList<>();
        }
        lore.add(ColorUtils.color("&7Cena: &6" + item.cost + " diamentów"));
        lore.add(ColorUtils.color("&7Wystawione przez: &6" + Bukkit.getOfflinePlayer(item.owner).getName()));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public int getPages(){
        int viewItems = 0;
        int pages = 0;
        while(rynekItems.size() > viewItems){
            viewItems += 45;
            pages++;
        }
        return pages;
    }
    public ArrayList<RynekItem> getPage(int page){
        int startItem = (page-1)*45;
        ArrayList<RynekItem> pageList = new ArrayList<>();
        for(int i = startItem; i != startItem+45; i++){
            try{
                pageList.add(rynekItems.get(i));
            }catch(Exception ignored){

            }
        }
        return pageList;
    }
    public Inventory getRynekInventory(int page){
        int size = 54-1;
        ArrayList<RynekItem> pageItems = getPage(page);
        int availablePages = getPages();
        Inventory inv = Bukkit.createInventory(null, 54, String.format(RynekGUI_Name, page + ""));
        for (int i = size-8; i != size+1; i++){
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        if(page > 1){
            inv.setItem(size-9, ItemStackUtils.createGuiItem(Material.ARROW, 1, ColorUtils.color("&cPoprzednia strona")));
        }
        if(page < availablePages){
            inv.setItem(size, ItemStackUtils.createGuiItem(Material.ARROW, 1, ColorUtils.color("&cNastępna strona")));
        }
        inv.setItem(size-4, ItemStackUtils.createGuiItem(Material.DIAMOND, 1, ColorUtils.color("&aTwoje oferty")));
        if(pageItems.size() > 0){
            for (RynekItem items : pageItems){
                inv.addItem(getItemOfItemRynek(items));
            }
        }
        return inv;
    }

}
