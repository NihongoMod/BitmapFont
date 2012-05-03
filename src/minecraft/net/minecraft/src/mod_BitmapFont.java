package net.minecraft.src;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.src.bmpfont.BMPFont_FontRenderer;
import net.minecraft.src.bmpfont.BitmapFont;
import net.minecraft.src.bmpfont.BitmapFontLoader;
import net.minecraft.src.bmpfont.BitmapFontProperties;
import net.minecraft.src.bmpfont.BitmapFontRenderer;

/**
 * ビットマップフォントを表示するためのMOD
 */
public class mod_BitmapFont extends BaseMod {
    public static final String UNIQUE_NAME = "BitmapFont";
    public static final String VERSION = "v1.0";

    private BMPFont_FontRenderer fontRenderer;
    private BitmapFontProperties properties;

    @Override
    public String getVersion() {
        return UNIQUE_NAME + " " + VERSION;
    }

    public mod_BitmapFont() throws IOException {
        File mcroot = Minecraft.getMinecraftDir();
        File configDir = new File(mcroot, "config");
        File propFile = new File(configDir, UNIQUE_NAME + ".properties");
        properties = new BitmapFontProperties(propFile);

        File fontFile = new File(getFontDir(mcroot), properties.fontFile);
        BitmapFontLoader loader = new BitmapFontLoader();
        BitmapFont font = loader.load(fontFile);

        Minecraft minecraft = ModLoader.getMinecraftInstance();
        BitmapFontRenderer renderer = new BitmapFontRenderer(minecraft, font, (float) properties.baseLine, (float) properties.scale);
        this.fontRenderer = new BMPFont_FontRenderer(renderer, minecraft.gameSettings, "/font/default.png", minecraft.renderEngine, false);

        this.fontRenderer.setEnableMod(properties.enableMod);

        // FontRendererの置き換え
        minecraft.fontRenderer = this.fontRenderer;
    }

    /**
     * font/ ディレクトリを取得します。
     */
    private File getFontDir(File mcroot) {
        File fontDir = new File(getResourceDir(mcroot), "font");
        if (!fontDir.exists())
            fontDir.mkdir();
        return fontDir;
    }

    /**
     * resources/ フォルダを取得します。
     * @return
     */
    private File getResourceDir(File mcroot) {
        File rsrcDir = new File(mcroot, "resources");
        if (!rsrcDir.exists())
            rsrcDir.mkdir();
        return rsrcDir;
    }

    @Override
    public void load() {
    }
}
