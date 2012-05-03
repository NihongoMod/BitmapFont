package net.minecraft.src.bmpfont;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GameSettings;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * ビットマップフォントを描画するためのクラス
 */
public class BitmapFontRenderer {
    private static final String OPTION_CHARACTERS = "0123456789abcdefklmnor";

    public static enum FontStyle {
        DEFAULT('r', 0),
        RANDOM('k', 1),
        BOLD('l', 2),
        STRIKE('m', 4),
        UNDERLINE('n', 8),
        ITALIC('o', 16);

        public final char optionChar;
        public final int flagBit;

        FontStyle(char c, int flagBit) {
            this.optionChar = c;
            this.flagBit = flagBit;
        }

        public static FontStyle getStyle(char c) {
            for (FontStyle fs : values()) {
                if (fs.optionChar == c)
                    return fs;
            }
            return DEFAULT;
        }
    }

    private static Random rand = new Random();

    private Minecraft minecraft;
    private GameSettings gamesettings;
    private RenderEngine renderengine;
    private BitmapFont font;

    private float propertyScale = 1f;
    private float propertyBaseLine = 0f;

    private float calcedScale;
    private float calcedBaseLine;
    private float fontHeight;

    private FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
    private int[] colorTable = new int[32];

    public void setScale(float scale) {
        this.propertyScale = scale;
    }

    public void setBaseLine(float baseLine) {
        this.propertyBaseLine = baseLine;
    }

