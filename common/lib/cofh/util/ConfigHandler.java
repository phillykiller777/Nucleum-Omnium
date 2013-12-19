package lib.cofh.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * This is effectively a wrapper for Forge Configurations. It allows for easier manipulation of Config files.
 * 
 * @author King Lemming
 */
public class ConfigHandler
{
    ArrayList<String> blockEntries = new ArrayList<String>();
    ArrayList<String> itemEntries = new ArrayList<String>();

    TreeMap<String, Property> blockIds = new TreeMap<String, Property>();
    TreeMap<String, Property> itemIds = new TreeMap<String, Property>();

    Configuration modConfiguration;

    int blockIdStart = 1000;
    int itemIdStart = 3000;

    public ConfigHandler()
    {
        blockEntries = new ArrayList<String>();
        itemEntries = new ArrayList<String>();
    }

    public ConfigHandler(int blockStart, int itemStart)
    {
        blockIdStart = blockStart;
        itemIdStart = itemStart;

        blockEntries = new ArrayList<String>();
        itemEntries = new ArrayList<String>();
    }

    public void setConfiguration(Configuration config)
    {
        modConfiguration = config;
        load();
    }

    public Configuration getConfiguration()
    {
        return modConfiguration;
    }

    public void addBlockEntry(String name)
    {
        blockEntries.add(name);
    }

    public void addItemEntry(String name)
    {
        itemEntries.add(name);
    }

    public int getBlockId(String name)
    {
        Property ret = blockIds.get(name);

        if (ret == null)
        {
            return -1;
        }
        return ret.getInt();
    }

    public int getItemId(String name)
    {
        Property ret = itemIds.get(name);

        if (ret == null)
        {
            return -1;
        }
        return ret.getInt();
    }

    public int get(String category, String key, int defaultValue)
    {
        return modConfiguration.get(category, key, defaultValue).getInt();
    }

    public boolean get(String category, String key, boolean defaultValue)
    {
        return modConfiguration.get(category, key, defaultValue).getBoolean(defaultValue);
    }

    public String get(String category, String key, String defaultValue)
    {
        return modConfiguration.get(category, key, defaultValue).getString();
    }

    public Property getProperty(String category, String key, int defaultValue)
    {
        return modConfiguration.get(category, key, defaultValue);
    }

    public Property getProperty(String category, String key, boolean defaultValue)
    {
        return modConfiguration.get(category, key, defaultValue);
    }

    public Property getProperty(String category, String key, String defaultValue)
    {
        return modConfiguration.get(category, key, defaultValue);
    }

    public ConfigCategory getCategory(String category)
    {
        return modConfiguration.getCategory(category);
    }

    public Map<?, ?> getCategoryMap(String category)
    {
        return modConfiguration.getCategory(category).getValues();
    }

    public Set<String> getCategoryKeys(String category)
    {
        return modConfiguration.getCategory(category).getValues().keySet();
    }

    public boolean hasCategory(String category)
    {
        return modConfiguration.hasCategory(category);
    }

    public boolean hasKey(String category, String key)
    {
        return modConfiguration.hasKey(category, key);
    }

    public void init()
    {
        for (String entry : blockEntries)
        {
            if (modConfiguration.hasKey(Configuration.CATEGORY_BLOCK, entry))
            { // get ids for existing blocks
                int existingId = modConfiguration.getCategory(Configuration.CATEGORY_BLOCK).getValues().get(entry).getInt();
                blockIds.put(entry, modConfiguration.getBlock(entry, existingId));
            } else
            { // get ids for new blocks
                boolean idFound = false;
                for (int id = blockIdStart; (id < Block.blocksList.length) && !idFound; ++id)
                {
                    if (Block.blocksList[id] == null)
                    {
                        blockIds.put(entry, modConfiguration.getBlock(entry, id));
                        idFound = true;
                    }
                }
            }
        }
        for (String entry : itemEntries)
        {
            if (modConfiguration.hasKey(Configuration.CATEGORY_ITEM, entry))
            {// get ids for existing items
                int existingId = modConfiguration.getCategory(Configuration.CATEGORY_ITEM).getValues().get(entry).getInt();
                itemIds.put(entry, modConfiguration.getItem(entry, existingId));
            } else
            {// get ids for new items
                boolean idFound = false;
                for (int id = itemIdStart; (id < Item.itemsList.length) && !idFound; ++id)
                {
                    if (Item.itemsList[id] == null)
                    {
                        itemIds.put(entry, modConfiguration.getItem(entry, id));
                        idFound = true;
                    }
                }
            }
        }
        save();
    }

    public void save()
    {
        if (modConfiguration.hasChanged())
        {
            modConfiguration.save();
        }
    }

    public void load()
    {
        modConfiguration.load();
    }

    public boolean renameProperty(String category, String key, String newCategory, String newKey, boolean forceValue)
    {
        if (modConfiguration.hasKey(category, key))
        {
            Property prop = modConfiguration.getCategory(category).get(key);

            if (prop.isIntValue())
            {
                int value = modConfiguration.getCategory(category).getValues().get(key).getInt();
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            } else if (prop.isBooleanValue())
            {
                boolean value = modConfiguration.getCategory(category).getValues().get(key).getBoolean(false);
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            } else if (prop.isDoubleValue())
            {
                double value = modConfiguration.getCategory(category).getValues().get(key).getDouble(0.0);
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            } else
            {
                String value = modConfiguration.getCategory(category).getValues().get(key).getString();
                removeProperty(category, key);

                if (forceValue)
                {
                    removeProperty(newCategory, newKey);
                }
                modConfiguration.get(newCategory, newKey, value);
            }
            return true;
        }
        return false;
    }

    public boolean removeProperty(String category, String key)
    {
        if (!modConfiguration.hasKey(category, key))
        {
            return false;
        }
        modConfiguration.getCategory(category).remove(key);
        return true;
    }

    public boolean renameCategory(String category, String newCategory)
    {
        if (!modConfiguration.hasCategory(category))
        {
            return false;
        }
        for (Property prop : modConfiguration.getCategory(category).values())
        {
            renameProperty(category, prop.getName(), newCategory, prop.getName(), true);
        }
        removeCategory(category);
        return true;
    }

    public boolean removeCategory(String category)
    {
        if (!modConfiguration.hasCategory(category))
        {
            return false;
        }
        modConfiguration.removeCategory(modConfiguration.getCategory(category));
        return true;
    }
}