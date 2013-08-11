/**
 * Developer Capes by Jadar
 * License: Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * version 2.0
 */
package lib.com.jadarstudios.developercapes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import net.minecraft.client.renderer.IImageBuffer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DevCapesImageBufferDownload implements IImageBuffer
{

    private int imageWidth;
    private int imageHeight;

    @Override
    public BufferedImage parseUserSkin(final BufferedImage par1BufferedImage)
    {
        if (par1BufferedImage == null)
        {
            return null;
        }
        else
        {
            imageWidth = (par1BufferedImage.getWidth((ImageObserver) null) <= 64) ? 64
                                                                                 : (par1BufferedImage.getWidth((ImageObserver) null));
            imageHeight = (par1BufferedImage.getHeight((ImageObserver) null) <= 32) ? 32
                                                                                   : (par1BufferedImage.getHeight((ImageObserver) null));

            final BufferedImage capeImage = new BufferedImage(imageWidth, imageHeight, 2);

            final Graphics graphics = capeImage.getGraphics();
            graphics.drawImage(par1BufferedImage, 0, 0, (ImageObserver) null);
            graphics.dispose();

            return capeImage;
        }
    }
}