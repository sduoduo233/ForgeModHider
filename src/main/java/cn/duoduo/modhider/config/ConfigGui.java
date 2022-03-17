package cn.duoduo.modhider.config;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ConfigGui extends GuiScreen {

    private static class Mod {

        private String modId;
        private boolean hidden = false;

        public Mod(String modId, boolean hidden) {
            this.modId = modId;
            this.hidden = hidden;
        }

        public String getModId() {
            return modId;
        }

        public Mod setModId(String modId) {
            this.modId = modId;
            return this;
        }

        public boolean isHidden() {
            return hidden;
        }

        public Mod setHidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }
    }

    private List<Mod> mods = new ArrayList<>();
    private int selected = -1;
    private int scroll = 0;

    public ConfigGui(GuiScreen parent) {
        for (ModContainer modContainer : Loader.instance().getActiveModList()) {
            mods.add(new Mod(modContainer.getModId(),
                    Arrays.stream(ConfigManager.instance.getHiddenMods()).anyMatch(s -> s.equals(modContainer.getModId()))));
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);

        this.buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - (80 * 2 + 5) / 2, sr.getScaledHeight() - 25, 80, 20, "Hide"));
        this.buttonList.add(new GuiButton(2, sr.getScaledWidth() / 2 - (80 * 2 + 5) / 2 + (80 + 5), sr.getScaledHeight() - 25, 80, 20, "Show"));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int p_mouseClicked_3_) throws IOException {
        super.mouseClicked(mouseX, mouseY, p_mouseClicked_3_);

        ScaledResolution sr = new ScaledResolution(mc);

        int minY = 10 + fontRendererObj.FONT_HEIGHT + 10;
        int maxY = sr.getScaledHeight() - 25 - 10;

        if (mouseY < minY || mouseY > maxY) return;

        int selectedRender = (mouseY - minY) / (5 + fontRendererObj.FONT_HEIGHT + 5);
        selected = selectedRender + scroll;
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_drawScreen_3_) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        fontRendererObj.drawStringWithShadow("Hidden Mods", sr.getScaledWidth() / 2f - fontRendererObj.getStringWidth("Hidden Mods") / 2f, 10, Color.WHITE.getRGB());

        int i = 0;
        int y = 10 + fontRendererObj.FONT_HEIGHT + 10;
        for (Mod mod : this.mods) {
            if (i >= scroll) {
                if (selected == i)
                    Gui.drawRect(20, y, sr.getScaledWidth() - 20, y + 5 + fontRendererObj.FONT_HEIGHT + 5, new Color(138, 138, 138, 179).getRGB());
                String name = mod.getModId() + (mod.isHidden() ? " (HIDDEN)" : "");
                fontRendererObj.drawString(name, sr.getScaledWidth() / 2 - fontRendererObj.getStringWidth(name) / 2, y + 5, Color.WHITE.getRGB());
                y += 5 + fontRendererObj.FONT_HEIGHT + 5;
            }
            i++;
        }

        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            scroll--;
        }
        if (dWheel > 0) {
            scroll++;
        }

        if (scroll < 0) scroll = 0;
        if (scroll >= mods.size()) scroll = mods.size() - 1;

        super.drawScreen(mouseX, mouseY, p_drawScreen_3_);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (selected < 0) return;
        switch (button.id) {
            case 1: // hide
                mods.get(selected).setHidden(true);
                ConfigManager.instance.addHiddenMod(mods.get(selected).getModId());
                break;
            case 2: // show
                mods.get(selected).setHidden(false);
                ConfigManager.instance.removeHiddenMod(mods.get(selected).getModId());
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
