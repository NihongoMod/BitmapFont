package net.minecraft.src.bmpfont;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.minecraft.src.RenderEngine;

/*
 * jMonkeyEngineの同名のクラスを参考にしました。
 * thanks!
 */
public class BitmapFontLoader {
	public BitmapFont load(File fontFile) throws IOException {
        BitmapFont font = new BitmapFont();

        BufferedReader reader = new BufferedReader(new FileReader(fontFile));
        String regex = "[\\s=]+";

        while (reader.ready()){
            String line = reader.readLine();
            String[] tokens = line.split(regex);
            if (tokens[0].equals("info")){
                // Get rendered size
                for (int i = 1; i < tokens.length; i++){
                    if (tokens[i].equals("size")){
                        font.renderedSize = Integer.parseInt(tokens[i + 1]);
                    }
                }
            }else if (tokens[0].equals("common")){
                // Fill out BitmapCharacterSet fields
                for (int i = 1; i < tokens.length; i++){
                    String token = tokens[i];
                    if (token.equals("lineHeight")){
                    	font.lineHeight = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("base")){
                    	font.base = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("scaleW")){
                    	font.imgWidth = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("scaleH")){
                    	font.imgHeight = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("pages")){
                        // number of texture pages
                        font.textureFiles = new File[Integer.parseInt(tokens[i + 1])];
                        font.texturePages = new int[Integer.parseInt(tokens[i + 1])];
                    }
                }
            }else if (tokens[0].equals("page")){
                int index = -1;
                File file = null;

                for (int i = 1; i < tokens.length; i++){
                    String token = tokens[i];
                    if (token.equals("id")){
                        index = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("file")){
                        String fn = tokens[i + 1];
                        if (fn.startsWith("\"")){
                            fn = fn.substring(1, fn.length()-1);
                        }

                        file = new File(fontFile.getParentFile(), fn);
                    }
                }
                // set page
                if (index >= 0 && file != null){
                	font.textureFiles[index] = file;
                }
            }else if (tokens[0].equals("char")){
                // New BitmapCharacter
                BitmapCharacter ch = null;
                for (int i = 1; i < tokens.length; i++){
                    String token = tokens[i];
                    if (token.equals("id")){
                        int index = Integer.parseInt(tokens[i + 1]);
                        ch = new BitmapCharacter((char)index);
                        font.addCharacter(index, ch);
                    }else if (token.equals("x")){
                        ch.x = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("y")){
                        ch.y = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("width")){
                        ch.width = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("height")){
                        ch.height = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("xoffset")){
                        ch.xOffset = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("yoffset")){
                        ch.yOffset = Integer.parseInt(tokens[i + 1]);
                    }else if (token.equals("xadvance")){
                        ch.xAdvance = Integer.parseInt(tokens[i + 1]);
                    } else if (token.equals("page")) {
                        ch.page = Integer.parseInt(tokens[i + 1]);
                    }
                }
            }else if (tokens[0].equals("kerning")){
                // Build kerning list
                int index = 0;
                int second = 0;
                int amount = 0;

                for (int i = 1; i < tokens.length; i++){
                    if (tokens[i].equals("first")){
                        index = Integer.parseInt(tokens[i + 1]);
                    }else if (tokens[i].equals("second")){
                        second = Integer.parseInt(tokens[i + 1]);
                    }else if (tokens[i].equals("amount")){
                        amount = Integer.parseInt(tokens[i + 1]);
                    }
                }

                BitmapCharacter ch = font.getCharacter(index);
                ch.addKerning(second, amount);
            }
        }
        return font;
    }
}
