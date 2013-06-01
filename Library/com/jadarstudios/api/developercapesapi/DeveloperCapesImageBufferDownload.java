package com.jadarstudios.api.developercapesapi;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.IImageBuffer;

@SideOnly(Side.CLIENT)
public class DeveloperCapesImageBufferDownload implements IImageBuffer
{

    private int imageWidth;

    private int imageHeight;

    @Override
    public BufferedImage parseUserSkin(final BufferedImage par1BufferedImage)
    {
        if (par1BufferedImage == null){
            return null;
        }else{
            this.imageWidth = (par1BufferedImage.getWidth((ImageObserver) null) <= 64) ? 64 : (par1BufferedImage.getWidth((ImageObserver) null));
            this.imageHeight = (par1BufferedImage.getHeight((ImageObserver) null) <= 32) ? 32 : (par1BufferedImage.getHeight((ImageObserver) null));

            final BufferedImage capeImage = new BufferedImage(this.imageWidth, this.imageHeight, 2);

            final Graphics graphics = capeImage.getGraphics();
            graphics.drawImage(par1BufferedImage, 0, 0, (ImageObserver) null);
            graphics.dispose();

            return capeImage;
        }
    }
}
