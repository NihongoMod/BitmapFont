package net.minecraft.src.bmpfont;

import java.io.File;

/**
 * BitmapFontMODの設定
 * 
 * @author wiro
 */
public class BitmapFontProperties extends net.minecraft.src.bmpfont.Properties {
    /** 文字を表示する際のスケール */
    public double scale = 1.25;

    /** 文字表示位置のベースライン */
    public double baseLine = 0;

    /** フォントファイル(.fnt)のパス */
    public String fontFile = "default/default.fnt";

    /** フォントの置き換えをするかどうか */
    public boolean enableMod = true;

    public BitmapFontProperties(File propFile) {
        super(propFile);
    }
}
