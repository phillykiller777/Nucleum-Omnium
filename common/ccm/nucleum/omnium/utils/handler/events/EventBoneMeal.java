/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.omnium.utils.handler.events;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;

import ccm.nucleum.omnium.block.sub.SubBlock;
import ccm.nucleum.omnium.block.sub.SubCrop;
import ccm.nucleum.omnium.utils.helper.BlockHelper;
import ccm.nucleum.omnium.utils.helper.MathHelper;

public final class EventBoneMeal
{
    @ForgeSubscribe
    public void fertilize(final BonemealEvent event)
    {
        final SubBlock tmp = BlockHelper.getSubBlock(event.world, event.X, event.Y, event.Z);
        if (tmp instanceof SubCrop)
        {
            final SubCrop block = (SubCrop) tmp;

            int growth = block.getCurrentStage() + MathHelper.clampInt(2, block.getTotalStages(), event.world.rand.nextInt(block.getTotalStages()));

            if (growth > block.getTotalStages())
            {
                growth = block.getTotalStages();
            }

            block.growToStage(growth);
        }
    }
}