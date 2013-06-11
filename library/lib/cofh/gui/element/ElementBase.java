package lib.cofh.gui.element;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.client.FMLClientHandler;

import lib.cofh.gui.GuiBase;

/**
 * Base class for a modular GUI element. Has self-contained rendering methods and a link back to the
 * {@link GuiBase} it is a part of.
 * 
 * @author King Lemming
 */
public abstract class ElementBase
{

    public static final SoundManager elementSoundManager = FMLClientHandler.instance().getClient().sndManager;

    public static final FontRenderer elementFontRenderer = FMLClientHandler.instance().getClient().fontRenderer;

    protected GuiBase                gui;

    protected String                 texture;

    protected int                    posX;

    protected int                    posY;

    protected int                    sizeX;

    protected int                    sizeY;

    public int                       texW                = 256;

    public int                       texH                = 256;

    protected boolean                visible             = true;

    public ElementBase(final GuiBase gui,
                       final int posX,
                       final int posY)
    {

        this.gui = gui;
        this.posX = gui.guiLeft + posX;
        this.posY = gui.guiTop + posY;
    }

    public ElementBase setTexture(final String texture, final int texW, final int texH)
    {

        this.texture = texture;
        this.texW = texW;
        this.texH = texH;
        return this;
    }

    public ElementBase setPosition(final int posX, final int posY)
    {

        this.posX = this.gui.guiLeft + posX;
        this.posY = this.gui.guiTop + posY;
        return this;
    }

    public ElementBase setSize(final int sizeX, final int sizeY)
    {

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        return this;
    }

    public ElementBase setVisible(final boolean visible)
    {

        this.visible = visible;
        return this;
    }

    public boolean isVisible()
    {

        return this.visible;
    }

    public void update()
    {

    }

    public abstract void draw();

    public void draw(final int x, final int y)
    {

        this.posX = x;
        this.posY = y;
        this.draw();
    }

    public abstract String getTooltip();

    public boolean intersectsWith(int mouseX, int mouseY)
    {

        mouseX += this.gui.guiLeft;
        mouseY += this.gui.guiTop;

        if ((mouseX >= this.posX) && (mouseX <= (this.posX + this.sizeX)) && (mouseY >= this.posY) && (mouseY <= (this.posY + this.sizeY))){
            return true;
        }
        return false;
    }

    public boolean handleMouseClicked(final int x, final int y, final int mouseButton)
    {

        return false;
    }

    public void drawTexturedModalRect(final int x, final int y, final int u, final int v, final int width, final int height)
    {

        this.gui.drawSizedTexturedModalRect(x, y, u, v, width, height, this.texW, this.texH);
    }

}