package minechem.network.message;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import minechem.tileentity.synthesis.SynthesisTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SynthesisUpdateMessage implements IMessage, IMessageHandler<SynthesisUpdateMessage, IMessage>
{
    private int posX, posY, posZ;
    private int energyStored;

    public SynthesisUpdateMessage()
    {

    }

    public SynthesisUpdateMessage(SynthesisTileEntity tile)
    {
        this.posX = tile.xCoord;
        this.posY = tile.yCoord;
        this.posZ = tile.zCoord;

        this.energyStored = tile.getEnergyStored();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();

        this.energyStored = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);

        buf.writeInt(this.energyStored);
    }

    @Override
    public IMessage onMessage(SynthesisUpdateMessage message, MessageContext ctx)
    {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.posX, message.posY, message.posZ);
        if (tileEntity instanceof SynthesisTileEntity)
        {
            ((SynthesisTileEntity) tileEntity).syncEnergyValue(message.energyStored);
        }
        return null;
    }
}
