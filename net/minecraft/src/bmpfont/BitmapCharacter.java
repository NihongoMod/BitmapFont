package net.minecraft.src.bmpfont;

import java.util.HashMap;
import java.util.Map;

/**
 * ビットマップフォントで扱われる1文字
 */
public class BitmapCharacter {
    public final char c;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int xOffset;
    public final int yOffset;
    public final int xAdvance;
    public final int page;

    protected final float s0;
    protected final float s1;
    protected final float t0;
    protected final float t1;

    private Map<Integer, Integer> kerning = new HashMap<Integer, Integer>();

    public BitmapCharacter(BitmapFont font, char c, int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance, int page) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
        this.page = page;

        this.s0 = (float) x / font.imgWidth;
        this.t0 = (float) y / font.imgHeight;
        this.s1 = s0 + (float) width / font.imgWidth;
        this.t1 = t0 + (float) height / font.imgHeight;
    }

    public void addKerning(int second, int amount) {
        kerning.put(second, amount);
    }

    public int getKerning(int second) {
        Integer i = kerning.get(second);
        if (i == null)
            return 0;
        else
            return i.intValue();
    }

}
