package com.bishe.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author robin
 * @date 2015-4-20 
 * Copyright 2015 The robin . All rights reserved
 */
public class StringUtils {
	/**
	 * 检验邮箱格式是否正确
	 * @param target
	 * @return
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
	
	/**
	 * 根据文件得到字节流
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getFileByte(File file) {
		if (!file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			int len = fis.available();
			byte[] bytes = new byte[len];
			fis.read(bytes);
			fis.close();
			return bytes;
		} catch (Exception e) {

		}

		return null;
	}
	
	/**
	 * 将输入流中的数据全部读取出来, 一次性返回
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] load(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) != -1)
			baos.write(buffer, 0, len);
		baos.close();
		is.close();
		return baos.toByteArray();
	}
	
	/**
	 * 将指定byte数组转换成16进制大写字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}
	
	/**
	 * 大数组（String）获取相同元素 大致思路是:1.首先将两个数组A、B排序(递增)<br>
	 * 2.分别从A和B中各取出一元素a,b，对a和b进行比 较：<br>
	 * 1) 如果a与b相等，则将a或b存入一指定集合中<br>
	 * 2)如果a小于b，则继续取A的下一元素，再与b比 较<br>
	 * 3) 如果a大于b，则取B的下一个元素，与a进行比较<br>
	 * 3.反复进行步骤2，知道A或B的元素都比较完<br>
	 * 4.返回集合(存了相同的元素)<br>
	 * 
	 * @param strArr1
	 * @param strArr2
	 * @return
	 */
	public static List<String> getAllSameElement2(String[] strArr1,
			String[] strArr2) {
		if (strArr1 == null || strArr2 == null) {
			return null;
		}
		Arrays.sort(strArr1);
		Arrays.sort(strArr2);
		List<String> list = new ArrayList<String>();
		int k = 0;
		int j = 0;
		while (k < strArr1.length && j < strArr2.length) {
			if (strArr1[k].compareTo(strArr2[j]) == 0) {
				if (strArr1[k].equals(strArr2[j])) {
					list.add(strArr1[k]);
					k++;
					j++;
				}
				continue;
			} else if (strArr1[k].compareTo(strArr2[j]) < 0) {
				k++;
			} else {
				j++;
			}
		}
		return list;
	}
	

	/**
	 *  MD5変換
	 * */
	public static String Md5(String str) {
		if (str != null && !str.equals("")) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'a', 'b', 'c', 'd', 'e', 'f' };
				byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < md5Byte.length; i++) {
					sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
					sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
				}
				str = sb.toString();
			} catch (NoSuchAlgorithmException e) {

			} catch (Exception e) {
			}
		}
		return str;
	}

}
