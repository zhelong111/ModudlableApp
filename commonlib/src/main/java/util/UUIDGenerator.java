package util;

import java.util.UUID;

/**
 * Author: Bruce
 * Email: zhelong0615@gmail.com
 * Date: 2015/10/21
 */
public class UUIDGenerator {
    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        //bruce 
     /*  return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24) +
                s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);*/
        //Cindy
      return s.replaceAll("-", "");  
    }
    /**
     * 获得指定数目的UUID
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number){
        if(number < 1){
            return null;
        }
        String[] ss = new String[number];
        for(int i=0;i<number;i++){
            ss[i] = getUUID();
        }
        return ss;
    }
    
    /**
     * 将生产的UUID去掉横线和转换成大小写
     * @param str
     * @return
     */
    public static String exLowerCase(String str){
		String str2 = str.replaceAll("-", "");  
		StringBuffer sb = new StringBuffer();
		if(str!=null){
			for(int i=0;i<str2.length();i++){
				char c = str2 .charAt(i);
				if(Character.isUpperCase(c)){
					sb.append(Character.toLowerCase(c));
				}else {
					sb.append(c); 
				}
			}
		}
		
		return sb.toString();
	}
	
}