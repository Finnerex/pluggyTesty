package co.tantleffbeef.pluggytesty;

import co.aikar.commands.*;
import co.tantleffbeef.mcplanes.*;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.armor.BaseArmor;
import co.tantleffbeef.pluggytesty.armor.HeavyArmor;
import co.tantleffbeef.pluggytesty.armor.effect_listeners.*;
import co.tantleffbeef.pluggytesty.attributes.CraftListener;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionBuilder;
import co.tantleffbeef.pluggytesty.expeditions.LocationTraverser;
import co.tantleffbeef.pluggytesty.expeditions.loading.*;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.RandomRoomLoader;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.SpecificRoomLoader;
import co.tantleffbeef.pluggytesty.extra_listeners.*;
import co.tantleffbeef.pluggytesty.levels.DisabledRecipeManager;
import co.tantleffbeef.pluggytesty.bosses.*;
import co.tantleffbeef.pluggytesty.custom.item.utility.*;
import co.tantleffbeef.pluggytesty.custom.item.weapons.*;
import co.tantleffbeef.pluggytesty.custom.item.armor.*;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.*;
import co.tantleffbeef.pluggytesty.expeditions.PTExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.parties.PTPartyManager;
import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.expeditions.parties.commands.PartyCommand;
import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import co.tantleffbeef.pluggytesty.expeditions.listeners.PTExpeditionManagerListener;
import co.tantleffbeef.pluggytesty.expeditions.loot.LootTableManager;
import co.tantleffbeef.pluggytesty.expeditions.loot.LootTableTestCommand;
import co.tantleffbeef.pluggytesty.expeditions.listeners.PartyFriendlyFireListener;
import co.tantleffbeef.pluggytesty.levels.LevelController;
import co.tantleffbeef.pluggytesty.levels.PTLevelController;
import co.tantleffbeef.pluggytesty.levels.YmlLevelStore;
import co.tantleffbeef.pluggytesty.levels.commands.LevelCommand;
import co.tantleffbeef.pluggytesty.misc.Debug;
import co.tantleffbeef.pluggytesty.misc.RandomGenTestCommand;
import co.tantleffbeef.pluggytesty.goober.GooberStateListener;
import co.tantleffbeef.pluggytesty.goober.OfflineGoober;
import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
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
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class PluggyTesty extends JavaPlugin {
    public static final int DEFAULT_PLAYER_LEVEL = 0;

    private static final long PARTY_INVITE_EXPIRATION_TIME_SECONDS = 60L;

    private ResourceManager resourceManager;
    private RecipeManager recipeManager;
    private KeyManager<CustomNbtKey> nbtKeyManager;
    private AttributeManager attributeManager;
    private LootTableManager lootTableManager;
    private LevelController levelController;
    private GooberStateController gooberStateController;

    @Override
    public void onEnable() {
        getLogger().info("penis hahaha");

        getLogger().info("grabbing resource manager");
        getLogger().info("poopie steam machine");

        Debug.setConsoleSender(getServer().getConsoleSender());
        Debug.setDebugMessagesEnabled(true);
        Debug.info("Debug messages enabled");

        Debug.info("testing some shit man");
        try {
            saveAllStartingWith("data/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        final var rApiProvider = getServer().getServicesManager().getRegistration(ResourceApi.class);
        if (rApiProvider == null)
            throw new RuntimeException("Can't find ResourceApi!");

        final var rApi = rApiProvider.getProvider();
        resourceManager = rApi.getResourceManager();
        recipeManager = rApi.getRecipeManager();
        nbtKeyManager = rApi.getNbtKeyManager();

        attributeManager = new AttributeManager(nbtKeyManager);
        lootTableManager = new LootTableManager(attributeManager);



        // Create level controller
        final var levelDataFilePath = getDataFolder().toPath().resolve("levels.yml");
        try {
            Files.createDirectories(levelDataFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        levelController = new PTLevelController(new YmlLevelStore(levelDataFilePath, DEFAULT_PLAYER_LEVEL, this.getServer()));

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

        gooberStateController = new GooberStateController(levelController, partyManager, getServer());

        final var commandManager = new PaperCommandManager(this);
        commandManager.getCommandContexts().registerIssuerAwareContext(Goober.class, context -> {
            final boolean isOptional = context.isOptional();

            if (!context.hasFlag("other")) {
                // If this is the sender
                final var player = context.getPlayer();
                if (player == null)
                    throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false);

                return gooberStateController.wrapPlayer(player);
            } else {
                // if this is an argument
                String arg = context.popFirstArg();
                if (arg == null) {
                    if (isOptional)
                        return null;

                    throw new InvalidCommandArgument();
                }

                final var player = ACFBukkitUtil.findPlayerSmart(context.getIssuer(), arg);
                if (player == null) {
                    if (isOptional)
                        return null;

                    throw new InvalidCommandArgument();
                }

                return gooberStateController.wrapPlayer(player);
            }
        });
        commandManager.getCommandContexts().registerContext(OfflineGoober.class, context -> {
            String name = context.popFirstArg();
            OfflinePlayer offlinePlayer;
            if (context.hasFlag("uuid")) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(name);
                } catch (IllegalArgumentException e) {
                    throw new InvalidCommandArgument(MinecraftMessageKeys.NO_PLAYER_FOUND_OFFLINE,
                            "{search}", name);
                }
                offlinePlayer = getServer().getOfflinePlayer(uuid);
            } else {
                offlinePlayer = getServer().getOfflinePlayer(name);
            }
            if (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()) {
                if (!context.hasFlag("uuid") && !commandManager.isValidName(name)) {
                    throw new InvalidCommandArgument(MinecraftMessageKeys.IS_NOT_A_VALID_NAME, "{name}", name);
                }
                throw new InvalidCommandArgument(MinecraftMessageKeys.NO_PLAYER_FOUND_OFFLINE,
                        "{search}", name);
            }
            return gooberStateController.wrapOfflinePlayer(offlinePlayer);
        });
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
        commandManager.registerCommand(new LevelCommand());
        final var randomGenTest = new RandomGenTestCommand(getServer().getScheduler(), this, 2500);
        commandManager.registerCommand(randomGenTest);

        getServer().getScheduler().runTaskTimer(this, randomGenTest, 1, 5);

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
        getServer().getPluginManager().registerEvents(new SpecialArrowShootListener(nbtKeyManager, resourceManager, this), this);

        // Trims / Armor effects
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(), this);
        getServer().getPluginManager().registerEvents(new ExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEffectListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerUnswimListener(), this);
        getServer().getPluginManager().registerEvents(new DashAbilityInteractListener(), this);

        getServer().getPluginManager().registerEvents(new GoatHornInteractListener(), this);

        // Special Keep-inventory
        getServer().getPluginManager().registerEvents(new PlayerDeathMonitor(), this);

        getServer().getPluginManager().registerEvents(new VillagerTradesListener(gooberStateController), this);
        getServer().getPluginManager().registerEvents(new CraftListener(attributeManager), this);
        getServer().getPluginManager().registerEvents(new SmithListener(), this);
        getServer().getPluginManager().registerEvents(new PartyFriendlyFireListener(partyManager), this);
        getServer().getPluginManager().registerEvents(new GooberStateListener(gooberStateController, getServer()), this);
        getServer().getPluginManager().registerEvents(new DisabledRecipeManager(this, gooberStateController, nbtKeyManager), this);


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

        final var expeditionController = new PTExpeditionController();
        final var expeditionBuilder = new ExpeditionBuilder(expeditionController, getServer(), "expeditions", new LocationTraverser(), 512);

        getServer().getPluginManager().registerEvents(new PTExpeditionManagerListener(expeditionController), this);

        Objects.requireNonNull(getCommand("testexpedition")).setExecutor((commandSender, command, s, strings) -> {
            if (!(commandSender instanceof Player player))
                return false;

            expeditionBuilder.buildExpedition(new ExpeditionInformation(
                    new SpecificRoomLoader(List.of(
                            new RoomInformationInstance(
                                    new RoomInformation(RoomType.SIMPLE_STARTING_ROOM,
                                            getDataFolder().toPath().resolve("data").resolve("rooms").resolve("test_expedition").resolve("te_room1.schem"), null, 0),
                                    null, new Vector3i(0, 0, 0), 0
                            ),
                            new RoomInformationInstance(
                                    new RoomInformation(RoomType.SIMPLE_EXIT,
                                            getDataFolder().toPath().resolve("data").resolve("rooms").resolve("test_expedition").resolve("te_room2.schem"), null, 0),
                                    null, new Vector3i(25, -5, 0), 0
                            )
                    )),
                    ExpeditionType.TEST_EXPEDITION
            )).whenComplete((r, e) -> {
                if (e != null)
                    e.printStackTrace();
            }).thenAccept(expedition -> getServer().getScheduler().runTask(this, () -> {
                final var party = Objects.requireNonNullElseGet(partyManager.getPartyWith(player), () -> {
                    final var newParty = new Party(getServer(), player);
                    partyManager.registerParty(newParty);
                    return newParty;
                });

                player.sendMessage("built expedition");
                party.broadcastMessage("starting expedition");

                expedition.start(party);
            }));

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

            expeditionController.quitExpedition(player);

            return true;
        });

        Objects.requireNonNull(getCommand("randomtestexpedition")).setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player))
                return false;

            final var goober = gooberStateController.wrapPlayer(player);

            final int numOptional;

            if (args.length > 0)
                numOptional = Integer.parseInt(args[0]);
            else
                numOptional = 3;

            final var teFolder = getDataFolder().toPath()
                    .resolve("data")
                    .resolve("rooms")
                    .resolve("test_expedition");

            final var roomDoors = List.of(
                    new RoomDoor(BlockFace.NORTH, Material.DIRT),
                    new RoomDoor(BlockFace.SOUTH, Material.DIRT),
                    new RoomDoor(BlockFace.EAST, Material.DIRT),
                    new RoomDoor(BlockFace.WEST, Material.DIRT)
            );

            final var firstRoom = new RoomInformation(RoomType.SIMPLE_STARTING_ROOM,
                    teFolder.resolve("te_room1.schem"),
                    roomDoors,
                    3);

            final var lastRoom = new RoomInformation(RoomType.SIMPLE_EXIT,
                    teFolder.resolve("te_room2.schem"),
                    roomDoors,
                    8);

            // required
            // re_room_1 5
            // re_room_5 5

            final List<RoomInformation> requiredRooms = List.of(
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_1.schem"),
                            roomDoors,
                            5
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_5.schem"),
                            roomDoors,
                            5
                    )
            );

            // optional
            // re_room_3 5
            // re_room_4 6
            // re_room_6 12
            // re_room_7 9
            // re_room_8 4
            final List<RoomInformation> optionalRooms = List.of(
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_3.schem"),
                            roomDoors,
                            5
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_4.schem"),
                            roomDoors,
                            6
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_6.schem"),
                            roomDoors,
                            12
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_7.schem"),
                            roomDoors,
                            9
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_8.schem"),
                            roomDoors,
                            4
                    )
            );

            expeditionBuilder.buildExpedition(new ExpeditionInformation(
                    new RandomRoomLoader(
                            firstRoom,
                            lastRoom,
                            requiredRooms,
                            optionalRooms,
                            numOptional
                    ),
                    ExpeditionType.TEST_EXPEDITION
            )).whenComplete((r, e) -> {
                if (e != null)
                    e.printStackTrace();
            }).thenAccept(exp -> getServer().getScheduler().runTask(this,
                    () -> exp.start(goober.getPartyOrCreate())));

            return true;
        });
    }

    private void saveAllStartingWith(@Language("RegExp") String match) throws IOException {
        final var jarPath = PluggyTesty.class.getProtectionDomain().getCodeSource().getLocation();

        try (final var jar = new JarFile(new File(jarPath.getPath()))) {
            final var e = jar.entries();

            while (e.hasMoreElements()) {
                final String entry = e.nextElement().getName();

                Debug.log(entry.startsWith(match) + ": " + entry);
            }
        }
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

        // Arrows
        resourceManager.registerItem(new JestersArrowItemType(this, "jesters_arrow", false, ChatColor.BLUE + "Jester's Arrow"));
        resourceManager.registerItem(new BouncyArrowItemType(this, "bouncy_arrow", false, ChatColor.BLUE + "Bouncy Arrow"));
        resourceManager.registerItem(new ExplosiveArrowItemType(this, "explosive_arrow", false, ChatColor.BLUE + "Explosive Arrow"));
        resourceManager.registerItem(new HomingArrowItemType(this, "homing_arrow", false, ChatColor.BLUE + "Homing Arrow"));
        resourceManager.registerItem(new HyperArrowItemType(this, "hyper_arrow", false, ChatColor.BLUE + "Hyper Arrow"));
        resourceManager.registerItem(new RambunctiousArrowItemType(this, "rambunctious_arrow", false, ChatColor.BLUE + "Rambunctious Arrow"));
        resourceManager.registerItem(new CrazyArrowItemType(this, "crazy_arrow", false, ChatColor.BLUE + "Crazy Arrow"));
        resourceManager.registerItem(new FrostburnArrowItemType(this, "frostburn_arrow", false, ChatColor.BLUE + "Frostburn Arrow"));
        resourceManager.registerItem(new FlamingArrowItemType(this, "flaming_arrow", false, ChatColor.BLUE + "Flaming Arrow"));


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
                new AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, 2, EquipmentSlot.CHEST));
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
        final ShapedRecipe chains = new ShapedRecipe(new NamespacedKey(this, "chain"), new ItemStack(Material.CHAIN))
                .shape( "i",
                        "i",
                        "i")
                .setIngredient('i', Material.IRON_NUGGET);
        getServer().addRecipe(chains);
        recipeManager.registerUnlockableRecipe(new NamespacedKey(this, "chain"), Material.IRON_NUGGET);


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
