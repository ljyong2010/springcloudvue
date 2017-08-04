package yf.liu.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;
import sun.security.util.ManifestEntryVerifier;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class FastJsonUtils {
    public static final String SUCCESS_MSG = "数据加载成功";

    private static SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.DisableCircularReferenceDetect
    };

    /**
     * 生成json返回结果
     * @param code
     * @param msg
     * @param data
     * @return
     */
     public static String resultSuccess(Integer code,String msg,Object data){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("msg", StringUtils.isNotEmpty(msg)?msg:SUCCESS_MSG);
         rs.put("data",data==null ? new Object():data);
         rs.put("error","");
         return toString(rs);
     }
     public static String resultError(Integer code,String error,Object data){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("data",data == null ?new Object():data);
         rs.put("error",StringUtils.isNotEmpty(error)?error:"");
         return toString(rs);
     }

    /**
     * 生成json返回结果
     * @param code
     * @param msg
     * @param pageNo
     * @param pageSize
     * @param data
     * @return
     */
     public static String resultList(Integer code,String msg,Integer pageNo,Integer pageSize,Object data){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("msg",StringUtils.isNotEmpty(msg)?msg:SUCCESS_MSG);
         rs.put("data",data == null ? new Object():data);
         rs.put("pageNo",pageNo == null ? 0: pageNo);
         rs.put("pageSize",pageSize == null ? 10:pageSize);
         return toString(rs);
     }

    /**
     * 生成json返回结果
     * @param code
     * @param msg
     * @param data
     * @param features
     * @return
     */
     public static String resultFeatures(Integer code,String msg,Object data,SerializerFeature... features){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("msg",StringUtils.isNotEmpty(msg)?msg:SUCCESS_MSG);
         rs.put("data",data == null ? new Object() : data);
         return JSON.toJSONString(rs,features);
     }

    /**
     * 生成json返回结果
     * @param code
     * @param msg
     * @param data
     * @param dateFormat
     * @return
     */
     public static String resultDate(Integer code,String msg,Object data,String dateFormat){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("msg",StringUtils.isNotEmpty(msg)?msg:SUCCESS_MSG);
         rs.put("data",data == null ? new Object():data);
         return JSON.toJSONStringWithDateFormat(rs,dateFormat,features);
     }

    /**
     * 生成json返回结果,包含字段
     * @param code
     * @param msg
     * @param data
     * @param properties
     * @return
     */
     public static String resultIncludes(Integer code,String msg,Object data,String ...properties){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("msg",StringUtils.isNotEmpty(msg)?msg:SUCCESS_MSG);
         rs.put("data",data==null?new Object(): data);
         return toStringIncludes(rs,properties);
     }

    /**
     * 生成json返回结果,排除字段
     * @param code
     * @param msg
     * @param data
     * @param type
     * @param properties
     * @return
     */
     public static String resultExcludes(Integer code,String msg,Object data,Class<?> type,String ...properties){
         Map<String,Object> rs = new HashMap<>();
         rs.put("code",code);
         rs.put("msg",StringUtils.isNotEmpty(msg)?msg:SUCCESS_MSG);
         rs.put("data",data == null?new Object():data);
         return toStringExcludes(rs,type,properties);
     }
     public static String toString(Object data){
         return JSON.toJSONString(data);
     }
     private static String toStringIncludes(Object data,String ...properties){
         PropertyFilter filter = new PropertyFilter() {
             @Override
             public boolean apply(Object object, String name, Object value) {
                 if (object.getClass() == HashMap.class && ("code".equals(name) || "data".equals(name)||"msg".equals(name))){
                     return true;
                 }
                 for (String pro:properties){
                     if (pro.equals(name))
                         return true;
                 }
                 return false;
             }
         };
         return JSON.toJSONString(data,filter,features);
     }
     private static String toStringExcludes(Object data,Class<?> type,String ...properties){
         PropertyFilter filter = new PropertyFilter() {
             @Override
             public boolean apply(Object object, String name, Object value) {
                 if (object.getClass() == type){
                     for (String pro : properties){
                         if (pro.equals(name)){
                             return false;
                         }
                     }
                 }
                 return true;
             }
         };
         return JSON.toJSONString(data,filter,features);
     }
}
