package paulevs.edenring.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	/*@Shadow @Final private Minecraft minecraft;
	@Shadow private ClientLevel level;
	
	@Inject(method = "handleAddPainting(Lnet/minecraft/network/protocol/game/ClientboundAddPaintingPacket;)V", at = @At("HEAD"), cancellable = true)
	public void handleAddPainting(ClientboundAddPaintingPacket original, CallbackInfo info) {
		if (original instanceof EdenPaintingPaintingPacket) {
			EdenPaintingPaintingPacket packet = (EdenPaintingPaintingPacket) original;
			PacketUtils.ensureRunningOnSameThread(packet, ClientPacketListener.class.cast(this), this.minecraft);
			PaintingInfo paintingInfo = EdenPaintings.getPainting(packet.getRawID());
			EdenPainting painting = new EdenPainting(this.level, packet.getPos(), packet.getDirection(), paintingInfo);
			painting.setUUID(packet.getUUID());
			painting.setId(packet.getId());
			this.level.putNonPlayerEntity(packet.getId(), painting);
			info.cancel();
		}
	}*/
}
