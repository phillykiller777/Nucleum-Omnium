package ccm.nucleum_omnium.block.sub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ccm.nucleum_omnium.block.ICollisionListener;
import ccm.nucleum_omnium.block.IDisplayListener;
import ccm.nucleum_omnium.block.MainBlock;
import ccm.nucleum_omnium.helper.TextureHelper;
import ccm.nucleum_omnium.helper.enums.IBlockEnum;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubBlock {
    
    /*
     * DATA
     */
    private MainBlock               mainBlock;
    private CreativeTabs            tab;
    private int                     meta;
    
    private ItemStack               drop;
    private int                     dropMin;
    private int                     dropMax;
    
    private float                   hardness;
    private float                   blockResistance;
    
    private boolean                 collisionEffect;
    
    private String                  unlocName;
    
    public List<IDisplayListener>   displayList   = new ArrayList<IDisplayListener>();
    public List<ICollisionListener> collisionList = new ArrayList<ICollisionListener>();
    
    public Icon                     icon;
    public String                   iconName;
    
    /*
     * Constructors
     */
    /**
     * Creates a new SubBlock instance
     * 
     * @param id
     *            The ID of the MainBlock to use (Put the same for multiple instances if you want them to all correspond to the same block ID)
     * @param meta
     *            The metadata to use (Typically the enum.ordinal())
     * @param iconName
     *            The full path of the Icon, ex: harvestry:coal
     */
    public SubBlock(final int id, final int meta, final String iconName) {
        if (Block.blocksList[id] == null) {
            mainBlock = new MainBlock(id);
            mainBlock.addSubBlock(this, meta);
        } else {
            mainBlock = (MainBlock) Block.blocksList[id];
            mainBlock.addSubBlock(this, meta);
        }
        
        this.meta = meta;
        this.iconName = iconName;
    }
    
    /**
     * Creates a new SubBlock instance
     * 
     * @param id
     *            The ID of the MainBlock to use (Put the same for multiple instances if you want them to all correspond to the same block ID)
     * @param meta
     *            The metadata to use (Typically the enum.ordinal())
     * @param material
     *            The Material that the block should be made of
     * @param iconName
     *            The full path of the Icon, ex: harvestry:coal
     */
    public SubBlock(final int id, final int meta, final Material material, final String iconName) {
        if (Block.blocksList[id] == null) {
            mainBlock = new MainBlock(id, material);
            mainBlock.addSubBlock(this, meta);
        } else {
            mainBlock = (MainBlock) Block.blocksList[id];
            mainBlock.addSubBlock(this, meta);
        }
        
        this.meta = meta;
        this.iconName = iconName;
    }
    
    /*
     * Listeners
     */
    /**
     * @param displayL
     *            the {@link IDisplayListener} who's randomDisplayTick method will be called for this instance
     */
    public void addDisplayListener(final IDisplayListener displayL) {
        mainBlock.setTickRandomly(meta);
        displayList.add(displayL);
    }
    
    /**
     * @param collisionL
     *            the {@link ICollisionListener} who's collide method will be called for this instance
     */
    public void addCollisionListener(final ICollisionListener collisionL) {
        collisionEffect = true;
        collisionList.add(collisionL);
    }
    
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
        for (final IDisplayListener dl : displayList) {
            dl.randomDisplayTick(world, x, y, z, rand);
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        if (collisionEffect) {
            final float var5 = 0.025F;
            return AxisAlignedBB.getAABBPool().getAABB(x, y, z, (x + 1), ((y + 1.0F) - var5), (z + 1));
        }
        
        return null;
    }
    
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity par5Entity) {
        for (final ICollisionListener cl : collisionList) {
            cl.collide(world, x, y, z, par5Entity, meta);
        }
    }
    
    /*
     * Instance Modifiers
     */
    public SubBlock setCreativeTab(final CreativeTabs tab) {
        mainBlock.setCreativeTab(tab);
        this.tab = tab;
        return this;
    }
    
    public SubBlock setUnlocalizedName(final String string) {
        unlocName = string;
        mainBlock.setUnlocalizedName(unlocName);
        return this;
    }
    
    public SubBlock setBlockDrops(final ItemStack item, final int min, final int max) {
        drop = item.copy();
        dropMin = min;
        dropMax = max;
        return this;
    }
    
    public SubBlock setHardness(final float hardness) {
        this.hardness = hardness;
        
        if (blockResistance < (hardness * 5.0F)) {
            blockResistance = hardness * 5.0F;
        }
        
        return this;
    }
    
    public SubBlock setResistance(final float ressistance) {
        blockResistance = ressistance * 3;
        return this;
    }
    
    public SubBlock setSlipperiness(final float slipperiness) {
        mainBlock.slipperiness = slipperiness;
        return this;
    }
    
    /*
     * Block Redirect Methods
     */
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }
    
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return this.icon;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IconRegister iconRegister) {
        icon = iconRegister.registerIcon(iconName);
    }
    
    public Icon getBlockTextureFromSide(final int side) {
        return icon;
    }
    
    public CreativeTabs getCreativeTab() {
        return tab;
    }
    
    public String getUnlocalizedName() {
        return unlocName;
    }
    
    public Block getBlock() {
        return mainBlock;
    }
    
    public int quantityDroppedWithBonus(final int fortune, final Random rand) {
        if ((drop != null) && (dropMax > 1)) {
            return dropMin + rand.nextInt(dropMax + fortune) + fortune;
        } else {
            return 1;
        }
    }
    
    public int idDropped(final Random rand, final int par3) {
        if (drop != null) {
            return drop.itemID;
        } else {
            return mainBlock.blockID;
        }
    }
    
    public float getBlockHardness() {
        return hardness;
    }
    
    public float getExplosionResistance(final Entity entity,
                                        final World world,
                                        final int x,
                                        final int y,
                                        final int z,
                                        final double explosionX,
                                        final double explosionY,
                                        final double explosionZ) {
        return blockResistance / 5.0F;
    }
    
    public int damageDropped(final int meta) {
        if (idDropped(new Random(), meta) == mainBlock.blockID) {
            return meta;
        } else {
            return 0;
        }
    }
    
    public int getDamageValue(final World world, final int x, final int y, final int z) {
        return meta;
    }
    
    public TileEntity createTileEntity(final World world, final int meta) {
        return null;
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final int id, final int meta) {}
    
    public boolean onBlockActivated(final World world,
                                    final int x,
                                    final int y,
                                    final int z,
                                    final EntityPlayer player,
                                    final int wut,
                                    final float clickX,
                                    final float clickY,
                                    final float clockZ) {
        
        return false;
    }
    
    public void onBlockAdded(final World world, final int x, final int y, final int z) {}
    
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLiving living, final ItemStack itemStack) {}
    
    /*
     * Object Redirect Methods
     */
    @Override
    public String toString() {
        return super.toString() + mainBlock.getUnlocalizedName();
    }
    
    /*
     * Static Factory's
     */
    public static SubBlock setUp(final Enum<? extends IBlockEnum> blockEnum, final SubBlock instance) {
        
        MainBlock block = (MainBlock) instance.getBlock();
        
        ((IBlockEnum) blockEnum).setBaseBlock(block);
        
        MainBlock.registerID(block.blockID);
        
        return instance;
    }
    
    public static SubBlock createAndSetUp(final Enum<? extends IBlockEnum> blockEnum, final int id, final String textureLoc) {
        
        return setUp(blockEnum, new SubBlock(id, blockEnum.ordinal(), TextureHelper.getTextureFromName(blockEnum.name(), textureLoc)).setUnlocalizedName(blockEnum.name()));
    }
}