package cn.duoduo.modhider.mixins;

import cn.duoduo.modhider.config.ConfigManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public abstract class MixinModList {

    private static final Logger logger = LogManager.getLogger();

    @Shadow(remap = false)
    private Map<String, String> modTags;

    @Inject(method = "toBytes", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void toBytes(ByteBuf buffer, CallbackInfo callbackInfo) {
        callbackInfo.cancel();

        ArrayList<Map.Entry<String, String>> shownTags = new ArrayList<>();
        for (Map.Entry<String, String> modTag : this.modTags.entrySet()) {
            boolean hidden = Arrays.stream(ConfigManager.instance.getHiddenMods()).anyMatch(s -> s.equalsIgnoreCase(modTag.getKey()));
            if (!hidden) {
                shownTags.add(modTag);
            } else {
                logger.info(String.format("HideModList: %s %s", modTag.getKey(), modTag.getValue()));
            }
        }

        ByteBufUtils.writeVarInt(buffer, shownTags.size(), 2);

        for (Map.Entry<String, String> modTag : shownTags) {
            logger.info(String.format("SendModList: %s %s", modTag.getKey(), modTag.getValue()));
            ByteBufUtils.writeUTF8String(buffer, (String) modTag.getKey());
            ByteBufUtils.writeUTF8String(buffer, (String) modTag.getValue());
        }

    }

}
