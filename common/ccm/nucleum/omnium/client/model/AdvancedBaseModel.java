/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.omnium.client.model;

import net.minecraftforge.client.model.IModelCustom;

import cpw.mods.fml.client.FMLClientHandler;

import ccm.nucleum.omnium.utils.handler.ResourceHandler;
import ccm.nucleum.omnium.utils.helper.CCMLogger;

/**
 * Advanced Base Model, to use with the Advanced Model Loader
 * 
 * @author Captain_Shadows
 */
public class AdvancedBaseModel
{

    protected IModelCustom model;
    protected String name;

    public AdvancedBaseModel(final Enum<?> resourceName)
    {
        this(resourceName.name());
    }

    public AdvancedBaseModel(final String resourceName)
    {
        name = resourceName;
        CCMLogger.DEFAULT_LOGGER.debug(name);
        CCMLogger.DEFAULT_LOGGER.debug(ResourceHandler.getModelLocation(name));
        model = ResourceHandler.loadModel(name);
    }

    public void bindTexture()
    {
        ResourceHandler.bindModel(FMLClientHandler.instance().getClient(), name);
    }

    /** When calling this method the texture doesn't need to be binded */
    public void render()
    {
        // Bind texture
        bindTexture();
        // Render
        model.renderAll();
    }

    public void renderPart(final String partName)
    {
        model.renderPart(partName);
    }
}