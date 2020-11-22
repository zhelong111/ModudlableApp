package util;

import java.io.IOException;
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64 {
	/**  
	* 编码  
	* @param bstr  
	* @return String  
	*/    
	public static String encode(byte[] bstr){    
		return new BASE64Encoder().encode(bstr);
	}    

	/**  
	* 解码  
	* @param str  
	* @return string  
	*/    
	public static byte[] decode(String str) {    
		byte[] bt = null;    
		try {    
		   BASE64Decoder decoder = new BASE64Decoder();
		   bt = decoder.decodeBuffer( str );    
		} catch (IOException e) {    
		   e.printStackTrace();    
		}
		return bt;    
	}
}
