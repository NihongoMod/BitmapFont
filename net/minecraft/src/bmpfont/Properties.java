package net.minecraft.src.bmpfont;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.src.mod_BitmapFont;

/**
 * プロパティファイルを簡単に扱うためのクラス
 * 
 * @author wiro
 */
public class Properties {
	
	private File propFile;	
	
	public Properties(File propFile) {
		load(propFile);
	}
	
	private void load(File propFile) {
		this.propFile = propFile;
		
		java.util.Properties p = new java.util.Properties();
		if (propFile.exists()) {
			try {
				p.load(new FileReader(propFile));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		boolean needInitialize = false;
		for (Field f : this.getClass().getFields()) {
			if (Modifier.isPublic(f.getModifiers())) {
				if (p.containsKey(f.getName())) {
					parseField(p, f);
				} else {
					needInitialize = true;
				}
			}
		}

		if (needInitialize) {
			save();
		}
	}
	
	private void parseField(java.util.Properties p, Field field) {
		try {
			String value = (field.get(this) != null) ? 
					p.getProperty(field.getName(), field.get(this).toString()) :
						p.getProperty(field.getName());
			Class<?> type = field.getType();
			if (type      == boolean.class)	field.setBoolean(this, value.equals("1"));
			else if (type == byte.class)	field.setByte	(this, Byte.parseByte(value));
			else if (type == char.class)	field.setChar	(this, value.charAt(0));	// TODO: value.length() == 0 だと例外でる
			else if (type == double.class)	field.setDouble	(this, Double.parseDouble(value));
			else if (type == float.class)	field.setFloat	(this, Float.parseFloat(value));
			else if (type == int.class)		field.setInt	(this, Integer.parseInt(value));
			else if (type == long.class)	field.setLong	(this, Long.parseLong(value));
			else if (type == short.class)	field.setShort	(this, Short.parseShort(value));
			else if (type == String.class)	field.set		(this, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			PrintWriter pw = new PrintWriter(propFile);
			pw.println("# MOD設定ファイル（このファイルはUTF-8で保存してください）");
			
			Class<?> cls = this.getClass();
			
			Field[] fields = cls.getFields();
			Arrays.sort(fields, new Comparator<Field>() {
				@Override
				public int compare(Field o1, Field o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			
			for (Field f : fields) {
				try {
					if (f.getType() == Boolean.class || f.getType() == boolean.class)
						pw.println(f.getName() + "=" + ((f.getBoolean(this)) ? '1' : '0'));
					else
						pw.println(f.getName() + "=" + f.get(this).toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			pw.flush();
			pw.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
