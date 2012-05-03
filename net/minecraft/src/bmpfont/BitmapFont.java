package net.minecraft.src.bmpfont;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ビットマップフォント
 */
public class BitmapFont {
    public final int lineHeight;
    public final int base;
    public final int renderedSize;
    public final int imgWidth;
    public final int imgHeight;
	public final int[] texturePages;
	public final File[] textureFiles;
	
	private Map<Integer, BitmapCharacter> characters;
	
	public BitmapFont(int lineHeight, int base, int renderedSize, int imgWidth, int imgHeight, int[] texturePages, File[] textureFiles) {
		this.lineHeight = lineHeight;
		this.base = base;
		this.renderedSize = renderedSize;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.texturePages = texturePages;
		this.textureFiles = textureFiles;
		this.characters = new HashMap<Integer, BitmapCharacter>();
	}
    
	public void addCharacter(int index, BitmapCharacter ch) {
		characters.put(index, ch);
	}
	public BitmapCharacter getCharacter(int index) {
		return characters.get(index);
	}
    
}
