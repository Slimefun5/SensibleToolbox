package io.github.thebusybiscuit.sensibletoolbox.slimefun;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBMachine;
import io.github.thebusybiscuit.sensibletoolbox.api.recipes.STBFurnaceRecipe;
import io.github.thebusybiscuit.sensibletoolbox.api.recipes.SimpleCustomRecipe;
import io.github.thebusybiscuit.sensibletoolbox.blocks.AngelicBlock;
import io.github.thebusybiscuit.sensibletoolbox.blocks.machines.BasicSolarCell;
import io.github.thebusybiscuit.sensibletoolbox.blocks.machines.BatteryBox;
import io.github.thebusybiscuit.sensibletoolbox.blocks.machines.BigStorageUnit;
import io.github.thebusybiscuit.sensibletoolbox.blocks.machines.Generator;
import io.github.thebusybiscuit.sensibletoolbox.blocks.router.ItemRouter;
import io.github.thebusybiscuit.sensibletoolbox.items.CombineHoe;
import io.github.thebusybiscuit.sensibletoolbox.items.MoistureChecker;
import io.github.thebusybiscuit.sensibletoolbox.items.PVCell;
import io.github.thebusybiscuit.sensibletoolbox.items.PaintBrush;
import io.github.thebusybiscuit.sensibletoolbox.items.energycells.EnergyCell;
import io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules.ItemRouterModule;
import io.github.thebusybiscuit.sensibletoolbox.items.recipebook.RecipeBook;
import io.github.thebusybiscuit.sensibletoolbox.items.upgrades.AbstractMachineUpgrade;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.recipes.MinecraftRecipe;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class SlimefunBridge implements SlimefunAddon {

    // Handheld tools & gadgets that don't share a common STB base class.
    private static final java.util.Set<String> EXTRA_TOOLS = new java.util.HashSet<>(java.util.Arrays.asList(
            "EnderLeash", "EnderBag", "EnderTuner", "WateringCan", "TapeMeasure",
            "LandMarker", "Multimeter", "MultiBuilder", "PaintCan", "Elevator", "PowerMonitor"));

    // Powered / utility blocks that don't extend BaseSTBMachine.
    private static final java.util.Set<String> EXTRA_MACHINES = new java.util.HashSet<>(java.util.Arrays.asList(
            "RedstoneClock", "BlockUpdateDetector", "TrashCan", "SoundMuffler", "HolographicMonitor"));

    private final SensibleToolboxPlugin plugin;

    public SlimefunBridge(@Nonnull SensibleToolboxPlugin plugin) {
        this.plugin = plugin;

        ItemGroup items = new ItemGroup(new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey("sensibletoolbox", "items"), CustomItemStack.create(MaterialCompat.safe(XMaterial.SHEARS), "&7STB - Items")).setTheme("tools");
        ItemGroup blocks = new ItemGroup(new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey("sensibletoolbox", "blocks"), CustomItemStack.create(MaterialCompat.safe(XMaterial.PURPLE_STAINED_GLASS), "&7STB - Blocks and Machines")).setTheme("machines");

        for (String id : SensibleToolboxPlugin.getInstance().getItemRegistry().getItemIds()) {
            BaseSTBItem item = SensibleToolboxPlugin.getInstance().getItemRegistry().getItemById(id);
            ItemGroup category = item.toItemStack().getType().isBlock() ? blocks : items;
            List<ItemStack> recipe = new ArrayList<>();
            RecipeType recipeType = RecipeType.NULL;
            Recipe r = item.getMainRecipe();

            if (r != null) {
                if (r instanceof SimpleCustomRecipe) {
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(((SimpleCustomRecipe) r).getIngredient());
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                } else if (r instanceof STBFurnaceRecipe) {
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(((STBFurnaceRecipe) r).getIngredient());
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                    recipe.add(null);
                } else if (item.getMainRecipe() instanceof ShapelessRecipe) {
                    recipeType = new RecipeType(MinecraftRecipe.SHAPELESS_CRAFTING);

                    for (ItemStack input : ((ShapelessRecipe) item.getMainRecipe()).getIngredientList()) {
                        if (input == null) {
                            recipe.add(null);
                        } else {
                            recipe.add(RecipeBook.getIngredient(item, input));
                        }
                    }

                    for (int i = recipe.size(); i < 9; i++) {
                        recipe.add(null);
                    }
                } else if (item.getMainRecipe() instanceof ShapedRecipe) {
                    recipeType = new RecipeType(MinecraftRecipe.SHAPED_CRAFTING);

                    for (String row : ((ShapedRecipe) item.getMainRecipe()).getShape()) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                recipe.add(RecipeBook.getIngredient(item, ((ShapedRecipe) item.getMainRecipe()).getIngredientMap().get(Character.valueOf(row.charAt(i)))));
                            } catch (StringIndexOutOfBoundsException x) {
                                recipe.add(null);
                            }
                        }
                    }

                    for (int i = recipe.size(); i < 9; i++) {
                        recipe.add(null);
                    }
                }
            }

            SlimefunItem sfItem = null;
            SlimefunItemStack itemStack = new SlimefunItemStack("STB_" + id.toUpperCase(Locale.ROOT), item.toItemStack());

            if (item instanceof Generator) {
                List<ItemStack> fuels = ((Generator) item).getFuelInformation();
                sfItem = new STBSlimefunGenerator(category, itemStack, recipeType, recipe.toArray(new ItemStack[0]), fuels);
            } else {
                sfItem = new STBSlimefunItem(category, itemStack, recipeType, recipe.toArray(new ItemStack[0]));
            }

            if (r != null) {
                sfItem.setRecipeOutput(r.getResult());
            }

            String guideType = guideTypeFor(item);
            if (guideType != null) {
                sfItem.setGuideType(guideType);
            }

            sfItem.register(this);
        }

        RecipeType masher = new RecipeType(new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey("sensibletoolbox", "masher"), SlimefunItem.getById("STB_MASHER").getItem());
        RecipeType fermenter = new RecipeType(new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey("sensibletoolbox", "fermenter"), SlimefunItem.getById("STB_FERMENTER").getItem());
        RecipeType mobDrop = new RecipeType(new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey("sensibletoolbox", "mob_drop"), CustomItemStack.create(MaterialCompat.safe(XMaterial.IRON_SWORD), "&bMob Drop", "&7Kill that Mob to", "&7obtain this Item"));

        patch("STB_INFERNALDUST", mobDrop, CustomItemStack.create(MaterialCompat.safe(XMaterial.BLAZE_SPAWN_EGG), "&a&oBlaze"));
        patch("STB_ENERGIZEDGOLDINGOT", new RecipeType(MinecraftRecipe.FURNACE), SlimefunItem.getById("STB_ENERGIZEDGOLDDUST").getItem());
        patch("STB_QUARTZDUST", masher, new ItemStack(MaterialCompat.safe(XMaterial.QUARTZ)));
        patch("STB_ENERGIZEDIRONINGOT", new RecipeType(MinecraftRecipe.FURNACE), SlimefunItem.getById("STB_ENERGIZEDIRONDUST").getItem());
        patch("STB_SILICONWAFER", new RecipeType(MinecraftRecipe.FURNACE), SlimefunItem.getById("STB_QUARTZDUST").getItem());
        patch("STB_IRONDUST", masher, new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)));
        patch("STB_GOLDDUST", masher, new ItemStack(MaterialCompat.safe(XMaterial.GOLD_INGOT)));
        patch("STB_FISHBAIT", fermenter, new ItemStack(MaterialCompat.safe(XMaterial.ROTTEN_FLESH)));

        Slimefun.getItemTranslationService().registerTranslations(plugin);
        registerWiki(this);
    }

    /**
     * Maps an STB item to a guide category id. STB items are wrapped in plain
     * {@link SlimefunItem}s and use head textures, so the guide heuristic can't
     * type them - they'd all fall into "Misc" without this. Returns {@code null}
     * to leave typing to the core heuristic.
     */
    @javax.annotation.Nullable
    private String guideTypeFor(BaseSTBItem item) {
        String simpleName = item.getClass().getSimpleName();

        if (item instanceof Generator || item instanceof BasicSolarCell || item instanceof PVCell
                || item instanceof EnergyCell || item instanceof BatteryBox) {
            return "energy_tech";
        }
        if (item instanceof ItemRouterModule || item instanceof ItemRouter
                || item instanceof BigStorageUnit || "EnderBox".equals(simpleName)) {
            return "logistics";
        }
        if (item instanceof AbstractMachineUpgrade
                || item.getClass().getName().contains(".items.components.")) {
            return "resources";
        }
        if (item instanceof CombineHoe || item instanceof PaintBrush || item instanceof MoistureChecker
                || item instanceof RecipeBook || EXTRA_TOOLS.contains(simpleName)) {
            return "tools";
        }
        if (item instanceof AngelicBlock) {
            return "decoration";
        }
        if (item instanceof BaseSTBMachine || EXTRA_MACHINES.contains(simpleName)) {
            return "machines";
        }
        return null;
    }

    private void registerWiki(SlimefunAddon addon) {
        io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText wiki = Slimefun.getWikiText();

        // Bucket this addon's items by their ItemGroup dynamically - never hardcode item lists.
        java.util.Map<ItemGroup, java.util.List<String>> byGroup = new java.util.LinkedHashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item.getAddon() != addon) {
                    continue;
                }
                ItemGroup group = item.getItemGroup();
                if (group == null) {
                    continue;
                }
                byGroup.computeIfAbsent(group, g -> new java.util.ArrayList<>()).add(item.getId());

                java.util.List<String> text = itemText(item.getId());
                if (text != null) {
                    wiki.set(item.getId(), text);
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break wiki registration.
            }
        }

        for (java.util.Map.Entry<ItemGroup, java.util.List<String>> entry : byGroup.entrySet()) {
            try {
                String groupKey = entry.getKey().getKey().getKey();
                String topicId = "addon_sensibletoolbox_" + groupKey;

                wiki.registerTopic(new io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiTopic(topicId,
                        categoryTitle(groupKey), categoryIcon(groupKey), categoryTagline(groupKey)));
                wiki.setMechanic(topicId, categoryBlurb(groupKey));
                wiki.setTopicItems(topicId, entry.getValue());
            } catch (Exception | LinkageError ignored) {
                // A broken group should not break wiki registration.
            }
        }
    }

    @Nonnull
    private String categoryTitle(String groupKey) {
        switch (groupKey) {
            case "blocks":
                return "STB - Blocks & Machines";
            case "items":
                return "STB - Tools & Gadgets";
            default:
                return "Sensible Toolbox";
        }
    }

    @Nonnull
    private XMaterial categoryIcon(String groupKey) {
        switch (groupKey) {
            case "blocks":
                return XMaterial.PURPLE_STAINED_GLASS;
            case "items":
                return XMaterial.SHEARS;
            default:
                return XMaterial.IRON_PICKAXE;
        }
    }

    @Nonnull
    private String categoryTagline(String groupKey) {
        switch (groupKey) {
            case "blocks":
                return "&7Machines, engines & storage";
            case "items":
                return "&7Tools, modules & upgrades";
            default:
                return "&7Ported STB content";
        }
    }

    @Nonnull
    private java.util.List<String> categoryBlurb(String groupKey) {
        switch (groupKey) {
            case "blocks":
                return java.util.Arrays.asList(
                    "&6Blocks & Machines", "",
                    "&7Sensible Toolbox's placeable machinery,",
                    "&7brought into Slimefun. Powered devices",
                    "&7run on STB energy: connect generators",
                    "&7such as the &fHeat Engine&7 or &fSolar Cells",
                    "&7to machines like the &fMasher&7, &fSmelter",
                    "&7and &fSawmill&7 to process resources.", "",
                    "&7Use &fStorage Units&7 to stockpile a single",
                    "&7item type far beyond a chest, and &fItem",
                    "&7Routers&7 with modules to automate logistics.", "",
                    "&7Click an item below for its recipe & details.");
            case "items":
                return java.util.Arrays.asList(
                    "&6Tools & Gadgets", "",
                    "&7Sensible Toolbox's handheld tools, crafting",
                    "&7components and router modules.", "",
                    "&7Includes multi-tools like the &fMulti Builder",
                    "&7and &fCombine Hoes&7, ender-tech gadgets such",
                    "&7as the &fEnder Bag&7 and &fElevator&7, and the",
                    "&7modules & upgrades that drive Item Routers.", "",
                    "&7Dusts and refined ingots produced by STB",
                    "&7machines also live here.", "",
                    "&7Click an item below for its recipe & details.");
            default:
                return java.util.Arrays.asList(
                    "&7Ported STB machines & gadgets.", "",
                    "&7Click an item below for its recipe.");
        }
    }

    /**
     * Authored wiki page lines for individual STB items. Returns {@code null}
     * for items that do not warrant a bespoke description.
     */
    @SuppressWarnings("PMD.NcssCount")
    private java.util.List<String> itemText(String id) {
        switch (id) {
            case "STB_MASHER":
                return java.util.Arrays.asList(
                    "&7Grinds ingots and ores into dusts,",
                    "&7letting you double your metal yield",
                    "&7when the dust is smelted back down.",
                    "&7Requires STB energy to run.");
            case "STB_SMELTER":
                return java.util.Arrays.asList(
                    "&7An electric furnace that smelts items",
                    "&7far faster than vanilla, running on",
                    "&7STB energy instead of burning fuel.");
            case "STB_SAWMILL":
                return java.util.Arrays.asList(
                    "&7Cuts logs into a higher plank yield",
                    "&7than the crafting grid, and produces",
                    "&7sawdust as a useful by-product.");
            case "STB_FERMENTER":
                return java.util.Arrays.asList(
                    "&7Ferments organic matter into new",
                    "&7products such as fish bait.",
                    "&7Runs on STB energy.");
            case "STB_BIOENGINE":
                return java.util.Arrays.asList(
                    "&7A generator that burns organic fuels",
                    "&7and biofuel to produce STB energy.",
                    "&7Greener than a coal-fired engine.");
            case "STB_HEATENGINE":
                return java.util.Arrays.asList(
                    "&7Burns solid fuels to generate STB",
                    "&7energy for your machine network.",
                    "&7A reliable starter power source.");
            case "STB_MAGMATICENGINE":
                return java.util.Arrays.asList(
                    "&7A powerful generator fuelled by lava,",
                    "&7producing a steady stream of STB",
                    "&7energy for demanding setups.");
            case "STB_BASICSOLARCELL":
            case "STB_PVCELL":
                return java.util.Arrays.asList(
                    "&7Generates STB energy from sunlight.",
                    "&7Place it with a clear view of the sky;",
                    "&7output stops at night and in the rain.");
            case "STB_DENSESOLAR":
                return java.util.Arrays.asList(
                    "&7A high-output solar array that gathers",
                    "&7far more energy than a basic cell.",
                    "&7Needs unobstructed daylight.");
            case "STB_ELECTRICALENERGIZER":
                return java.util.Arrays.asList(
                    "&7Energizes dusts and items into their",
                    "&7charged variants using STB energy.");
            case "STB_TRASHCAN":
                return java.util.Arrays.asList(
                    "&7Permanently voids any items inserted",
                    "&7into it. Handy on sorter and router",
                    "&7networks to discard unwanted output.");

            case "STB_BIGSTORAGEUNIT":
            case "STB_HYPERSTORAGEUNIT":
                return java.util.Arrays.asList(
                    "&7A mass storage block that holds a vast",
                    "&7quantity of a single item type, shown",
                    "&7on its face. Far beyond a chest.");
            case "STB_TENKBATTERYBOX":
            case "STB_FIFTYKBATTERYBOX":
                return java.util.Arrays.asList(
                    "&7Stores STB energy, buffering power",
                    "&7between your generators and machines",
                    "&7so output keeps flowing at night.");
            case "STB_TENKENERGYCELL":
            case "STB_FIFTYKENERGYCELL":
                return java.util.Arrays.asList(
                    "&7A portable energy cell that carries",
                    "&7stored STB power. Charge it up and",
                    "&7use it to power devices on the go.");

            case "STB_ITEMROUTER":
                return java.util.Arrays.asList(
                    "&7The heart of STB automation. Fit it",
                    "&7with modules to pull, push, sort and",
                    "&7move items between connected blocks.");
            case "STB_SENDERMODULE":
            case "STB_ADVANCEDSENDERMODULE":
            case "STB_HYPERSENDERMODULE":
                return java.util.Arrays.asList(
                    "&7Slots into an Item Router to push",
                    "&7items out to a target inventory.",
                    "&7Higher tiers reach further & faster.");
            case "STB_RECEIVERMODULE":
                return java.util.Arrays.asList(
                    "&7Slots into an Item Router to pull",
                    "&7items in from an adjacent inventory.");
            case "STB_SORTERMODULE":
                return java.util.Arrays.asList(
                    "&7Routes items to different outputs based",
                    "&7on configurable filters - the basis of",
                    "&7an automated sorting system.");
            case "STB_PULLERMODULE":
                return java.util.Arrays.asList(
                    "&7Actively pulls items from the inventory",
                    "&7the router is attached to.");
            case "STB_DROPPERMODULE":
                return java.util.Arrays.asList(
                    "&7Drops routed items into the world",
                    "&7as physical entities.");
            case "STB_BREAKERMODULE":
            case "STB_SILKYBREAKERMODULE":
                return java.util.Arrays.asList(
                    "&7Lets a router break the block in front",
                    "&7of it and collect the drops. The silky",
                    "&7variant mines with Silk Touch.");
            case "STB_VACUUMMODULE":
                return java.util.Arrays.asList(
                    "&7Sucks up nearby dropped items and feeds",
                    "&7them into the router's inventory.");
            case "STB_STACKMODULE":
                return java.util.Arrays.asList(
                    "&7Increases how many items a router moves",
                    "&7in a single operation.");
            case "STB_BLANKMODULE":
                return java.util.Arrays.asList(
                    "&7An empty module blank, the base used to",
                    "&7craft the various router modules.");

            case "STB_SPEEDUPGRADE":
            case "STB_SPEEDMODULE":
                return java.util.Arrays.asList(
                    "&7Installed in a machine to make it run",
                    "&7faster, at the cost of higher energy use.");
            case "STB_REGULATORUPGRADE":
                return java.util.Arrays.asList(
                    "&7Lets a machine hold a charge buffer so",
                    "&7it can keep working through power dips.");
            case "STB_EJECTORUPGRADE":
                return java.util.Arrays.asList(
                    "&7Automatically ejects a machine's finished",
                    "&7output into an adjacent inventory.");
            case "STB_THOROUGHNESSUPGRADE":
                return java.util.Arrays.asList(
                    "&7Improves a machine's processing quality,",
                    "&7increasing useful yields.");

            case "STB_MULTIBUILDER":
                return java.util.Arrays.asList(
                    "&7A configurable building tool that places",
                    "&7or exchanges blocks over an area, with",
                    "&7several selectable build modes.");
            case "STB_AUTOBUILDER":
                return java.util.Arrays.asList(
                    "&7Automatically lays down blocks from its",
                    "&7inventory across a region you mark out.");
            case "STB_WOODCOMBINEHOE":
            case "STB_IRONCOMBINEHOE":
            case "STB_GOLDCOMBINEHOE":
            case "STB_DIAMONDCOMBINEHOE":
                return java.util.Arrays.asList(
                    "&7A multi-tool that tills, digs and harvests",
                    "&7in one. Higher material tiers are more",
                    "&7durable and work over a wider area.");
            case "STB_WATERINGCAN":
                return java.util.Arrays.asList(
                    "&7Waters farmland and speeds up nearby crop",
                    "&7growth. Refill it from a water source.");
            case "STB_PAINTBRUSH":
            case "STB_PAINTROLLER":
                return java.util.Arrays.asList(
                    "&7Applies paint from a Paint Can to recolour",
                    "&7compatible blocks. The roller covers a",
                    "&7larger area per use.");
            case "STB_PAINTCAN":
                return java.util.Arrays.asList(
                    "&7Holds a supply of coloured paint for use",
                    "&7with the Paint Brush and Paint Roller.");
            case "STB_ENDERBAG":
                return java.util.Arrays.asList(
                    "&7Provides portable access to your ender",
                    "&7chest contents from anywhere.");
            case "STB_ENDERLEASH":
                return java.util.Arrays.asList(
                    "&7Teleports leashed animals to you so you",
                    "&7can relocate livestock with ease.");
            case "STB_ELEVATOR":
                return java.util.Arrays.asList(
                    "&7Place elevator blocks in a vertical column",
                    "&7to teleport instantly between floors.");
            case "STB_TAPEMEASURE":
                return java.util.Arrays.asList(
                    "&7Measures the distance and volume between",
                    "&7two points you select in the world.");
            case "STB_MULTIMETER":
            case "STB_POWERMONITOR":
                return java.util.Arrays.asList(
                    "&7A diagnostic tool that reads out the STB",
                    "&7energy stored and flowing in a machine.");
            case "STB_SOUNDMUFFLER":
                return java.util.Arrays.asList(
                    "&7Silences the sounds of nearby blocks and",
                    "&7machines within its range.");
            case "STB_ANGELICBLOCK":
                return java.util.Arrays.asList(
                    "&7A temporary floating scaffold block, ideal",
                    "&7for building out into open space.");
            case "STB_REDSTONECLOCK":
                return java.util.Arrays.asList(
                    "&7Emits a configurable repeating redstone",
                    "&7pulse to drive timed contraptions.");

            case "STB_CIRCUITBOARD":
            case "STB_SIMPLECIRCUIT":
            case "STB_INTEGRATEDCIRCUIT":
                return java.util.Arrays.asList(
                    "&7A crafting component used to build STB",
                    "&7machines and electronics.");
            case "STB_MACHINEFRAME":
            case "STB_TOUGHMACHINEFRAME":
                return java.util.Arrays.asList(
                    "&7The structural chassis that most STB",
                    "&7machines are built around.");
            case "STB_SILICONWAFER":
                return java.util.Arrays.asList(
                    "&7A refined silicon wafer used in the",
                    "&7crafting of circuits and electronics.");
            case "STB_IRONDUST":
            case "STB_GOLDDUST":
            case "STB_QUARTZDUST":
                return java.util.Arrays.asList(
                    "&7A dust produced by the Masher. Smelt it",
                    "&7to recover the original material - the",
                    "&7basis of STB ore doubling.");
            case "STB_ENERGIZEDGOLDDUST":
            case "STB_ENERGIZEDIRONDUST":
                return java.util.Arrays.asList(
                    "&7A charged dust made in the Electrical",
                    "&7Energizer. Smelt it into an energized",
                    "&7ingot for advanced recipes.");
            case "STB_ENERGIZEDGOLDINGOT":
            case "STB_ENERGIZEDIRONINGOT":
                return java.util.Arrays.asList(
                    "&7An energized ingot smelted from charged",
                    "&7dust, used in high-tier STB crafting.");
            case "STB_INFERNALDUST":
                return java.util.Arrays.asList(
                    "&7A fiery dust dropped by Blazes, used as",
                    "&7a crafting ingredient in STB recipes.");
            case "STB_FISHBAIT":
                return java.util.Arrays.asList(
                    "&7Fermented bait that improves your odds",
                    "&7when used with STB fishing gear.");
            default:
                return null;
        }
    }

    @ParametersAreNonnullByDefault
    private void patch(String id, RecipeType recipeType, ItemStack recipe) {
        SlimefunItem item = SlimefunItem.getById(id);

        if (item != null) {
            item.setRecipe(new ItemStack[] { null, null, null, null, recipe, null, null, null, null });
            item.setRecipeType(recipeType);
        }
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return plugin;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Slimefun5/SensibleToolbox/issues";
    }

}


