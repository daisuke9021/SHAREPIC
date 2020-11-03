package com.sharepic.util;

import java.util.ResourceBundle;

public class PropertyUtils {

	public static ResourceBundle getProperties(String fileName) {
		return ResourceBundle.getBundle(fileName);
	}

}
