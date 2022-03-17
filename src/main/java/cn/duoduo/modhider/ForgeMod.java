package cn.duoduo.modhider;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "modhider", name = "ForgeModHider", version = "1.0", guiFactory = "cn.duoduo.modhider.config.GuiFactory")
public class ForgeMod {
    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {

    }
}
