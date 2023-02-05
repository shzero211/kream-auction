package site.shkrr.kreamAuction.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static class ResponseUtil{
        public static ResponseEntity of(String msg){
            return new ResponseEntity(MapUtil.of("msg",msg), HttpStatus.OK);
        }

        public static ResponseEntity ofException(Object msg){
            return new ResponseEntity(MapUtil.of("error_msg",msg), HttpStatus.BAD_REQUEST);
        }

    }

    public static class MapUtil {
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

}
