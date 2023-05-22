package co.tantleffbeef.pluggytesty;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.tantleffbeef.mcplanes.*;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.armor.BaseArmor;
import co.tantleffbeef.pluggytesty.armor.HeavyArmor;
import co.tantleffbeef.pluggytesty.armor.effect_listeners.*;
import co.tantleffbeef.pluggytesty.attributes.CraftListener;
import co.tantleffbeef.pluggytesty.bosses.*;
import co.tantleffbeef.pluggytesty.custom.item.utility.*;
import co.tantleffbeef.pluggytesty.custom.item.weapons.*;
import co.tantleffbeef.pluggytesty.custom.item.armor.*;
import co.tantleffbeef.pluggytesty.expeditions.PTExpeditionManager;
import co.tantleffbeef.pluggytesty.expeditions.PTPartyManager;
import co.tantleffbeef.pluggytesty.expeditions.Party;
import co.tantleffbeef.pluggytesty.expeditions.TestExpedition;
import co.tantleffbeef.pluggytesty.expeditions.commands.PartyCommand;
import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import co.tantleffbeef.pluggytesty.expeditions.listeners.PTExpeditionManagerListener;
import co.tantleffbeef.pluggytesty.expeditions.loot.LootTableManager;
import co.tantleffbeef.pluggytesty.expeditions.loot.LootTableTestCommand;
import co.tantleffbeef.pluggytesty.expeditions.listeners.PartyFriendlyFireListener;
import co.tantleffbeef.pluggytesty.misc.Debug;
import co.tantleffbeef.pluggytesty.misc.GoatHornInteractListener;
import co.tantleffbeef.pluggytesty.misc.PlayerDeathMonitor;
import co.tantleffbeef.pluggytesty.villagers.VillagerTradesListener;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class PluggyTesty extends JavaPlugin {
    private static final long PARTY_INVITE_EXPIRATION_TIME_SECONDS = 60L;

    private ResourceManager resourceManager;
    private RecipeManager recipeManager;
    private KeyManager<CustomNbtKey> nbtKeyManager;
    private AttributeManager attributeManager;
    private LootTableManager lootTableManager;

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getLogger().info("grabbing resource manager");
        getLogger().info("poopie steam machine");

        Debug.setConsoleSender(getServer().getConsoleSender());
        Debug.setDebugMessagesEnabled(true);
        Debug.info("Debug messages enabled");

        final var rApiProvider = getServer().getServicesManager().getRegistration(ResourceApi.class);
        if (rApiProvider == null)
            throw new RuntimeException("Can't find ResourceApi!");

        final var rApi = rApiProvider.getProvider();
        resourceManager = rApi.getResourceManager();
        recipeManager = rApi.getRecipeManager();
        nbtKeyManager = rApi.getNbtKeyManager();

        attributeManager = new AttributeManager(nbtKeyManager);
        lootTableManager = new LootTableManager(attributeManager);

        registerItems();
//        registerRecipes();

        // Adds all the textures and models in the resources folder to the resource pack
        try (JarFile jar = new JarFile(getFile())) {
            resourceManager.addAssetsFolder(jar);
            resourceManager.registerItemTextureAtlasDirectory("misc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var partyManager = new PTPartyManager();
        final var commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("partyPlayers", context -> {
            final var player = context.getPlayer();
            final var party = partyManager.getPartyWith(player);

            if (party == null)
                throw new InvalidCommandArgument();

            return party.getAllPlayers().stream()
                    .map(OfflinePlayer::getName)
                    .toList();
        });

        commandManager.registerCommand(new PartyCommand(this, getServer(), partyManager, PARTY_INVITE_EXPIRATION_TIME_SECONDS));

        getCommand("summonjawn").setExecutor(new BossJawn(this));
        getCommand("summonseaman").setExecutor(new BossSeaman(this));
        getCommand("giveheavyarmor").setExecutor(new HeavyArmor());
        getCommand("summongru").setExecutor(new BossGru(this));
        getCommand("summonbouncer").setExecutor(new BossFireWorker(this));
        getCommand("chesttest").setExecutor(new LootTableTestCommand(this, lootTableManager));
        getCommand("trial1boss").setExecutor(new BossTrial1(this));
        getCommand("trial2boss").setExecutor(new BossTrial2(this));
        getCommand("trial3boss").setExecutor(new BossTrial3(this));
        getCommand("trial4boss").setExecutor(new BossTrial4(this));
        getCommand("trial5boss").setExecutor(new BossTrial5(this));


        getServer().getPluginManager().registerEvents(new RandomEffectBowInteractListener(nbtKeyManager, resourceManager), this);

        // Trims / Armor effects
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(), this);
        getServer().getPluginManager().registerEvents(new ExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEffectListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerUnswimListener(), this);

        getServer().getPluginManager().registerEvents(new GoatHornInteractListener(), this);

        // Special Keep-inventory
        getServer().getPluginManager().registerEvents(new PlayerDeathMonitor(), this);

        getServer().getPluginManager().registerEvents(new VillagerTradesListener(), this);
        getServer().getPluginManager().registerEvents(new CraftListener(attributeManager), this);
        getServer().getPluginManager().registerEvents(new SmithListener(), this);
        getServer().getPluginManager().registerEvents(new PartyFriendlyFireListener(partyManager), this);


        ArmorEquipEvent.registerListener(this);

        registerRecipes();

        // After we know all custom items have been registered (hopefully)
        // we can add them to the attribute manager
        getServer().getScheduler().runTask(
                this,
                // Loop through all items in the resource manager
                // and register them in the attribute manager
                () -> { resourceManager.getItemIdList().stream()
                        .map(resourceManager::getCustomItemStack)
                        .forEach(attributeManager::registerModifiedItem);
                    getServer().getScheduler().runTask(this, () -> lootTableManager.registerLootTables());
                }
        );
        addCustomAttributes();

        // Check every once in a while if player inventories need to be updated
        /*new BukkitRunnable() {
            private int listIndex = 0;
            private List<? extends Player> playerList = null;

            @Override
            public void run() {
                // If we're higher than the list then reset
                if (playerList == null || listIndex >= playerList.size()) {
                    listIndex = 0;
                    playerList = getServer().getOnlinePlayers().stream().toList();
                    return;
                }

                final var player = playerList.get(listIndex);

                if (!player.isOnline())
                    return;

                final var inventory = player.getInventory();
                attributeManager.checkInventory(inventory);

                player.updateInventory();

                listIndex++;
            }
        }.runTaskTimer(this, 3, 7);*/

        final var expeditionManager = new PTExpeditionManager(
                partyManager,
                getServer().getScheduler(),
                getServer(),
                "expeditions",
                256
        );

        getServer().getPluginManager().registerEvents(new PTExpeditionManagerListener(expeditionManager), this);

        Objects.requireNonNull(getCommand("testexpedition")).setExecutor((commandSender, command, s, strings) -> {
            if (!(commandSender instanceof Player player))
                return false;

            final var expedition = new TestExpedition(this);

            expeditionManager.buildExpedition(
                    // The expedition to build
                    expedition,

                    // post build callback
                    rooms -> {
                        var party = partyManager.getPartyWith(player);
                        if (party == null) {
                            party = new Party(getServer(), player);
                            partyManager.registerParty(party);
                        }

                        player.sendMessage("expedition built or something");
                        expeditionManager.startExpedition(expedition, party);
                    },

                    // error callback
                    Throwable::printStackTrace
            );

            return true;
        });

        Objects.requireNonNull(getCommand("selectioninfo")).setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player))
                return false;

            final var wePlayer = BukkitAdapter.adapt(player);
            try {
                final var clipboards = wePlayer.getSession().getClipboard().getClipboards();
                if (clipboards.size() < 1) {
                    player.sendMessage("No clipboard!");
                    return true;
                }

                final var clipboard = clipboards.get(0);

                player.sendMessage("minimum: " + clipboard.getMinimumPoint());
                player.sendMessage("maximum: " + clipboard.getMaximumPoint());
            } catch (EmptyClipboardException e) {
                player.sendMessage("No clipboard!");
            }

            return true;
        });

        Objects.requireNonNull(getCommand("leaveexpedition")).setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player))
                return false;

            expeditionManager.quitExpedition(player);

            return true;
        });
    }

    private void registerItems() {
        // Weapons
        resourceManager.registerItem(new MagicStickItemType(this, "magic_stick", false, "Magic Stick"));
        resourceManager.registerItem(new AxeOfYourMotherItemType(this, "mother_axe", false, ChatColor.AQUA + "Axe of Your Mother"));
        resourceManager.registerItem(new BoltRodItemType(this, "bolt_rod", false, ChatColor.GOLD + "Bolt Rod"));
        resourceManager.registerItem(new ClusterBombItemType(this, "cluster_bomb", false, "Cluster Bomb"));
        resourceManager.registerItem(new FrostPoleItemType(this, "frost_pole", false, ChatColor.AQUA + "Frost Pole"));
        resourceManager.registerItem(new SwordsmansDreamItemType(this, "swordsmans_dream", false, ChatColor.AQUA + "Swordsman's Dream"));
        resourceManager.registerItem(new RandomEffectBowItemType(this, "random_bow", false, "Random Effect Bow"));
        resourceManager.registerItem(new LauncherItemType(this, "launcher", false, "Launcher"));
        resourceManager.registerItem(new MinotaursAxeItemType(this, "minotaurs_axe", false, ChatColor.DARK_GREEN + "Minotaur's Axe"));
        resourceManager.registerItem(new ZapinatorItemType(this, "zapinator", false, ChatColor.GOLD + "Zapinator"));
        resourceManager.registerItem(new MagicMissileItemType(this, "magic_missile", false, ChatColor.BLUE + "Magic Missile"));
        resourceManager.registerItem(new MeteorStaffItemType(this, "meteor_staff", false, ChatColor.DARK_GRAY + "Meteor Staff"));
        resourceManager.registerItem(new LifeDrainItemType(this, "life_drain", false, ChatColor.DARK_RED + "Life Drain"));
        resourceManager.registerItem(new NimbusRodItemType(this, "nimbus_rod", false, ChatColor.DARK_BLUE + "Nimbus Rod"));
        resourceManager.registerItem(new YoyoItemType(this, "yoyo", false, ChatColor.DARK_BLUE + "Yoyo"));
        resourceManager.registerItem(new MeowmereItemType(this, "meowmere", false, ChatColor.DARK_BLUE + "Meowmere"));
        resourceManager.registerItem(new MagnetSphereItemType(this, "magnet_sphere", false, ChatColor.AQUA + "Magnet Sphere"));


        // Utility
        resourceManager.registerItem(new GoItemType(this, "go", false, "Go!"));
        resourceManager.registerItem(new HealingHeartItemType(this, "healing_heart", false, ChatColor.RED + "Healing Heart"));
        resourceManager.registerItem(new DashItemType(this, "dash", false, "Dash"));
        resourceManager.registerItem(new DiggaItemType(this, "digga", false, "Digga"));

        // Armor
        resourceManager.registerItem(new SimpleItemType(this, "buffed_leather_helmet", true, ChatColor.AQUA + "Buffed" + ChatColor.WHITE + "Leather Hat", Material.LEATHER_HELMET));
    }

    private void addCustomAttributes() {
        // modify a bunch of vanilla items

        // Armor

        // leather
        addCustomAttributeToVanillaItem(Material.LEATHER_HELMET,
                new AttributePair(Attribute.GENERIC_ARMOR, 1,
                        EquipmentSlot.HEAD));
        addCustomAttributeToVanillaItem(Material.LEATHER_CHESTPLATE,
                new AttributePair(Attribute.GENERIC_ARMOR, 1,
                        EquipmentSlot.CHEST));
        addCustomAttributeToVanillaItem(Material.LEATHER_LEGGINGS,
                new AttributePair(Attribute.GENERIC_ARMOR, 1,
                        EquipmentSlot.LEGS));
        addCustomAttributeToVanillaItem(Material.LEATHER_BOOTS,
                new AttributePair(Attribute.GENERIC_ARMOR, 1,
                        EquipmentSlot.FEET));

        // chain
        addCustomAttributeToVanillaItem(Material.CHAINMAIL_HELMET,
                new AttributePair(Attribute.GENERIC_ARMOR, 2,
                        EquipmentSlot.HEAD));
        addCustomAttributeToVanillaItem(Material.CHAINMAIL_CHESTPLATE,
                new AttributePair(Attribute.GENERIC_ARMOR, 2,
                        EquipmentSlot.CHEST));
        addCustomAttributeToVanillaItem(Material.CHAINMAIL_LEGGINGS,
                new AttributePair(Attribute.GENERIC_ARMOR, 2,
                        EquipmentSlot.LEGS));
        addCustomAttributeToVanillaItem(Material.CHAINMAIL_BOOTS,
                new AttributePair(Attribute.GENERIC_ARMOR, 2,
                        EquipmentSlot.FEET));

        // iron
        addCustomAttributeToVanillaItem(Material.IRON_HELMET,
                new AttributePair(Attribute.GENERIC_ARMOR, 3,
                        EquipmentSlot.HEAD));
        addCustomAttributeToVanillaItem(Material.IRON_CHESTPLATE,
                new AttributePair(Attribute.GENERIC_ARMOR, 3,
                        EquipmentSlot.CHEST));
        addCustomAttributeToVanillaItem(Material.IRON_LEGGINGS,
                new AttributePair(Attribute.GENERIC_ARMOR, 3,
                        EquipmentSlot.LEGS));
        addCustomAttributeToVanillaItem(Material.IRON_BOOTS,
                new AttributePair(Attribute.GENERIC_ARMOR, 3,
                        EquipmentSlot.FEET));

        // gold
        addCustomAttributeToVanillaItem(Material.GOLDEN_HELMET,
                new AttributePair(Attribute.GENERIC_ARMOR, 4, EquipmentSlot.HEAD),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 1, EquipmentSlot.HEAD));
        addCustomAttributeToVanillaItem(Material.GOLDEN_CHESTPLATE,
                new AttributePair(Attribute.GENERIC_ARMOR, 4, EquipmentSlot.CHEST),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 1, EquipmentSlot.CHEST));
        addCustomAttributeToVanillaItem(Material.GOLDEN_LEGGINGS,
                new AttributePair(Attribute.GENERIC_ARMOR, 4, EquipmentSlot.LEGS),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 1, EquipmentSlot.LEGS));
        addCustomAttributeToVanillaItem(Material.GOLDEN_BOOTS,
                new AttributePair(Attribute.GENERIC_ARMOR, 4, EquipmentSlot.FEET),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 1, EquipmentSlot.FEET));

        // diamond
        addCustomAttributeToVanillaItem(Material.DIAMOND_HELMET,
                new AttributePair(Attribute.GENERIC_ARMOR, 5, EquipmentSlot.HEAD),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 2, EquipmentSlot.HEAD));
        addCustomAttributeToVanillaItem(Material.DIAMOND_CHESTPLATE,
                new AttributePair(Attribute.GENERIC_ARMOR, 5, EquipmentSlot.CHEST),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 2));
        addCustomAttributeToVanillaItem(Material.DIAMOND_LEGGINGS,
                new AttributePair(Attribute.GENERIC_ARMOR, 5, EquipmentSlot.LEGS),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 2, EquipmentSlot.LEGS));
        addCustomAttributeToVanillaItem(Material.DIAMOND_BOOTS,
                new AttributePair(Attribute.GENERIC_ARMOR, 5, EquipmentSlot.FEET),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 2, EquipmentSlot.FEET));

        // netherite
        addCustomAttributeToVanillaItem(Material.NETHERITE_HELMET,
                new AttributePair(Attribute.GENERIC_ARMOR, 6, EquipmentSlot.HEAD),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 3, EquipmentSlot.HEAD));
        addCustomAttributeToVanillaItem(Material.NETHERITE_CHESTPLATE,
                new AttributePair(Attribute.GENERIC_ARMOR, 6, EquipmentSlot.CHEST),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 3, EquipmentSlot.CHEST));
        addCustomAttributeToVanillaItem(Material.NETHERITE_LEGGINGS,
                new AttributePair(Attribute.GENERIC_ARMOR, 6, EquipmentSlot.LEGS),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 3, EquipmentSlot.LEGS));
        addCustomAttributeToVanillaItem(Material.NETHERITE_BOOTS,
                new AttributePair(Attribute.GENERIC_ARMOR, 6, EquipmentSlot.FEET),
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 3, EquipmentSlot.FEET));

    }

    private static final class AttributePair {
        public final Attribute attribute;
        public final double amount;
        public final AttributeModifier.Operation operation;
        public final EquipmentSlot slot;

        public AttributePair(Attribute attribute, double amount, AttributeModifier.Operation operation, @Nullable EquipmentSlot slot) {
            this.attribute = attribute;
            this.amount = amount;
            this.operation = operation;
            this.slot = slot;
        }

        public AttributePair(Attribute attribute, double amount, @Nullable EquipmentSlot slot) {
            this(attribute, amount, AttributeModifier.Operation.ADD_NUMBER, slot);
        }

        public AttributePair(Attribute attribute, double amount) {
            this(attribute, amount, null);
        }
    }

    private void addCustomAttributeToVanillaItem(@NotNull Material vanillaMaterial, @NotNull AttributePair... pairs) {
        // Create item object to hold the modifications
        final var vanillaItem = new ItemStack(vanillaMaterial);
        final var itemMeta = Objects.requireNonNull(vanillaItem.getItemMeta());

        // Loop through every pair passed into the method and add an attribute modifier
        for (final var pair : pairs) {
            itemMeta.addAttributeModifier(pair.attribute, new AttributeModifier(UUID.randomUUID(), "pluggyTesty", pair.amount, pair.operation, pair.slot));
        }

        // save the modified meta back
        vanillaItem.setItemMeta(itemMeta);

        // register the item
        attributeManager.registerModifiedItem(vanillaItem);
    }

    @Override
    public void onDisable() {
        getLogger().info("no more");
    }

    private void registerRecipes() {
//        final ShapedRecipe chains = new ShapedRecipe(NamespacedKey.minecraft("chain"), new ItemStack(Material.CHAIN))
//                .shape( "i",
//                        "i",
//                        "i")
//                .setIngredient('i', Material.IRON_NUGGET);
//        getServer().addRecipe(chains);
//        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chain"), Material.IRON_NUGGET);


        final ShapedRecipe chainHelm = new ShapedRecipe(NamespacedKey.minecraft("chainmail_helmet"), BaseArmor.cH())
                .shape(
                        "ccc",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainHelm);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_helmet"), Material.CHAIN);


        final ShapedRecipe chainBoots = new ShapedRecipe(NamespacedKey.minecraft("chainmail_boots"), BaseArmor.cB())
                .shape(
                        "c c",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainBoots);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_boots"), Material.CHAIN);


        final ShapedRecipe chainChestplate = new ShapedRecipe(NamespacedKey.minecraft("chainmail_chestplate"), BaseArmor.cC())
                .shape(
                        "c c",
                        "ccc",
                        "ccc")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainChestplate);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_chestplate"), Material.CHAIN);


        final ShapedRecipe chainLeggings = new ShapedRecipe(NamespacedKey.minecraft("chainmail_leggings"), BaseArmor.cL())
                .shape(
                        "ccc",
                        "c c",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainLeggings);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_leggings"), Material.CHAIN);
    }

}
