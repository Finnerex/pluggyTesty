package co.tantleffbeef.pluggytesty.custom.item;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class GoldBagItemType extends SimpleItemType {
    private static final PlayerProfile GOLD_BAG_SKULL_OWNER;

    static {
        GOLD_BAG_SKULL_OWNER = Bukkit.createPlayerProfile(UUID.fromString("ce38e9ba-0644-4949-b7a6-c71e12060832"));

        // grab the gold bag's texture
        final var goldTexture = GOLD_BAG_SKULL_OWNER.getTextures();
        try {
            // set the skin for the bag
            goldTexture.setSkin(
                    new URL("http://textures.minecraft.net/texture/396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7"));
        } catch (MalformedURLException e) {
            Debug.alwaysError("failed to load the Gold Bag texture");
            throw new RuntimeException(e);
        }

        // save the textures back to the bag
        GOLD_BAG_SKULL_OWNER.setTextures(goldTexture);
    }

    public GoldBagItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.SKELETON_SKULL);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);

        if (!(meta instanceof SkullMeta skull)) {
            Debug.alwaysError("GoldBagItemType isn't a Skull!");
            return;
        }

        // set the owner of the skull to the textures we
        // calculated at startup
        skull.setOwnerProfile(GOLD_BAG_SKULL_OWNER);
    }
}
