/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.world;

import static ccm.nucleum.world.utils.lib.Archive.MOD_ID;
import static ccm.nucleum.world.utils.lib.Archive.MOD_NAME;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import ccm.nucleum.omnium.BaseMod;
import ccm.nucleum.omnium.IMod;
import ccm.nucleum.world.generator.WorldGenHandler;
import ccm.nucleum.world.utils.TickHandlerWorld;
import ccm.nucleum.world.utils.lib.Properties;

@Mod(modid = MOD_ID, name = MOD_NAME, useMetadata = true)
public class NucleumWorld extends BaseMod implements IMod
{

    @Instance(MOD_ID)
    public static NucleumWorld instance;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent evt)
    {
        initializeConfig(evt);

        GameRegistry.registerWorldGenerator(WorldGenHandler.instance);

        MinecraftForge.EVENT_BUS.register(WorldGenHandler.instance);

        MinecraftForge.ORE_GEN_BUS.register(WorldGenHandler.instance);
    }

    @EventHandler
    public void modsLoaded(final FMLPostInitializationEvent event)
    {
        if (Properties.retroOreGen)
        {
            TickRegistry.registerTickHandler(TickHandlerWorld.instance, Side.SERVER);
        }
    }
}