package net.minecraft.src.bmpfont;

import java.util.HashMap;
import java.util.Map;

public class BitmapCharacter {

	public char c;
	public int x;
	public int y;
	public int width;
	public int height;
	public int xOffset;
	public int yOffset;
	public int xAdvance;
	public int page;
    private Map<Integer, Integer> kerning = new HashMap<Integer, Integer>();
    
	public BitmapCharacter() {
	}

	public BitmapCharacter(char c) {
		this.c = c;
	}

	public BitmapCharacter(char c, int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance, int page) {
		this.c = c;
		this.x = x;
		this.y = y;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xAdvance = xAdvance;
		this.page = page;
	}
	
    public void addKerning(int second, int amount){
        kerning.put(second, amount);
    }

    public int getKerning(int second){
        Integer i = kerning.get(second);
        if (i == null)
            return 0;
        else
            return i.intValue();
    }

}
