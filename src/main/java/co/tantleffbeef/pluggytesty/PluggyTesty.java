package co.tantleffbeef.pluggytesty;

import co.aikar.commands.PaperCommandManager;
import co.tantleffbeef.mcplanes.RecipeManager;
import co.tantleffbeef.mcplanes.ResourceApi;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.armor.HeavyArmor;
import co.tantleffbeef.pluggytesty.armor.effect_listeners.*;
import co.tantleffbeef.pluggytesty.bosses.*;
import co.tantleffbeef.pluggytesty.custom.item.MagicStickItemType;
import co.tantleffbeef.pluggytesty.custom.item.armor.CustomItemArmorType;
import co.tantleffbeef.pluggytesty.expeditions.PTPartyManager;
import co.tantleffbeef.pluggytesty.expeditions.commands.PartyCommand;
import co.tantleffbeef.pluggytesty.misc.PlayerDeathMonitor;
import co.tantleffbeef.pluggytesty.utility.*;
import co.tantleffbeef.pluggytesty.villagers.VillagerTrades;
import co.tantleffbeef.pluggytesty.weapons.*;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class PluggyTesty extends JavaPlugin {
    private static final long PARTY_INVITE_EXPIRATION_TIME_SECONDS = 60L;

    private ResourceManager resourceManager;
    private RecipeManager recipeManager;

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getLogger().info("grabbing resource manager");
        final var rApiProvider = getServer().getServicesManager().getRegistration(ResourceApi.class);
        if (rApiProvider == null)
            throw new RuntimeException("Can't find ResourceApi!");

        final var rApi = rApiProvider.getProvider();
        //final ResourceApi rApi = (ResourceApi) JavaPlugin.getProvidingPlugin(ResourceApi.class);
        resourceManager = rApi.getResourceManager();
        recipeManager = rApi.getRecipeManager();

        registerItems();
//        registerRecipes();

        // Adds all the textures and models in the resources folder to the resource pack
        try (JarFile jar = new JarFile(getFile())) {
            resourceManager.addAssetsFolder(jar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var partyManager = new PTPartyManager();

        final var commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new PartyCommand(this, getServer(), partyManager, PARTY_INVITE_EXPIRATION_TIME_SECONDS));

        getCommand("givemewood").setExecutor(new MagicStick());
        getCommand("givemerod").setExecutor(new BoltRod());
        getCommand("givemepole").setExecutor(new FrostPole());
        getCommand("givemeheal").setExecutor(new HealingHeart());
        //getCommand("givemesummon").setExecutor(new Summon());
        getCommand("givemed").setExecutor(new Digga());
        getCommand("givemesword").setExecutor(new SwordsmansDream());
        getCommand("givemego").setExecutor(new Goer());
        getCommand("givemecock").setExecutor((new Launcher()));
        getCommand("summonjawn").setExecutor(new BossJawn(this));
        getCommand("summonseaman").setExecutor(new BossSeaman(this));
        getCommand("giveheavyarmor").setExecutor(new HeavyArmor());
        getCommand("summongru").setExecutor(new BossGru(this));
        getCommand("summonbouncer").setExecutor(new BossFireWorker(this));
        getCommand("givemeClusterBomb").setExecutor(new ClusterBomb());
        getCommand("givemedash").setExecutor(new Dash());


        getServer().getPluginManager().registerEvents(new MagicStickInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BoltRodInteractListener(), this);
        getServer().getPluginManager().registerEvents(new FrostPoleInteractListener(), this);
        getServer().getPluginManager().registerEvents(new HealingHeartInteractListener(this), this);
        //getServer().getPluginManager().registerEvents(new SummonInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new DashInteractListener(), this);
        getServer().getPluginManager().registerEvents(new DiggaInteractListener(), this);
        getServer().getPluginManager().registerEvents(new SwordsmansDreamInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new GoerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new LauncherInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new ClusterBombInteractListener(this), this);

        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(), this);
        getServer().getPluginManager().registerEvents(new ExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEffectListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathMonitor(), this);

        getServer().getPluginManager().registerEvents(new VillagerTrades(), this);


        ArmorEquipEvent.registerListener(this);
    }

    private void registerItems() {
        // Weapons
        resourceManager.registerItem(new MagicStickItemType(this, "magic_stick", false,
                "Magic Stick"));

        // Armor
        resourceManager.registerItem(new CustomItemArmorType(this, "buffed_leather_helmet",
                Material.LEATHER_HELMET,
                true,
                "Buffed Leather Helmet",
                itemMeta -> {
            itemMeta.addAttributeModifier(
                    Attribute.GENERIC_ARMOR,
                    new AttributeModifier(UUID.randomUUID(), "pluggyTesty", 2,
                            AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HEAD));
                }));
    }

    public void addRecipe(Material material, Material addedMaterial, String name){
        var key = new NamespacedKey(this, name);
        getServer().addRecipe((new ShapelessRecipe(key, resourceManager.getCustomItemStack(key))
                .addIngredient(1, material))
                .addIngredient(1, addedMaterial));
        recipeManager.registerUnlockableRecipe(key, material);
    }
//   addRecipe();

    @Override
    public void onDisable() {
        getLogger().info("no more");
    }
}
