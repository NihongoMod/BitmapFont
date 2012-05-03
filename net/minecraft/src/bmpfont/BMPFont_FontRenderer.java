package net.minecraft.src.bmpfont;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.RenderEngine;

/**
 * 標準のFontRendererをオーバライドし、描画をBitmapFontRendererに委譲するためのクラス
 * 
 * @author wiro
 */
public class BMPFont_FontRenderer extends FontRenderer {
	private BitmapFontRenderer renderer;

	private boolean enableMod = true;

	public void setEnableMod(boolean enableMod) {
		this.enableMod = enableMod;
	}
	
	public boolean isEnableMod() {
		return enableMod;
	}
	
	public BMPFont_FontRenderer(BitmapFontRenderer renderer, GameSettings gamesettings, String s, final RenderEngine renderengine, boolean flag) {
		super(gamesettings, s, renderengine, flag);
		this.renderer = renderer;
	}

	@Override
	public int drawStringWithShadow(String par1Str, int par2, int par3, int par4) {
		if (enableMod)
			return renderer.drawStringWithShadow(par1Str, par2, par3, par4);
		return super.drawStringWithShadow(par1Str, par2, par3, par4);
	}

	@Override
	public void drawString(String par1Str, int par2, int par3, int par4) {
		if (enableMod)
			renderer.drawString(par1Str, par2, par3, par4);
		else
			super.drawString(par1Str, par2, par3, par4);
	}

	@Override
	public int func_50101_a(String par1Str, int par2, int par3, int par4, boolean par5) {
		if (enableMod)
			return renderer.renderString(par1Str, par2, par3, par4, par5);
		return super.func_50101_a(par1Str, par2, par3, par4, par5);
	}

	@Override
	public int getStringWidth(String par1Str) {
		if (enableMod)
			return renderer.getStringWidth(par1Str);
		return super.getStringWidth(par1Str);
	}

	@Override
	public int func_50105_a(char par1) {
		if (enableMod)
			return (int)renderer.getCharacterWidth(par1);
		return super.func_50105_a(par1);
	}

	/*
	@Override
	public String func_50107_a(String par1Str, int par2) {
		// TODO Auto-generated method stub
		return super.func_50107_a(par1Str, par2);
	}

	@Override
	public String func_50104_a(String par1Str, int par2, boolean par3) {
		// TODO Auto-generated method stub
		return super.func_50104_a(par1Str, par2, par3);
	}
	*/

	@Override
	public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5) {
		if (enableMod)
			renderer.drawSplitString(par1Str, par2, par3, par4, par5);
		else
			super.drawSplitString(par1Str, par2, par3, par4, par5);
	}

	@Override
	public int splitStringWidth(String par1Str, int par2) {
		if (enableMod)
			return renderer.getSplitStringHeight(par1Str, par2);
		return super.splitStringWidth(par1Str, par2);
	}

	/*
	@Override
	public void setUnicodeFlag(boolean par1) {
		// TODO Auto-generated method stub
		super.setUnicodeFlag(par1);
	}

	@Override
	public void setBidiFlag(boolean par1) {
		// TODO Auto-generated method stub
		super.setBidiFlag(par1);
	}

	@Override
	public List func_50108_c(String par1Str, int par2) {
		// TODO Auto-generated method stub
		return super.func_50108_c(par1Str, par2);
	}
	*/
}
