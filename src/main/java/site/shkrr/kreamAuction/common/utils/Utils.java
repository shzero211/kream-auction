package site.shkrr.kreamAuction.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import site.shkrr.kreamAuction.common.config.AppConfig;
import site.shkrr.kreamAuction.exception.user.JsonToMapException;
import site.shkrr.kreamAuction.exception.user.MapToJsonException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {
    private static ObjectMapper getObjectMapper(){
        return (ObjectMapper) AppConfig.getContext().getBean("objectMapper");
    }

    /*
    * ResponseUtil
    * */
    public static class response{
        public static ResponseEntity of(String msg){
            return new ResponseEntity(map.of("msg",msg), HttpStatus.OK);
        }

        public static ResponseEntity of(String msg,Map<String,String> tokenMap){
            HttpHeaders headers=new HttpHeaders();
            for(String key: tokenMap.keySet()){
                headers.add(key,tokenMap.get(key));
            }
            return new ResponseEntity(map.of("msg",msg),headers,HttpStatus.OK);
        }

        public static ResponseEntity ofException(Object msg){
            return new ResponseEntity(map.of("error_msg",msg), HttpStatus.BAD_REQUEST);
        }

        public static ResponseEntity ofExceptionForAPI(Object msg){
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * MapUtil
     * */
    public static class map{
        /*
         * 받는 객체 타입에 따른 Map 생성 메서드
         * */
        public static <K,V> Map<K,V> of(Object... args){
            HashMap<K,V> map=new HashMap<>();
            for(int i=0;i<args.length/2;i++){
                int keyIdx=i*2;
                int valueIdx=keyIdx+1;
                K key=(K)args[keyIdx];
                V value=(V) args[valueIdx];
                map.put(key,value);
            }
            return map;
        }
    }

    /*
     * RandomNumberUtil
     * */
    public static class random{
        public static String makeRandomNum(){
            Random random=new Random();
            return String.valueOf(100000 + random.nextInt(900000));
        }

        public static String makeRandomKey(){
            int leftLimit=48;//'0'
            int rightLimit=122;
            int keyLength=10;
            Random random=new Random();
            String randomKey=random.ints(leftLimit,rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(keyLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            return randomKey;
        }
    }

    public static class json{
        public static String toJson(Object obj){
            try {
                return getObjectMapper().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new MapToJsonException("객체를 Json 으로 변경 실패");
            }
        }

        public static <T> T toObj(String jsonStr,Class<T> classType){
            try {
                return getObjectMapper().readValue(jsonStr,classType);
            } catch (JsonProcessingException e) {
                throw new JsonToMapException(e.toString());
            }
        }
    }

}
