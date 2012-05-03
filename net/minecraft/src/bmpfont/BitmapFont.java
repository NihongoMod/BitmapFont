package net.minecraft.src.bmpfont;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BitmapFont {

    public int lineHeight;
    public int base;
    public int renderedSize;
    public int imgWidth;
    public int imgHeight;
    private int pageSize;
	public int[] texturePages;
	public File[] textureFiles;
	
	private Map<Integer, BitmapCharacter> characters;
	public int size;
	
	public BitmapFont() {
		characters = new HashMap<Integer, BitmapCharacter>();
	}
    
	public void addCharacter(int index, BitmapCharacter ch) {
		characters.put(index, ch);
	}
	public BitmapCharacter getCharacter(int index) {
		return characters.get(index);
	}
    
}
