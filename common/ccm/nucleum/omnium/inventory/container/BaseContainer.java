/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.omnium.inventory.container;

import java.lang.reflect.Constructor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import ccm.nucleum.omnium.inventory.slot.OutputSlot;
import ccm.nucleum.omnium.utils.helper.CCMLogger;

public abstract class BaseContainer extends Container
{
    protected final IInventory inventory;

    public BaseContainer(final IInventory inventory)
    {
        this.inventory = inventory;
    }

    public BaseContainer(final IInventory inventory, final InventoryPlayer player, final int x, final int y)
    {
        this(inventory);
        drawPlayerFullInv(player, x, y);
    }

    private void createSlot(final IInventory inventory,
                            final Class<? extends Slot> slot,
                            final int index,
                            final int x,
                            final int y,
                            final int row,
                            final int column)
    {
        Constructor<? extends Slot> c = null;
        try
        {
            c = slot.getConstructor(IInventory.class, int.class, int.class, int.class);
        } catch (final Exception e)
        {
            CCMLogger.DEFAULT.printCatch(e, "FAILED @ createSlot DURING getConstructor WITH %s, %s, %s, %s", inventory, index, x, y);;
        }
        try
        {
            addSlotToContainer(c.newInstance(inventory, index, x + (column * 18), y + (row * 18)));
        } catch (final Exception e)
        {
            CCMLogger.DEFAULT.printCatch(e,
                                                "FAILED @ createSlot DURING newInstance WITH %s, %s, %s, %s, %s, %s, %s",
                                                inventory,
                                                slot,
                                                index,
                                                x,
                                                y,
                                                row,
                                                column);
        }
    }

    public IInventory getIInventory()
    {
        return inventory;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player)
    {
        return inventory.isUseableByPlayer(player);
    }

    /**
     * Draws a Box made of slots inside the specified inventory
     * 
     * @param slot
     *            A class with the following constructor {@code IInventory, int, int, int}
     * @param rowSize
     *            The Amount of Rows
     * @param columnSize
     *            The Amount of Columns
     * @return The last index + 1
     */
    public int drawBoxInventory(final IInventory inventory,
                                final Class<? extends Slot> slot,
                                int index,
                                final int x,
                                final int y,
                                final int rowSize,
                                final int columnSize)
    {
        for (int row = 0; row < rowSize; ++row)
        {
            for (int column = 0; column < columnSize; ++column)
            {
                if (slot != Slot.class)
                {
                    if (slot != OutputSlot.class)
                    {
                        createSlot(inventory, slot, index++, x, y, row, column);
                    } else
                    {
                        addSlotToContainer(new OutputSlot(inventory, index++, x + (column * 18), y + (row * 18)));
                    }
                } else
                {
                    addSlotToContainer(new Slot(inventory, index++, x + (column * 18), y + (row * 18)));
                }
            }
        }
        return index;
    }

    public int drawBoxInventory(final IInventory inventory, final int index, final int x, final int y, final int rowSize, final int columnSize)
    {
        return drawBoxInventory(inventory, Slot.class, index, x, y, rowSize, columnSize);
    }

    public int drawOutBoxInventory(final IInventory inventory, final int index, final int x, final int y, final int rowSize, final int columnSize)
    {
        return drawBoxInventory(inventory, OutputSlot.class, index, x, y, rowSize, columnSize);
    }

    public int drawPlayerFullInv(final InventoryPlayer player, final int x, final int y)
    {
        return drawPlayerInventory(player, drawPlayerHotBar(player, 0, x, y + 58), x, y);
    }

    public int drawPlayerHotBar(final InventoryPlayer player, final int index, final int x, final int y)
    {
        return drawBoxInventory(player, index, x, y, 1, 9);
    }

    public int drawPlayerInventory(final InventoryPlayer player, final int index, final int x, final int y)
    {
        return drawBoxInventory(player, index, x, y, 3, 9);
    }

    public abstract Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z);
}