package pl.moderr.moderrkowo.core.cuboids.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.cuboids.ModerrCuboids;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.Objects;

public class PlaceRemoveCuboid implements Listener {

    @EventHandler
    public void cuboidPlace(BlockPlaceEvent e){
        if(e.getItemInHand().isSimilar(ModerrCuboids.getCuboid(1))){
            boolean canBuild = !e.isCancelled();
            BlockVector3 center = BukkitAdapter.asBlockVector(e.getBlockPlaced().getLocation());
            BlockVector3 minCheck = center.subtract(64-1,0,64-1);
            minCheck = minCheck.withY(0);
            BlockVector3 maxCheck = center.add(64-1,0,64-1);
            maxCheck = maxCheck.withY(255);
            ProtectedRegion checkArea = new ProtectedCuboidRegion("check", minCheck, maxCheck);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(e.getBlockPlaced().getWorld()));
            ApplicableRegionSet protectedRegions = null;
            ApplicableRegionSet protectedRegionsCheck = null;
            assert regions != null;
            if(regions.getRegion(ModerrCuboids.getCuboidNamePrefix().toLowerCase() + e.getPlayer().getName().toLowerCase()) != null){
                e.getPlayer().sendMessage(Main.getServerName() + ColorUtils.color(" &cJuż masz jedną działke!"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                canBuild = false;
                e.setCancelled(true);
                return;
            }
            if (regions != null) {
                protectedRegions = regions.getApplicableRegions(center);
                protectedRegionsCheck = regions.getApplicableRegions(checkArea);
                for(ProtectedRegion cuboid : protectedRegionsCheck){
                    e.getPlayer().sendMessage(Main.getServerName() + ColorUtils.color(" &cNie możesz postawić tutaj działki, znajduję się za blisko innej działki!"));
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                    canBuild = false;
                    e.setCancelled(true);
                    return;
                }
                for(ProtectedRegion cuboid : protectedRegions.getRegions()){
                    e.getPlayer().sendMessage(Main.getServerName() + ColorUtils.color(" &cNie możesz postawić drugiej działki!"));
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                    canBuild = false;
                    e.setCancelled(true);
                    return;
                }
            }
            if(canBuild){
                BlockVector3 min = center.subtract(64-1,0,64-1);
                min = min.withY(0);
                BlockVector3 max = center.add(64-1,0,64-1);
                max = max.withY(255);
                ProtectedRegion newCuboid = new ProtectedCuboidRegion(ModerrCuboids.getCuboidNamePrefix().toLowerCase() + e.getPlayer().getName().toLowerCase(), min, max);
                newCuboid.getOwners().addPlayer(e.getPlayer().getUniqueId());
                newCuboid.setFlag(Flags.GREET_MESSAGE, ColorUtils.color("&aWkroczyłeś na teren działki gracza &2" + newCuboid.getId().replace(ModerrCuboids.getCuboidNamePrefix().toLowerCase(), "").toUpperCase()));
                newCuboid.setFlag(Flags.FAREWELL_MESSAGE, ColorUtils.color("&aOpuszczasz teren działki gracza &2" + newCuboid.getId().replace(ModerrCuboids.getCuboidNamePrefix().toLowerCase(), "").toUpperCase()));
                newCuboid.setFlag(Flags.DENY_MESSAGE,  Main.getServerName() + ColorUtils.color(" &cNie masz uprawnień do interakcji na tej działce!"));
                assert regions != null;
                regions.addRegion(newCuboid);
                e.getBlockPlaced().setMetadata("cuboid", new FixedMetadataValue(Main.getInstance(), true));
                e.getBlockPlaced().setMetadata("cuboid-owner", new FixedMetadataValue(Main.getInstance(), e.getPlayer().getName()));
                e.getPlayer().sendMessage(Main.getServerName() + ColorUtils.color(" &aPostawiłeś własną prywatną działke! Aby zarządzać nią wpisz &2/dzialka"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1,1);
                try {
                    regions.save();
                } catch (StorageException storageException) {
                    storageException.printStackTrace();
                }
                e.getPlayer().spawnParticle(Particle.TOTEM, e.getPlayer().getLocation().getX(),e.getPlayer().getLocation().getY(),e.getPlayer().getLocation().getZ(),50,1,1,1,0.1f);
            }
        }
    }

    @EventHandler
    public void cuboidDestroy(BlockBreakEvent e){
        if(e.getBlock().hasMetadata("cuboid")){
            if(e.getBlock().getMetadata("cuboid-owner").get(0).asString().equals(e.getPlayer().getName())){
                BlockVector3 block = BukkitAdapter.asBlockVector(e.getBlock().getLocation());
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(BukkitAdapter.adapt(e.getBlock().getWorld()));
                assert regions != null;
                ApplicableRegionSet set = regions.getApplicableRegions(block);
                for(ProtectedRegion cuboid : set.getRegions()){
                    if(cuboid.getId().startsWith(ModerrCuboids.getCuboidNamePrefix())){
                        regions.removeRegion(cuboid.getId());
                        e.getPlayer().sendTitle(Main.getServerName(),ColorUtils.color(" &aPomyślnie usunięto cuboida!"));
                        e.setCancelled(true);
                        e.getBlock().setType(Material.AIR);
                        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), Objects.requireNonNull(ModerrCuboids.getCuboid(1)));
                        }
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES,1,1);
                        e.getBlock().removeMetadata("cuboid", Main.getInstance());
                        e.getBlock().removeMetadata("cuboid-owner", Main.getInstance());
                        try {
                            regions.save();
                        } catch (StorageException storageException) {
                            storageException.printStackTrace();
                        }
                    }
                }
            }else{
                e.getPlayer().sendTitle(Main.getServerName(), ColorUtils.color(" &cNie jesteś włascicielem tego cuboida! (" + e.getBlock().getMetadata("cuboid-owner").get(0).asString() + ")"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                e.getPlayer().spawnParticle(Particle.BARRIER, e.getBlock().getLocation().getX()+0.5f, e.getBlock().getLocation().getY()+1, e.getBlock().getLocation().getZ(),1);
                e.setCancelled(true);
            }
        }
    }

}
