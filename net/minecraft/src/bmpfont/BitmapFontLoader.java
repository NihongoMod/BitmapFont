package net.minecraft.src.bmpfont;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/*
 * jMonkeyEngineの同名のクラスを参考にしました。
 * thanks!
 */
public class BitmapFontLoader {
	/**
	 * 指定したファイルからビットマップフォントをロードします。
	 * 
	 * @param fontFile フォントファイル
	 * @return ビットマップフォント
	 * @throws IOException 読み込みに失敗した場合
	 */
	public BitmapFont load(File fontFile) throws IOException {
        return loadCharacters(fontFile, loadFont(fontFile));
    }
	
	/**
	 * フォント情報の読み込み
	 * 
	 * @return
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	private BitmapFont loadFont(File fontFile) throws NumberFormatException, IOException {
	    int lineHeight = -1, base = -1, renderedSize = -1, imgWidth = -1, imgHeight = -1;//, pageSize = -1;
		int[] texturePages = null;
		File[] textureFiles = null;

        BufferedReader br = new BufferedReader(new FileReader(fontFile));
        String regex = "[\\s]+";

        Properties p = new Properties();
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(regex);
            
            p.clear();
            for (String tk : tokens) {
            	String[] pair = tk.split("=");
            	if (pair.length == 2)
            		p.setProperty(pair[0], pair[1]);
            }
            
            if (tokens[0].equals("info")) {
                renderedSize = Integer.parseInt(p.getProperty("size"));
            }
            
            else if (tokens[0].equals("common")){
            	lineHeight = Integer.parseInt(p.getProperty("lineHeight"));
            	base = Integer.parseInt(p.getProperty("base"));
            	imgWidth = Integer.parseInt(p.getProperty("scaleW"));
            	imgHeight = Integer.parseInt(p.getProperty("scaleH"));
            	textureFiles = new File[Integer.parseInt(p.getProperty("pages"))];
                texturePages = new int[Integer.parseInt(p.getProperty("pages"))];
            }
            
            else if (tokens[0].equals("page")){
                int index = Integer.parseInt(p.getProperty("id"));
                String fn = p.getProperty("file");
                if (fn.startsWith("\""))
                    fn = fn.substring(1, fn.length() - 1);
                File file = new File(fontFile.getParentFile(), fn);
                if (index >= 0 && file != null)
                	textureFiles[index] = file;
            }
        }
        
        return new BitmapFont(lineHeight, base, renderedSize, imgWidth, imgHeight, texturePages, textureFiles);
	}
	
	/**
	 * 文字情報の読み込み
	 * 
	 * @param fontFile
	 * @param font
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	private BitmapFont loadCharacters(File fontFile, BitmapFont font) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fontFile));
        String regex = "[\\s]+";

        Properties p = new Properties();
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(regex);
            
            p.clear();
            for (String tk : tokens) {
            	String[] pair = tk.split("=");
            	if (pair.length == 2)
            		p.setProperty(pair[0], pair[1]);
            }
            
            if (tokens[0].equals("char")){
            	int id		= Integer.parseInt(p.getProperty("id"));
            	int x		= Integer.parseInt(p.getProperty("x"));
            	int y		= Integer.parseInt(p.getProperty("y"));
            	int width	= Integer.parseInt(p.getProperty("width"));
            	int height	= Integer.parseInt(p.getProperty("height"));
            	int xOffset	= Integer.parseInt(p.getProperty("xoffset"));
            	int yOffset	= Integer.parseInt(p.getProperty("yoffset"));
            	int xAdvance= Integer.parseInt(p.getProperty("xadvance"));
            	int page	= Integer.parseInt(p.getProperty("page"));
            	font.addCharacter(id, new BitmapCharacter(font, (char)id, x, y, width, height, xOffset, yOffset, xAdvance, page));
            }
            
            else if (tokens[0].equals("kerning")){
                int index = Integer.parseInt(p.getProperty("first"));
                int second = Integer.parseInt(p.getProperty("second"));
                int amount = Integer.parseInt(p.getProperty("amount"));
                BitmapCharacter ch = font.getCharacter(index);
                ch.addKerning(second, amount);
            }
        }
        
        return font;
	}
}
