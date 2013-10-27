/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.world.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent.Load;
import net.minecraftforge.event.world.ChunkDataEvent.Save;

import cpw.mods.fml.common.IWorldGenerator;

import ccm.nucleum.world.NucleumWorld;
import ccm.nucleum.world.utils.TickHandlerWorld;
import ccm.nucleum.world.utils.lib.Properties;

import lib.cofh.api.world.IFeatureGenerator;
import lib.cofh.api.world.IFeatureHandler;
import lib.cofh.util.ChunkCoord;

public class WorldGenHandler implements IWorldGenerator, IFeatureHandler
{
    public static boolean addOre(final IFeatureGenerator Ore)
    {
        return WorldGenHandler.instance.registerFeature(Ore);
    }

    private final List<IFeatureGenerator> ores = new ArrayList<IFeatureGenerator>();

    private final HashSet<String> oreNames = new HashSet<String>();

    private final HashSet<Integer> dimensionBlacklist = new HashSet<Integer>();

    public static WorldGenHandler instance = new WorldGenHandler();

    private void replaceBR(final int chunkX, final int chunkZ, final World world, final Block block)
    {
        for (int blockX = 0; blockX < 16; blockX++)
        {
            for (int blockZ = 0; blockZ < 16; blockZ++)
            {
                for (int blockY = 5; blockY > 0; blockY--)
                {
                    if (world.getBlockId((chunkX * 16) + blockX, blockY, (chunkZ * 16) + blockZ) == Block.bedrock.blockID)
                    {
                        world.setBlock((chunkX * 16) + blockX, blockY, (chunkZ * 16) + blockZ, block.blockID, 0, 2);
                    }
                }
            }
        }
    }

    @Override
    public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider)
    {
        generateWorld(random, chunkX, chunkZ, world, true);
    }

    public void generateWorld(final Random random, final int chunkX, final int chunkZ, final World world, final boolean newGen)
    {
        replaceBedrock(random, chunkX, chunkZ, world, newGen);

        if (!newGen && !Properties.retroOreGen)
        {
            return;
        }
        for (final IFeatureGenerator feature : ores)
        {
            feature.generateFeature(random, chunkX, chunkZ, world, newGen);
        }
        if (!newGen)
        {
            world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
        }
    }

    @ForgeSubscribe
    @SuppressWarnings(
    { "unchecked", "rawtypes" })
    public void handleChunkLoadEvent(final Load event)
    {
        final int dim = event.world.provider.dimensionId;

        if (dimensionBlacklist.contains(Integer.valueOf(dim)))
        {
            return;
        }
        boolean bedrock = false;
        boolean ores = false;
        boolean regen = false;
        final NBTTagCompound tag = (NBTTagCompound) event.getData().getTag("CCM-Properties");

        if (tag != null)
        {
            bedrock = !tag.hasKey("CCM-Bedrock") && Properties.retroFlatBedrock && Properties.genFlatBedrock;
            ores = !tag.hasKey("CCM-Ores") && Properties.retroOreGen;
        }
        final ChunkCoord cCoord = new ChunkCoord(event.getChunk());

        if ((tag == null) && ((Properties.retroFlatBedrock && Properties.genFlatBedrock) || Properties.retroOreGen))
        {
            regen = true;
        }
        if (bedrock)
        {
            NucleumWorld.instance.getLogger().finest("Regenerating flat bedrock for the chunk at %s", cCoord.toString());
            regen = true;
        }
        if (ores)
        {
            NucleumWorld.instance.getLogger().finest("Regenerating ores for the chunk at %s", cCoord.toString());
            regen = true;
        }
        if (regen)
        {
            ArrayList chunks = TickHandlerWorld.chunksToGen.get(Integer.valueOf(dim));

            if (chunks == null)
            {
                TickHandlerWorld.chunksToGen.put(Integer.valueOf(dim), new ArrayList<ChunkCoord>());
                chunks = TickHandlerWorld.chunksToGen.get(Integer.valueOf(dim));
            }
            if (chunks != null)
            {
                chunks.add(cCoord);
                TickHandlerWorld.chunksToGen.put(Integer.valueOf(dim), chunks);
            }
        }
    }

    @ForgeSubscribe
    public void handleChunkSaveEvent(final Save event)
    {
        final NBTTagCompound tag = new NBTTagCompound();

        if (Properties.retroFlatBedrock && Properties.genFlatBedrock)
        {
            tag.setBoolean("CCM-Bedrock", true);
        }
        if (Properties.retroOreGen)
        {
            tag.setBoolean("CCM-Ores", true);
        }
        event.getData().setTag("CCM-Properties", tag);
    }

    @Override
    public boolean registerFeature(final IFeatureGenerator feature)
    {
        if (oreNames.contains(feature.getFeatureName()))
        {
            return false;
        }
        oreNames.add(feature.getFeatureName());
        ores.add(feature);
        return true;
    }

    public void replaceBedrock(final Random random, final int chunkX, final int chunkZ, final World world, final boolean newGen)
    {
        if (!Properties.genFlatBedrock || (!newGen && !Properties.retroFlatBedrock))
        {
            return;
        }
        final boolean isNether = world.provider.isHellWorld;

        if (isNether)
        {
            replaceBR(chunkX, chunkZ, world, Block.netherrack);

            for (int blockX = 0; blockX < 16; blockX++)
            {
                for (int blockZ = 0; blockZ < 16; blockZ++)
                {
                    for (int blockY = 126; blockY > 121; blockY--)
                    {
                        if (world.getBlockId((chunkX * 16) + blockX, blockY, (chunkZ * 16) + blockZ) == Block.bedrock.blockID)
                        {
                            world.setBlock((chunkX * 16) + blockX, blockY, (chunkZ * 16) + blockZ, Block.netherrack.blockID, 0, 2);
                        }
                    }
                }
            }
        } else
        {
            replaceBR(chunkX, chunkZ, world, Block.stone);
        }
    }
}