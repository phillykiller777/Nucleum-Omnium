/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.omnium.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lib.cofh.api.world.WeightedRandomBlock;
import lib.cofh.world.feature.FeatureOreGenUniform;
import lib.cofh.world.feature.WorldGenMinableCluster;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import ccm.nucleum.omnium.CCMMod;
import ccm.nucleum.omnium.utils.helper.JavaHelper;

/**
 * @author Captain_Shadows
 */
public final class WorldGenerator
{
    /**
     * Adds world generation to the End
     * 
     * @param stack
     *            The ItemStack of the Ore
     * @param oreName
     *            The name of the ore
     * @param clusterSize
     *            The size of the clusters
     * @param numClusters
     *            The number of clusters per chunk
     * @param minY
     *            The minimum Y level to find the ore
     * @param maxY
     *            The maximum Y level to find the ore
     * @param enable
     *            is the ore enabled?
     */
    public static void addEndGen(final CCMMod mod,
                                 final ItemStack stack,
                                 final String oreName,
                                 final int clusterSize,
                                 final int numClusters,
                                 final int minY,
                                 final int maxY,
                                 final boolean enable)
    {
        WorldGenerator.addWorldGen(mod, stack, oreName, clusterSize, numClusters, minY, maxY, enable, Block.whiteStone);
    }

    /**
     * Adds world generation to the Nether
     * 
     * @param stack
     *            The ItemStack of the Ore
     * @param oreName
     *            The name of the ore
     * @param clusterSize
     *            The size of the clusters
     * @param numClusters
     *            The number of clusters per chunk
     * @param minY
     *            The minimum Y level to find the ore
     * @param maxY
     *            The maximum Y level to find the ore
     * @param enable
     *            is the ore enabled?
     */
    public static void addNetherGen(final CCMMod mod,
                                    final ItemStack stack,
                                    final String oreName,
                                    final int clusterSize,
                                    final int numClusters,
                                    final int minY,
                                    final int maxY,
                                    final boolean enable)
    {
        WorldGenerator.addWorldGen(mod, stack, oreName, clusterSize, numClusters, minY, maxY, enable, Block.netherrack);
    }

    /**
     * Adds world generation to the Overworld
     * 
     * @param stack
     *            The ItemStack of the Ore
     * @param oreName
     *            The name of the ore
     * @param clusterSize
     *            The size of the clusters
     * @param numClusters
     *            The number of clusters per chunk
     * @param minY
     *            The minimum Y level to find the ore
     * @param maxY
     *            The maximum Y level to find the ore
     * @param enable
     *            is the ore enabled?
     */
    public static void addOverworldGen(final CCMMod mod,
                                       final ItemStack stack,
                                       final String oreName,
                                       final int clusterSize,
                                       final int numClusters,
                                       final int minY,
                                       final int maxY,
                                       final boolean enable)
    {
        WorldGenerator.addWorldGen(mod, stack, oreName, clusterSize, numClusters, minY, maxY, enable, Block.stone);
    }

    public static void addWorldGen(final CCMMod mod,
                                   final ItemStack stack,
                                   final String oreName,
                                   int clusterSize,
                                   int numClusters,
                                   int minY,
                                   int maxY,
                                   final boolean enable,
                                   final Block blockToReplace)
    {
        final String ore = mod.name() + "." + JavaHelper.titleCase(oreName);
        final Configuration config = new Configuration(new File(CCMMod.configDir.getAbsolutePath() + "/WorldGen.cfg"), true);
        final ConfigCategory cat = config.getCategory(ore);
        clusterSize = config.get(ore, "ClusterSize", clusterSize).getInt();
        numClusters = config.get(ore, "NumClusters", numClusters).getInt();
        minY = config.get(ore, "MinHeight", minY).getInt();
        maxY = config.get(ore, "MaxHeight", maxY).getInt();
        final boolean regen = config.get(ore, "RetroGen", enable).getBoolean(enable);
        cat.setComment("Configurations for " + JavaHelper.titleCase(oreName));
        config.save();
        if (!enable)
        {
            return;
        }
        final List<WeightedRandomBlock> resList = new ArrayList<WeightedRandomBlock>();
        resList.add(new WeightedRandomBlock(stack));
        WorldGenHandler.addOre(new FeatureOreGenUniform(oreName,
                                                        new WorldGenMinableCluster(resList, clusterSize, blockToReplace.blockID),
                                                        numClusters,
                                                        minY,
                                                        maxY,
                                                        regen));
    }
}