package carpet.logging.logHelpers;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockEventData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

public class BlockEventsLogHelper
{
    private static int lastTick = 0;
    private static TextFormatting tickColor = TextFormatting.YELLOW;
    private static boolean updateFlag;
    
    public static void updateTimestamp(int ticks)
    {
        if (updateFlag)
        {
            lastTick = ticks;
            tickColor = tickColor == TextFormatting.YELLOW ? TextFormatting.GOLD : TextFormatting.YELLOW;
            updateFlag = false;
        }
    }
    
    public static void sendMessage(WorldServer world, BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        if (blockIn instanceof BlockEnderChest)
            return;
        MinecraftServer server = world.getMinecraftServer();
        if (server != null)
        {
            int ticks = server.getTickCounter();
            if (ticks != lastTick)
            {
                updateFlag = true;
            }
            TextFormatting blockColor = eventID == 0 ? TextFormatting.GREEN : TextFormatting.RED;
            String text = tickColor + "[" + ticks + "] " +  blockColor + blockIn.getLocalizedName() + TextFormatting.BLUE + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
            LoggerRegistry.getLogger("blockEvents").log(() -> new ITextComponent[] {
                    Messenger.s(null, text)
            });
        }
    }
    
    public static void queueClearedMessage(WorldServer world, BlockEventData event)
    {
        if (event.getBlock() instanceof BlockEnderChest)
            return;
        
        if (world.getMinecraftServer() != null)
        {
            int ticks = world.getMinecraftServer().getTickCounter();
            String text = tickColor + "[" + ticks + "] " + TextFormatting.RESET + "Block event queue swapped";
            
            LoggerRegistry.getLogger("blockEvents").log(() -> new ITextComponent[] {
                    Messenger.s(null, text)
            });
        }
    }
}
