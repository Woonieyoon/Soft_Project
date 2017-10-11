package myhome.bookknu;

/**
 * Created by sungw on 2017-09-08.
 */

public class SoundSearcher
{
    private static final char HANGUL_BEGIN_UNICODE = 44032; // °¡
    private static final char HANGUL_LAST_UNICODE = 55203; // ÆR
    private static final char HANGUL_BASE_UNIT = 588;//°¢ÀÚÀ½ ¸¶´Ù °¡Áö´Â ±ÛÀÚ¼ö
    //ÀÚÀ½
    private static final char[] INITIAL_SOUND = { '¤¡', '¤¢', '¤¤', '¤§', '¤¨', '¤©', '¤±', '¤²', '¤³', '¤µ', '¤¶', '¤·', '¤¸', '¤¹', '¤º', '¤»', '¤¼', '¤½', '¤¾' };


  
     //ÇØ´ç ¹®ÀÚ°¡ INITIAL_SOUNDÀÎÁö °Ë»ç.
   
    private static boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }

    //ÀÚÀ½
    private static char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    //ÇÑ±Û
    private static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }

    
    public SoundSearcher() { }

  
    public static boolean matchString(String value, String search){
        int t = 0;
        int seof = value.length() - search.length();
        int slen = search.length();
        if(seof < 0)
            return false; //°Ë»ö¾î°¡ ´õ ±æ¸é false¸¦ ¸®ÅÏÇÑ´Ù.
        for(int i = 0;i <= seof;i++){
            t = 0;
            while(t < slen){
                if(isInitialSound(search.charAt(t))==true && isHangul(value.charAt(i+t))){
                    //¸¸¾à ÇöÀç charÀÌ ÃÊ¼ºÀÌ°í value°¡ ÇÑ±ÛÀÌ¸é
                    if(getInitialSound(value.charAt(i+t))==search.charAt(t))
                        //°¢°¢ÀÇ ÃÊ¼º³¢¸® °°ÀºÁö ºñ±³ÇÑ´Ù
                        t++;
                    else
                        break;
                } else {
                    //charÀÌ ÃÊ¼ºÀÌ ¾Æ´Ï¶ó¸é
                    if(value.charAt(i+t)==search.charAt(t))
                        //±×³É °°ÀºÁö ºñ±³ÇÑ´Ù.
                        t++;
                    else
                        break;
                }
            }
            if(t == slen)
                return true; //¸ðµÎ ÀÏÄ¡ÇÑ °á°ú¸¦ Ã£À¸¸é true¸¦ ¸®ÅÏÇÑ´Ù.
        }
        return false; //ÀÏÄ¡ÇÏ´Â °ÍÀ» Ã£Áö ¸øÇßÀ¸¸é false¸¦ ¸®ÅÏÇÑ´Ù.
    }
}