    public BitmapFontRenderer(Minecraft minecraft, BitmapFont font, float baseLine, float scale) {
        this.minecraft = minecraft;
        this.font = font;
        this.propertyBaseLine = baseLine;
        this.propertyScale = scale;
        this.gamesettings = minecraft.gameSettings;
        this.renderengine = minecraft.renderEngine;

        try {
            for (int i = 0; i < font.textureFiles.length; ++i) {
                font.texturePages[i] = renderengine.allocateAndSetupTexture(ImageIO.read(font.textureFiles[i]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < colorTable.length; i++) {
            int j1 = (i >> 3 & 1) * 85;
            int l1 = (i >> 2 & 1) * 170 + j1;
            int j2 = (i >> 1 & 1) * 170 + j1;
            int l2 = (i >> 0 & 1) * 170 + j1;
            if (i == 6) {
                l1 += 85;
            }
            if (gamesettings.anaglyph) {
                int i3 = (l1 * 30 + j2 * 59 + l2 * 11) / 100;
                int k3 = (l1 * 30 + j2 * 70) / 100;
                int i4 = (l1 * 30 + l2 * 70) / 100;
                l1 = i3;
                j2 = k3;
                l2 = i4;
            }
            if (i >= 16) {
                l1 /= 4;
                j2 /= 4;
                l2 /= 4;
            }
            colorTable[i] = (l1 & 0xff) << 16 | (j2 & 0xff) << 8 | l2 & 0xff;
        }
    }

    public void drawString(String str, int x, int y, int color) {
        renderString(str, x, y, color, false);
    }

    public int drawStringWithShadow(String str, int x, int y, int color) {
        int i = renderString(str, x + 1, y + 1, color, true);
        renderString(str, x, y, color, false);
        return i;
    }

    public void drawSplitString(String str, int x, int y, int width, int color) {
        renderSplitString(str, x, y, width, color, false);
    }

    public void drawSplitStringWithShadow(String str, int x, int y, int width, int color) {
        renderSplitString(str, x, y, width, color, true);
        renderSplitString(str, x, y, width, color, false);
    }

    public int getStringWidth(String s) {
        if (s == null)
            return 0;

        updateScaleAndBaseLine();

        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\247') {
                i++;
                continue;
            }

            BitmapCharacter bc = font.getCharacter(c);
            if (bc != null) {
                length += bc.xAdvance * calcedScale;
            }
        }
        return length;
    }

    public float getCharacterWidth(char c) {
        BitmapCharacter bc = font.getCharacter(c);
        return (bc != null) ? bc.xAdvance * calcedScale : 0;
    }

    public int getSplitStringHeight(String str, int width) {
        return (int) (splitString(str, width).size() * fontHeight);
    }

    private List<String> splitString(String str, int width) {
        List<String> list = new ArrayList<String>();

        String as[] = str.split("\n");
        if (as.length > 1) {
            for (String line : as) {
                for (String ss : splitString(line, width)) {
                    list.add(ss);
                }
            }
            return list;
        }

        String as1[] = str.split(" ");
        int j1 = 0;
        String s1 = "";
        do {
            if (j1 >= as1.length) {
                break;
            }
            
            String s2;
            for (s2 = s1 + as1[j1++] + " "; j1 < as1.length && getStringWidth(s2 + as1[j1]) < width; s2 = s2 + as1[j1++] + " ");

            int k1;
            for (; getStringWidth(s2) > width; s2 = s1 + s2.substring(k1)) {
                for (k1 = 0; getStringWidth(s2.substring(0, k1 + 1)) <= width; k1++);
                if (s2.substring(0, k1).trim().length() <= 0) {
                    continue;
                }
                String s3 = s2.substring(0, k1);
                if (s3.lastIndexOf("\247") >= 0) {
                    s1 = "\247" + s3.charAt(s3.lastIndexOf("\247") + 1);
                }
                list.add(s3);
            }

            if (getStringWidth(s2.trim()) > 0) {
                if (s2.lastIndexOf("\247") >= 0) {
                    s1 = "\247" + s2.charAt(s2.lastIndexOf("\247") + 1);
                }
                list.add(s2);
            }
        } while (true);

        return list;
    }

    protected int renderString(String str, int x, int y, int color, boolean darken) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        updateScaleAndBaseLine();

        if (str != null) {
            if ((color & 0xfc000000) == 0) {
                color |= 0xff000000;
            }
            if (darken) {
                color = (color & 0xfcfcfc) >> 2 | color & 0xff000000;
            }
            GL11.glColor4f(
                    (float) (color >> 16 & 0xff) / 255F,
                    (float) (color >>  8 & 0xff) / 255F,
                    (float) (color       & 0xff) / 255F,
                    (float) (color >> 24 & 0xff) / 255F);
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0f);
            func_44029_a(str, color, darken);
            int posX = (int) currentPositionX();
            GL11.glPopMatrix();
            return posX;
        }
        return 0;
    }

    protected void renderSplitString(String str, int x, int y, int width, int color, boolean darken) {
        int n = 0;
        for (String s : splitString(str, width)) {
            renderString(s, x, y + (int) (n++ * fontHeight), color, darken);
        }
    }

    private void func_44029_a(String str, int color, boolean loop) {
        int styles = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\247' && i + 1 < str.length()) {
                char opt = str.toLowerCase().charAt(i + 1);
                int j = OPTION_CHARACTERS.indexOf(opt);
                if (j < 16) {
                    if (j < 0 || j > 15)
                        j = 15;
                    if (loop)
                        j += 16;

                    int col = colorTable[j];
                    GL11.glColor3f(
                            (float) (col >> 16 & 0xff) / 255f,
                            (float) (col >>  8 & 0xff) / 255f,
                            (float) (col       & 0xff) / 255f);
                } else {
                    FontStyle fs = FontStyle.getStyle(opt);
                    if (fs == FontStyle.DEFAULT) {
                        GL11.glColor4f(
                                (float) (color >> 16 & 0xff) / 255F,
                                (float) (color >>  8 & 0xff) / 255F,
                                (float) (color       & 0xff) / 255F,
                                (float) (color >> 24 & 0xff) / 255F);
                        styles = 0;
                    } else {
                        styles |= fs.flagBit;
                    }
                }

                i++;
                continue;
            }

            renderCharacterEx(c, styles);
        }
    }

    protected float renderCharacterEx(int c, int styles) {
        if ((styles & FontStyle.RANDOM.flagBit) == FontStyle.RANDOM.flagBit) {
            // ランダム表示するなら、幅は気にしなくても良さそう?
            int k = rand.nextInt(ChatAllowedCharacters.allowedCharacters.length());
            return renderCharacter(k);
        }

        BitmapCharacter bc = font.getCharacter(c);
        if (bc == null)
            return 0f;

        float x0 = bc.xOffset * calcedScale;
        float y0 = bc.yOffset * calcedScale - calcedBaseLine;
        float x1 = x0 + bc.width * calcedScale;
        float y1 = y0 + bc.height * calcedScale;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.texturePages[bc.page]);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(bc.s0, bc.t0);
        GL11.glVertex3f(x0, y0, 0.0F);
        GL11.glTexCoord2f(bc.s0, bc.t1);
        GL11.glVertex3f(x0, y1, 0.0F);
        GL11.glTexCoord2f(bc.s1, bc.t0);
        GL11.glVertex3f(x1, y0, 0.0F);
        GL11.glTexCoord2f(bc.s1, bc.t1);
        GL11.glVertex3f(x1, y1, 0.0F);
        GL11.glEnd();

        if ((styles & FontStyle.BOLD.flagBit) == FontStyle.BOLD.flagBit) {
            GL11.glPushMatrix();
            GL11.glTranslatef(1f, 0f, 0f);
            renderCharacter(c);
            GL11.glPopMatrix();
        }

        float translating = bc.xAdvance * calcedScale;

        if ((styles & FontStyle.STRIKE.flagBit) == FontStyle.STRIKE.flagBit) {
            Tessellator tessellator = Tessellator.instance;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.addVertex(0, (y1 - y0) / 2f, 0.0D);
            tessellator.addVertex(translating, (y1 - y0) / 2f, 0.0D);
            tessellator.addVertex(translating, (y1 - y0) / 2f - 1.0F, 0.0D);
            tessellator.addVertex(0, (y1 - y0) / 2f - 1.0F, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glTranslatef(translating, 0f, 0f);

        return translating;
    }

    protected float currentPositionX() {
        matrix.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, matrix);
        return matrix.get(12);
    }

    /**
     * 指定した文字を描画します。
     * 
     * @param i
     *            文字
     * @return 描画できたかどうか
     */
    protected float renderCharacter(int i, FontStyle style) {
        BitmapCharacter bc = font.getCharacter(i);
        if (bc == null)
            return 0f;

        float s0 = (float) bc.x / font.imgWidth;
        float t0 = (float) bc.y / font.imgHeight;
        float s1 = s0 + (float) bc.width / font.imgWidth;
        float t1 = t0 + (float) bc.height / font.imgHeight;

        float x0 = bc.xOffset * calcedScale;
        float y0 = bc.yOffset * calcedScale - calcedBaseLine;
        float x1 = x0 + bc.width * calcedScale;
        float y1 = y0 + bc.height * calcedScale;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.texturePages[bc.page]);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(s0, t0);
        GL11.glVertex3f(x0, y0, 0.0F);
        GL11.glTexCoord2f(s0, t1);
        GL11.glVertex3f(x0, y1, 0.0F);
        GL11.glTexCoord2f(s1, t0);
        GL11.glVertex3f(x1, y0, 0.0F);
        GL11.glTexCoord2f(s1, t1);
        GL11.glVertex3f(x1, y1, 0.0F);
        GL11.glEnd();

        float translating = bc.xAdvance * calcedScale;
        GL11.glTranslatef(translating, 0f, 0f);

        return translating;
    }

    public float renderCharacter(int i) {
        return renderCharacter(i, FontStyle.DEFAULT);
    }

    // 文字のスケールを更新する(FONT_HEIGHTが任意のタイミングで変更されるので描画ごとにスケールを決定する)
    private void updateScaleAndBaseLine() {
        this.fontHeight = minecraft.fontRenderer.FONT_HEIGHT * propertyScale;
        this.calcedScale = fontHeight / font.base;
        this.calcedBaseLine = (font.lineHeight - font.base) * calcedScale + propertyBaseLine;
    }

    /**
     * 日本語MODで描画できる文字列かどうかを判定します。
     */
    public boolean isDrawableString(String s) {
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (font.getCharacter(s.charAt(i)) == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
