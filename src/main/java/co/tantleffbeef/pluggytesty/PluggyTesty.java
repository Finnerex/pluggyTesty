package co.tantleffbeef.pluggytesty;

import co.aikar.commands.*;
import co.tantleffbeef.mcplanes.*;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.armor.PureArmor;
import co.tantleffbeef.pluggytesty.armor.HeavyArmor;
import co.tantleffbeef.pluggytesty.armor.effect_listeners.*;
import co.tantleffbeef.pluggytesty.attributes.AttributeUpdateListener;
import co.tantleffbeef.pluggytesty.custom.item.weapons.TNT.StickyTntItemType;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionBuilder;
import co.tantleffbeef.pluggytesty.expeditions.LocationTraverser;
import co.tantleffbeef.pluggytesty.expeditions.commands.ReloadExpeditionsCommand;
import co.tantleffbeef.pluggytesty.expeditions.commands.RunExpeditionCommand;
import co.tantleffbeef.pluggytesty.expeditions.loading.*;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.RoomLoader;
import co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters.*;
import co.tantleffbeef.pluggytesty.expeditions.parties.PartyManager;
import co.tantleffbeef.pluggytesty.extra_listeners.*;
import co.tantleffbeef.pluggytesty.extra_listeners.custom_items.*;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUIManager;
import co.tantleffbeef.pluggytesty.inventoryGUI.TestGUIItemType;
import co.tantleffbeef.pluggytesty.levels.DisabledRecipeManager;
import co.tantleffbeef.pluggytesty.bosses.*;
import co.tantleffbeef.pluggytesty.custom.item.utility.*;
import co.tantleffbeef.pluggytesty.custom.item.weapons.*;
import co.tantleffbeef.pluggytesty.custom.item.armor.*;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.*;
import co.tantleffbeef.pluggytesty.expeditions.PTExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.parties.PTPartyManager;
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
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class PluggyTesty extends JavaPlugin {
    public static final int DEFAULT_PLAYER_LEVEL = 0;
    private static final long PARTY_INVITE_EXPIRATION_TIME_SECONDS = 60L;
    public static final Collection<Entity> removeOnDisable = new ArrayList<>();

    private ResourceManager resourceManager;
    private BlockManager blockManager;
    private RecipeManager recipeManager;
    private KeyManager<CustomNbtKey> nbtKeyManager;
    private AttributeManager attributeManager;
    private LootTableManager lootTableManager;
    private LevelController levelController;
    private GooberStateController gooberStateController;
    private PartyManager partyManager;
    private final BiMap<String, RoomInformation> roomInformationBiMap = HashBiMap.create();
    private final BiMap<String, ExpeditionInformation> expeditionInformationBiMap = HashBiMap.create();
    private ExpeditionBuilder expeditionBuilder;
    private PTExpeditionController expeditionController;

    @Override
    public void onLoad() {
        // TODO: make this use config system
        if (!getServer().getWorlds().isEmpty())
            return;

        final Path worldContainer = getServer().getWorldContainer().toPath();

        final Path expeditionsWorldFolder = worldContainer.resolve("expeditions");
        if (Files.isDirectory(expeditionsWorldFolder)) {
            // delete all files in the folder
            try (final var walk = Files.walk(expeditionsWorldFolder)) {
                walk.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            deleteFolderRecursively(expeditionsWorldFolder);
        }
    }

    private void deleteFolderRecursively(Path folder) {
        try (final var files = Files.list(folder);
            final var files2 = Files.list(folder)) {
            if (files.findAny().isPresent())
                files2.forEach(this::deleteFolderRecursively);

            Files.delete(folder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        blockManager = rApi.getBlockManager();

        attributeManager = new AttributeManager(nbtKeyManager);
        lootTableManager = new LootTableManager(attributeManager);
        partyManager = new PTPartyManager();


        // Create level controller
        final var levelDataFilePath = getDataFolder().toPath().resolve("levels.yml");
        try {
            Files.createDirectories(levelDataFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        levelController = new PTLevelController(new YmlLevelStore(levelDataFilePath, DEFAULT_PLAYER_LEVEL, this.getServer()));


//        registerRecipes();

        // Adds all the textures and models in the resources folder to the resource pack
        try (JarFile jar = new JarFile(getFile())) {
            resourceManager.addAssetsFolder(jar);
            resourceManager.registerItemTextureAtlasDirectory("misc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        expeditionController = new PTExpeditionController();
        expeditionBuilder = new ExpeditionBuilder(expeditionController, getServer(), "expeditions", new LocationTraverser(), 512);

        getServer().getPluginManager().registerEvents(new PTExpeditionManagerListener(expeditionController), this);

        gooberStateController = new GooberStateController(levelController, partyManager, expeditionController, getServer());

        registerItems();

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
        getCommand("runexpedition").setExecutor(new RunExpeditionCommand(expeditionInformationBiMap, getServer(), getServer().getScheduler(), expeditionBuilder, this, gooberStateController));
        getCommand("reloadexpeditions").setExecutor(new ReloadExpeditionsCommand(this));


        getServer().getPluginManager().registerEvents(new RandomEffectBowInteractListener(nbtKeyManager, resourceManager), this);
        getServer().getPluginManager().registerEvents(new FisherOfSoulsEventListener(nbtKeyManager, resourceManager), this);
        getServer().getPluginManager().registerEvents(new FeatherBootsSneakListener(nbtKeyManager, resourceManager), this);
        getServer().getPluginManager().registerEvents(new LandMineDropListener(nbtKeyManager, resourceManager, this), this);
        getServer().getPluginManager().registerEvents(new LifeLinkListener(nbtKeyManager, resourceManager, this.getServer()), this);
        getServer().getPluginManager().registerEvents(new SpecialArrowShootListener(nbtKeyManager, resourceManager, this), this);
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);

        // Trims / Armor effects
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(), this);
        getServer().getPluginManager().registerEvents(new ExpChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEffectListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerUnswimListener(), this);
        getServer().getPluginManager().registerEvents(new DashAbilityInteractListener(this), this);

        getServer().getPluginManager().registerEvents(new GoatHornInteractListener(), this);

        // Special Keep-inventory
        getServer().getPluginManager().registerEvents(new PlayerDeathMonitor(), this);

        getServer().getPluginManager().registerEvents(new VillagerTradesListener(gooberStateController), this);
        getServer().getPluginManager().registerEvents(new PartyFriendlyFireListener(partyManager), this);
        getServer().getPluginManager().registerEvents(new GooberStateListener(gooberStateController, getServer()), this);
        getServer().getPluginManager().registerEvents(new DisabledRecipeManager(this, gooberStateController, nbtKeyManager), this);
        getServer().getPluginManager().registerEvents(new InventoryGUIManager(), this);
        getServer().getPluginManager().registerEvents(new AttributeUpdateListener(attributeManager), this);
//        getServer().getPluginManager().registerEvents(new EntityDeathGarbageCollector(), this);


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

        // load rooms asynchronously
        getServer().getScheduler().runTaskAsynchronously(this, this::loadRoomsAndExpeditionsDefaultLogger);

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

        /*Objects.requireNonNull(getCommand("testexpedition")).setExecutor((commandSender, command, s, strings) -> {
            if (!(commandSender instanceof Player player))
                return false;

            expeditionBuilder.buildExpedition(new ExpeditionInformation(
                    new SpecificRoomLoader(List.of(
                            new RoomInformationInstance(
                                    new RoomInformation(RoomType.SIMPLE_STARTING_ROOM,
                                            getDataFolder().toPath().resolve("data").resolve("rooms").resolve("test_expedition").resolve("te_room1.schem"), null, null),
                                    null, new Vector3i(0, 0, 0), 0, 0
                            ),
                            new RoomInformationInstance(
                                    new RoomInformation(RoomType.SIMPLE_EXIT,
                                            getDataFolder().toPath().resolve("data").resolve("rooms").resolve("test_expedition").resolve("te_room2.schem"), null, null),
                                    null, new Vector3i(25, -5, 0), 0, 0
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
        });*/

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

            final var goober = gooberStateController.wrapPlayer(player);
            if (goober.getParty().isEmpty() || goober.getExpedition().isEmpty()) {
                goober.asPlayer().sendMessage("You are not in an expedition!");
                return true;
            }

            expeditionController.quitExpedition(player);

            return true;
        });

        /*Objects.requireNonNull(getCommand("randomtestexpedition")).setExecutor((sender, command, label, args) -> {
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

            final var firstRoom = new RoomInformation(RoomType.SIMPLE_STARTING_ROOM,
                    teFolder.resolve("te_room1.schem"),
                    null,
                    List.of(new ConsistentHeightRoomDoor(BlockFace.WEST, Material.DIRT, 3))
            );

            final var lastRoom = new RoomInformation(RoomType.SIMPLE_EXIT,
                    teFolder.resolve("te_room2.schem"),
                    null,
                    List.of(new ConsistentHeightRoomDoor(BlockFace.SOUTH, Material.DIRT, 8))
            );

            // required
            // re_room_1 5
            // re_room_5 5

            final List<RoomInformation> requiredRooms = List.of(
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_1.schem"),
                            null,
                            fourDoorsSameHeight(5)
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("tall_room.schem"),
                            null,
                            List.of(
                                    new ConsistentHeightRoomDoor(BlockFace.WEST, Material.DIRT, 2),
                                    new ConsistentHeightRoomDoor(BlockFace.NORTH, Material.DIRT, 10),
                                    new ConsistentHeightRoomDoor(BlockFace.EAST, Material.DIRT, 30),
                                    new ConsistentHeightRoomDoor(BlockFace.SOUTH, Material.DIRT, 48)
                            )
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_5.schem"),
                            null,
                            fourDoorsSameHeight(5)
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
                            null,
                            fourDoorsSameHeight(5)
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_4.schem"),
                            null,
                            fourDoorsSameHeight(6)
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_6.schem"),
                            null,
                            fourDoorsSameHeight(12)
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_7.schem"),
                            null,
                            fourDoorsSameHeight(9)
                    ),
                    new RoomInformation(
                            RoomType.EMPTY,
                            teFolder.resolve("re_room_8.schem"),
                            null,
                            fourDoorsSameHeight(4)
                    )
            );

            expeditionBuilder.buildExpedition(new ExpeditionInformation(
                    new RandomRoomLoader(
                            firstRoom,
                            lastRoom,
                            requiredRooms,
                            optionalRooms,
                            25,
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

        Objects.requireNonNull(getCommand("printrooms")).setExecutor((sender, command, label, args) -> {
            if (args.length > 0) {
                var id = args[0];
                sender.sendMessage("Room with id: " + id);
                synchronized (roomInformationBiMap) {
                    sender.sendMessage(String.valueOf(roomInformationBiMap.get(id)));
                }

                return true;
            }

            sender.sendMessage("Rooms: ");

            synchronized (roomInformationBiMap) {
                for (final var entry : roomInformationBiMap.entrySet()) {
                    sender.sendMessage("id: " + entry.getKey());
                    sender.sendMessage(entry.getValue().toString());
                    sender.sendMessage();
                }
            }

            return true;
        });*/

        Objects.requireNonNull(getCommand("printexps")).setExecutor((sender, command, label, args) -> {
            if (args.length > 0) {
                var id = args[0];
                sender.sendMessage("Expedition with id: " + id);
                synchronized (expeditionInformationBiMap) {
                    sender.sendMessage(String.valueOf(expeditionInformationBiMap.get(id)));
                }

                return true;
            }

            sender.sendMessage("Expeditions: ");

            synchronized (expeditionInformationBiMap) {
                for (final var entry : expeditionInformationBiMap.entrySet()) {
                    sender.sendMessage("id: " + entry.getKey());
                    sender.sendMessage(entry.getValue().toString());
                    sender.sendMessage();
                }
            }

            return true;
        });
    }

    public void loadRoomsAndExpeditionsDefaultLogger() {
        loadRoomsAndExpeditions(null);
    }

    public void loadRoomsAndExpeditions(@Nullable CommandSender logger) {
        if (logger == null)
            logger = getServer().getConsoleSender();

        loadRooms(logger);
        loadExpeditions(logger);
    }

    private void loadRooms(@NotNull CommandSender logger) {
        synchronized (roomInformationBiMap) {
            roomInformationBiMap.clear();
        }

        // Create gson instance to load the rooms
        final var gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter(getDataFolder().toPath().resolve("data/schematics/rooms")))
                .registerTypeAdapter(RoomType.class, new RoomTypeTypeAdapter())
                .registerTypeHierarchyAdapter(RoomDoor.class, new RoomDoorTypeAdapter())
                .create();

        final Path roomsFolder = getDataFolder().toPath().resolve("data/rooms").normalize();

        try (final var walk = Files.walk(roomsFolder)) {
            walk
                    .filter(Files::isRegularFile)
                    .filter(path -> com.google.common.io.Files.getFileExtension(path.getFileName().toString()).equals("json"))
                    .forEach(path -> {
                        // grab relative path for the room's id
                        final Path relativePath = roomsFolder.relativize(path).normalize();
                        final String id = com.google.common.io.Files.getNameWithoutExtension(relativePath.toString());

                        // load the room's information
                        try (final var reader = new BufferedReader(new FileReader(path.toFile()))) {
                            final RoomInformation roomInfo = gson.fromJson(reader, RoomInformation.class);

                            synchronized (roomInformationBiMap) {
                                roomInformationBiMap.put(id, roomInfo);
                            }

                            Debug.success(logger, "loaded room '" + id + "'");
                        } catch (IOException e) {
                            Debug.alwaysError(logger, "failed to load room '" + id + "'\n(IOException: " + e.getMessage() + ")");
                        } catch (JsonParseException e) {
                            Debug.alwaysError(logger, "failed to parse room '" + id + "'\n(" + e.getMessage() + ")");
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadExpeditions(@NotNull CommandSender logger) {
        synchronized (expeditionInformationBiMap) {
            expeditionInformationBiMap.clear();
        }

        // Create gson instance to load the rooms
        final var gson = new GsonBuilder()
                // .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter(getDataFolder().toPath().resolve("data/schematics/rooms")))
                .registerTypeAdapter(ExpeditionType.class, new ExpeditionTypeTypeAdapter())
                .registerTypeHierarchyAdapter(RoomLoader.class, new RoomLoaderTypeAdapter(roomInformationBiMap, getDataFolder().toPath().resolve("data/")))
                .create();

        final Path expeditionsFolder = getDataFolder().toPath().resolve("data/expeditions").normalize();

        try (final var walk = Files.walk(expeditionsFolder)) {
            walk
                    .filter(Files::isRegularFile)
                    .filter(path -> com.google.common.io.Files.getFileExtension(path.getFileName().toString()).equals("json"))
                    .forEach(path -> {
                        // grab relative path for the room's id
                        final Path relativePath = expeditionsFolder.relativize(path).normalize();
                        final String id = com.google.common.io.Files.getNameWithoutExtension(relativePath.toString());

                        // load the room's information
                        try (final var reader = new BufferedReader(new FileReader(path.toFile()))) {
                            final ExpeditionInformation expInfo = gson.fromJson(reader, ExpeditionInformation.class);

                            synchronized (expeditionInformationBiMap) {
                                expeditionInformationBiMap.put(id, expInfo);
                            }

                            Debug.success(logger, "loaded expedition '" + id + "'");
                        } catch (IOException e) {
                            Debug.alwaysError(logger, "failed to load expedition '" + id + "'\n(IOException: " + e.getMessage() + ")");
                        } catch (JsonParseException e) {
                            Debug.alwaysError(logger, "failed to parse expedition '" + id + "'\n(" + e.getMessage() + ")");
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<RoomDoor> fourDoorsSameHeight(int heightOffset) {
        return List.of(
                new ConsistentHeightRoomDoor(BlockFace.NORTH, Material.DIRT, heightOffset),
                new ConsistentHeightRoomDoor(BlockFace.SOUTH, Material.DIRT, heightOffset),
                new ConsistentHeightRoomDoor(BlockFace.EAST, Material.DIRT, heightOffset),
                new ConsistentHeightRoomDoor(BlockFace.WEST, Material.DIRT, heightOffset)
        );
    }

    private void saveAllStartingWith(@Language("RegExp") String match) throws IOException {
        final var jarPath = PluggyTesty.class.getProtectionDomain().getCodeSource().getLocation();

        try (final var jar = new JarFile(new File(jarPath.getPath()))) {
            final var e = jar.entries();

            while (e.hasMoreElements()) {
                final JarEntry entry = e.nextElement();
                final var entryName = entry.getName();

                if (entry.isDirectory())
                    continue;

                if (!entryName.startsWith(match))
                    continue;

                Debug.info("entryName: " + entry.getName());

                final Path filePath = getDataFolder().toPath().resolve(entryName);

                if (Files.exists(filePath))
                    continue;

                Files.createDirectories(filePath.getParent());

                // save the resource
                try (OutputStream stream =
                             new BufferedOutputStream(
                                     new FileOutputStream(filePath.toFile()));
                     final InputStream resource = getResource(entryName)) {
                    byte[] buf = new byte[8192];
                    int length;
                    while ((length = resource.read(buf)) != -1)
                        stream.write(buf, 0, length);
                }
            }
        }
    }

    public SimpleArmorItemType RegisterAttributes(String id, String name, Material material, int amount, int amount2, EquipmentSlot slot){

        return new SimpleArmorItemType(this, "pure_"+ id, false, "Pure " + name, material,
                new SimpleArmorItemType.AttributePair(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "irrelevant", amount, AttributeModifier.Operation.ADD_NUMBER, slot)),
                new SimpleArmorItemType.AttributePair(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "irrelevant", amount2, AttributeModifier.Operation.ADD_NUMBER, slot)));
    }
    private void registerItems() {

        // Testing
        resourceManager.registerItem(new TestGUIItemType(this, "gui_tester", false, "GUI Tester"));

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
        resourceManager.registerItem(new KingPunchItemType(this, "king_punch", false, ChatColor.RED + "King Punch"));
        resourceManager.registerItem(new FisherOfSoulsItemType(this, "soul_fisher", false, ChatColor.DARK_PURPLE + "Fisher Of Souls"));
        resourceManager.registerItem(new MagicGripperItemType(this, "magic_gripper", false, ChatColor.LIGHT_PURPLE + "Magic Gripper"));
        resourceManager.registerItem(new BoombatStickItemType(this, "boombat_stick", false, ChatColor.DARK_GRAY + "Boombat Stick"));
        resourceManager.registerItem(new FlyingDaggerItemType(this, "flying_dagger", false, ChatColor.YELLOW + "Flying Dagger"));

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
        resourceManager.registerItem(new GoItemType(this, "go", false, ChatColor.WHITE + "Go!"));
        resourceManager.registerItem(new HealingHeartItemType(this, "healing_heart", false, ChatColor.RED + "Healing Heart"));
        resourceManager.registerItem(new DashItemType(this, "dash", false, ChatColor.WHITE + "Dash"));
        resourceManager.registerItem(new DiggaItemType(this, "digga", false, "Digga"));
        resourceManager.registerItem(new SwiftStaffItemType(this, "swift_staff", false, ChatColor.BLUE + "Swift Staff"));
        resourceManager.registerItem(new HealingAuraItemType(this, "healing_aura", false, ChatColor.RED + "Healing Aura", partyManager));
        resourceManager.registerItem(new HandThrusterItemType(this, "hand_thruster", false, ChatColor.GOLD + "Hand Thruster"));
        resourceManager.registerItem(new LandMineItemType(this, "land_mine", false, ChatColor.WHITE + "Land Mine"));
        resourceManager.registerItem(new LifeLinkItemType(this, "life_link", false, ChatColor.RED + "Life Link"));
        resourceManager.registerItem(new ArrowBeltItemType(this, "arrow_belt", false, ChatColor.WHITE + "Arrow Belt", nbtKeyManager, resourceManager));
        resourceManager.registerItem(new ExpeditionEnterItemType(this, "expedition_enter", false, ChatColor.WHITE + "Enter Expedition",
                expeditionBuilder, expeditionController, expeditionInformationBiMap, gooberStateController));
        resourceManager.registerItem(new BiomeCumPissItemType(this, "biome_compass", false, ChatColor.DARK_AQUA + "Biome Compass"));

        // Armor
        resourceManager.registerItem(new FeatherBootsItemType(this, "feather_boots", false, ChatColor.WHITE + "Feather Boots"));
        resourceManager.registerItem(new SimpleItemType(this, "buffed_leather_helmet", true, ChatColor.AQUA + "Buffed" + ChatColor.WHITE + "Leather Hat", Material.LEATHER_HELMET));
        // pure armor
        resourceManager.registerItem(RegisterAttributes("leather_helmet", "Leather Helmet", Material.LEATHER_HELMET, 1, 1, EquipmentSlot.HEAD));
        // TNT
        resourceManager.registerItem(new StickyTntItemType(this, "sticky_tnt", true, ChatColor.GREEN + "Sticky TNT"));

    }


    private void addCustomAttributes() {
        // modify a bunch of vanilla items

        // Swords
        addCustomAttributeToVanillaItem(Material.WOODEN_SWORD,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 5,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.STONE_SWORD,
                new AttributePair(Attribute.GENERIC_ARMOR, 6,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.IRON_SWORD,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 7,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.GOLDEN_SWORD,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 10,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.DIAMOND_SWORD,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 14,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.NETHERITE_SWORD,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 20,
                        EquipmentSlot.HAND));

        // Tools
        addCustomAttributeToVanillaItem(Material.WOODEN_AXE,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 1,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.STONE_AXE,
                new AttributePair(Attribute.GENERIC_ARMOR, 1,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.IRON_AXE,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 2,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.GOLDEN_AXE,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 3,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.DIAMOND_AXE,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 4,
                        EquipmentSlot.HAND));
        addCustomAttributeToVanillaItem(Material.NETHERITE_AXE,
                new AttributePair(Attribute.GENERIC_ATTACK_DAMAGE, 5,
                        EquipmentSlot.HAND));


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
//        addCustomAttributeToVanillaItem(Material.IRON_CHESTPLATE,
//                new AttributePair(Attribute.GENERIC_ARMOR, 3,
//                        EquipmentSlot.CHEST));
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

        removeOnDisable.forEach((entity) -> {
            removeOnDisable.remove(entity);
            entity.remove();
            Debug.log("Removed entity: " + entity);
        });

        getLogger().info("no more");
    }

//    public SmithingTransformRecipe smithingRecipes(RecipeChoice.ExactChoice purifier, RecipeChoice.MaterialChoice pureLeather){
//
//    }

    private void registerRecipes() {
        final ShapedRecipe chains = new ShapedRecipe(new NamespacedKey(this, "chain"), new ItemStack(Material.CHAIN))
                .shape( "i",
                        "i",
                        "i")
                .setIngredient('i', Material.IRON_NUGGET);
        getServer().addRecipe(chains);
        recipeManager.registerUnlockableRecipe(new NamespacedKey(this, "chain"), Material.IRON_NUGGET);


        final ShapedRecipe chainHelm = new ShapedRecipe(NamespacedKey.minecraft("chainmail_helmet"), PureArmor.cH())
                .shape(
                        "ccc",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainHelm);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_helmet"), Material.CHAIN);


        final ShapedRecipe chainBoots = new ShapedRecipe(NamespacedKey.minecraft("chainmail_boots"), PureArmor.cB())
                .shape(
                        "c c",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainBoots);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_boots"), Material.CHAIN);


        final ShapedRecipe chainChestplate = new ShapedRecipe(NamespacedKey.minecraft("chainmail_chestplate"), PureArmor.cC())
                .shape(
                        "c c",
                        "ccc",
                        "ccc")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainChestplate);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_chestplate"), Material.CHAIN);


        final ShapedRecipe chainLeggings = new ShapedRecipe(NamespacedKey.minecraft("chainmail_leggings"), PureArmor.cL())
                .shape(
                        "ccc",
                        "c c",
                        "c c")
                .setIngredient('c', Material.CHAIN);
        getServer().addRecipe(chainLeggings);
        recipeManager.registerUnlockableRecipe(NamespacedKey.minecraft("chainmail_leggings"), Material.CHAIN);


        //pure armor
        var result = resourceManager.getCustomItemStack(new NamespacedKey(this, "pure_leather_helmet"));


        RecipeChoice.ExactChoice purifier = new RecipeChoice.ExactChoice(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE));


        RecipeChoice.MaterialChoice pureLeather = new RecipeChoice.MaterialChoice(Material.LEATHER);
        RecipeChoice.MaterialChoice leatherHelmet = new RecipeChoice.MaterialChoice(Material.SADDLE);
        SmithingTransformRecipe pureLeatherHelmet = new SmithingTransformRecipe(new NamespacedKey(this, "pure_leather_helmet"), result, purifier, leatherHelmet, pureLeather);
        Bukkit.addRecipe(pureLeatherHelmet);

    }


}
