package ccm.nucleum_omnium.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import ccm.nucleum_omnium.helper.FunctionHelper;
import ccm.nucleum_omnium.helper.JavaHelper;
import ccm.nucleum_omnium.helper.CommandHelper;
import ccm.nucleum_omnium.utils.lib.Commands;

public class CommandTPX extends CommandBase {
    
    @Override
    public String getCommandName() {
        return Commands.COMMAND_TPX;
    }
    
    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return sender.translateString("commands.tpx.usage", new Object[0]);
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (JavaHelper.isNumeric(args[args.length - 1])) {
                final World dimension = FunctionHelper.getDimensions()
                        .get(Integer.parseInt(args[args.length - 1]));
                if (dimension != null) {
                    final ChunkCoordinates spawn = dimension.getSpawnPoint();
                    CommandHelper.teleportPlayer(sender,
                                                  CommandHelper.getPlayer(sender),
                                                  dimension.provider.dimensionId,
                                                  spawn.posX,
                                                  spawn.posY,
                                                  spawn.posZ);
                } else {
                    CommandBase.notifyAdmins(sender,
                                             "error.invalid.dim",
                                             new Object[] { sender.getCommandSenderName(),
                                                     args[args.length - 1] });
                }
            } else {
                CommandHelper.teleportPlayer(sender,
                                              CommandHelper.getPlayer(sender),
                                              CommandHelper.getPlayer(sender,
                                                                       args[args.length - 1]));
            }
        } else if (args.length == 2) {
            if (JavaHelper.isNumeric(args[args.length - 1])) {
                final World dimension = FunctionHelper.getDimensions()
                        .get(Integer.parseInt(args[args.length - 1]));
                if (dimension != null) {
                    final ChunkCoordinates spawn = dimension.getSpawnPoint();
                    CommandHelper.teleportPlayer(sender,
                                                  CommandHelper.getPlayer(sender,
                                                                           args[args.length - 2]),
                                                  dimension.provider.dimensionId,
                                                  spawn.posX,
                                                  spawn.posY,
                                                  spawn.posZ);
                } else {
                    CommandBase.notifyAdmins(sender,
                                             "error.invalid.dim",
                                             new Object[] { sender.getCommandSenderName(),
                                                     args[args.length - 1] });
                }
            } else {
                CommandHelper.teleportPlayer(sender, CommandHelper
                        .getPlayer(sender, args[args.length - 2]), CommandHelper
                        .getPlayer(sender, args[args.length - 1]));
            }
        } else if (args.length == 4) {
            final EntityPlayerMP player = CommandHelper.getPlayer(sender);
            if (player.worldObj != null) {
                int i = args.length - 4;
                final int dimension = CommandBase.parseInt(sender, args[i++]);
                final double x = CommandHelper.checkPosition(sender, player.posX, args[i++]);
                final double y = CommandHelper.checkPositionWithBounds(sender,
                                                                        player.posY,
                                                                        args[i++],
                                                                        0,
                                                                        0);
                final double z = CommandHelper.checkPosition(sender, player.posZ, args[i++]);
                CommandHelper.teleportPlayer(sender, player, dimension, x, y, z);
            }
        } else if (args.length == 5) {
            final EntityPlayerMP player = CommandHelper.getPlayer(sender, args[args.length - 5]);
            if (player.worldObj != null) {
                int i = args.length - 4;
                final int dimension = CommandBase.parseInt(sender, args[i++]);
                final double x = CommandHelper.checkPosition(sender, player.posX, args[i++]);
                final double y = CommandHelper.checkPositionWithBounds(sender,
                                                                        player.posY,
                                                                        args[i++],
                                                                        0,
                                                                        0);
                final double z = CommandHelper.checkPosition(sender, player.posZ, args[i++]);
                CommandHelper.teleportPlayer(sender, player, dimension, x, y, z);
            }
        } else {
            throw new WrongUsageException("commands.tpx.usage", new Object[0]);
        }
    }
    
    /**
     * Adds the strings available in this command to the given list of tab
     * completion options.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args) {
        if ((args.length != 1) && (args.length != 2)) {
            return null;
        } else {
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer()
                    .getAllUsernames());
        }
    }
    
    /**
     * Return whether the specified command parameter index is a username
     * parameter.
     */
    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex) {
        return userIndex == 0;
    }
}