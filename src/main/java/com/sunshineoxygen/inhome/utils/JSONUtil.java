package com.sunshineoxygen.inhome.utils;


import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sunshineoxygen.inhome.model.DynamicBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class JSONUtil {



    public static DynamicBean convertJSONToDynamicBean(String jsonString) {
            JsonParser p = new JsonParser();
            com.google.gson.JsonObject jsonObject = p.parse(jsonString).getAsJsonObject();
            return json2DynamicBean(jsonObject);
    }


    private static DynamicBean json2DynamicBean(com.google.gson.JsonObject jsonObject) {
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

        if (entrySet == null || entrySet.size() < 1) {
            return null;
        }

        DynamicBean bean = new DynamicBean();
        DynamicBean subBean = null;
        List<DynamicBean> beanList = new ArrayList<DynamicBean>();

        for(Map.Entry<String, JsonElement> entry : entrySet) {
            JsonElement jsonElement = entry.getValue();
            String key = entry.getKey();
            if (jsonElement.isJsonArray()) {
                beanList = convertJsonArrayToDynamicBeanList(jsonElement);
                if(beanList != null && !beanList.isEmpty()) {
                    bean.addProperty(key, beanList);
                } else {
                    bean.addProperty(key,  convertJsonArrayToList(jsonElement));
                }
            } else if (jsonElement.isJsonObject()) {
                subBean = json2DynamicBean(jsonElement.getAsJsonObject());
                if (subBean != null && !subBean.isEmpty()) {
                    bean.addProperty(key, subBean);
                }
            } else if (jsonElement.isJsonPrimitive()) {
                String stringValue = jsonElement.getAsString();
                if (GenericValidator.isInt(stringValue)) {
                    bean.addProperty(key, GenericTypeValidator.formatInt(stringValue));
                } else if (GenericValidator.isBoolean(stringValue)) {
                    bean.addProperty(key, GenericTypeValidator.formatBoolean(stringValue));
                } else if (GenericValidator.isFloat(stringValue)) {
                    bean.addProperty(key, GenericTypeValidator.formatFloat(stringValue));
                } else if (GenericValidator.isDouble(stringValue)) {
                    bean.addProperty(key, GenericTypeValidator.formatDouble(stringValue));
                } else if (GenericValidator.isLong(stringValue)) {
                    bean.addProperty(key, GenericTypeValidator.formatDouble(stringValue));
                } else {
                    bean.addProperty(key, stringValue);
                }
            } else if (!jsonElement.isJsonNull()) {
                bean.addProperty(key, jsonElement.getAsString());
            }
        }
        return bean;
    }



    public static List<DynamicBean> convertJSONArrayToDynamicBeanList(String jsonString) {
        if (isJsonArray(jsonString)) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonString);
            return convertJsonArrayToDynamicBeanList(jsonElement);
        }
        return null;
    }

    public static List<DynamicBean> convertJsonArrayToDynamicBeanList(JsonElement jsonElement) {
        List<DynamicBean> listBean = null;
        JsonArray jjsonArray = jsonElement.getAsJsonArray();
        if (jjsonArray != null) {
            listBean = new ArrayList<DynamicBean>();
            for (JsonElement je: jsonElement.getAsJsonArray()) {
                if (je.isJsonObject()) {
                    listBean.add(json2DynamicBean(je.getAsJsonObject()));
                }
            }
        }
        return listBean;
    }

    public static List<String> convertJSONArrayToStringList(String jsonString) {

        if(!isJsonArray(jsonString)) return null;

        List<String> list = new ArrayList<String>();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        JsonArray jjsonArray = jsonElement.getAsJsonArray();

        if (jjsonArray != null) {
            for (int i=0;i<jjsonArray.size();i++){
                list.add(jjsonArray.get(i).getAsString());
            }
        }

        return list;
    }

    /**
     * @deprecated use {@link #convertListToJsonString(List)} instead.
     */
    @Deprecated
    public static JSONArray convertDynamicBeanListToJSONArray(List<DynamicBean> beanList){
        JSONArray json = new JSONArray();
        for(DynamicBean bean: beanList) {
            json.put(bean.getProperties());
        }
        return json;
    }


    /**
     * @deprecated use {@link #convertDynamicBeanToJsonString(DynamicBean)} instead.
     */
    @Deprecated
    public static String convertDynamicBeanToJSONString(DynamicBean bean) {
        if(bean==null) return null;
        JSONObject jsonObject = new JSONObject(bean.getProperties());
        return jsonObject.toString();
    }

    public static boolean isValidJSON(String test) {
        try {
            JsonElement jsonElement = new JsonParser().parse(test);
            return jsonElement.isJsonObject()||jsonElement.isJsonArray();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    public static boolean isJsonArray(String test) {
        try {
            JsonElement jsonElement = new JsonParser().parse(test);
            return jsonElement.isJsonArray();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    @Deprecated
    private static Map toMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = jsonObject.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = jsonObject.get(key);

            if(value instanceof JSONArray) {
                value = convertJsonArrayToList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<?> convertJsonArrayToList(JsonElement jsonElement){
        List<Object> list = new ArrayList<Object>();
        for (JsonElement je: jsonElement.getAsJsonArray()) {
            if (je.isJsonArray()) {
                list.add(convertJsonArrayToList(je));
            } else if (je.isJsonObject()) {
                list.add(json2DynamicBean(je.getAsJsonObject()));
            } else if (!je.isJsonNull()) {
                list.add(je.getAsString());
            }
        }
        return list;
    }

    /**
     * @deprecated use {@link #convertJsonArrayToList(JsonElement)} instead.
     */
    public static List convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if(value instanceof JSONArray) {
                value = convertJsonArrayToList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


    public static <T> T fromJsonFile(String path, Type type) {

        Gson gson = new Gson();
        JsonReader reader;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            reader = new JsonReader(fileReader);
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileReader != null) fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T fromJsonString(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    public static String toJsonString(Object object, Type type, boolean isPrettyPrint) {
        Gson gson = null;
        if (isPrettyPrint) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            gson = new GsonBuilder().create();
        }
        return gson.toJson(object, type);
    }

    public static String toJsonString(Object object, Type type) {
        return toJsonString(object, type, false);
    }

    public static void toJsonFile(String path, Object object, Type type) {
        String json = toPrettyFormat(toJsonString(object, type));
        FileUtil.writeToFile(path, json);
    }

    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
        String prettyJson = null;
        if (jsonElement.isJsonArray()) {
            JsonArray json = jsonElement.getAsJsonArray();
            prettyJson = gson.toJson(json);
        } else {
            com.google.gson.JsonObject json = jsonElement.getAsJsonObject();
            prettyJson = gson.toJson(json);
        }
        return prettyJson;
    }

    public static String toPrettyFormatForHtml(String jsonString) {
        String output = toPrettyFormat(jsonString);
        output = TagUtils.replaceSpecialCharacters(output);
        output = output.replace(" ", "&nbsp;");
        return output;
    }

    public static String convertPojoToJSON(Object obj)  {
        if (obj == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e){
            System.err.println("Error on JSONUtil.convertPojoToJSON, obj class:"+obj.getClass().getName());
            return null;
        }

    }

    public static <T> T convertJsonToPojo(String jsonText, TypeReference<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return (T) mapper.readValue(jsonText, type);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDynamicBeanToJsonString(DynamicBean bean) {
        Map<String,Object> map = convertDynamicBeanToMap(bean);
        return toPrettyFormat(toJsonString(map, new TypeToken<Map<String,Object>>(){}.getType()));
    }

    public static String convertListToJsonString(List<?> list, boolean prettyPrint) {
        List<Object> convertedList = new ArrayList<Object>();
        for (int i = 0;  i < list.size(); i++) {
            if (list.get(i).getClass().isAssignableFrom(DynamicBean.class)) {
                convertedList.add(convertDynamicBeanToMap((DynamicBean)list.get(i)));
            } else {
                convertedList.add(list.get(i));
            }
        }
        return toJsonString(convertedList, new TypeToken<ArrayList<Object>>(){}.getType(), prettyPrint);
    }

    public static String convertListToJsonString(List<?> list) {
        return convertListToJsonString(list, true);
    }

    private static Map<String, Object> convertDynamicBeanToMap(DynamicBean bean) {
        Map<String,Object> map = new HashMap<String,Object>();
        for (String key: bean.getProperties().keySet()) {
            if (bean.get(key) == null) continue;
            else if (bean.getProperty(key).getClass().isAssignableFrom(DynamicBean.class)) {
                map.put(key, convertDynamicBeanToMap((DynamicBean)bean.getProperty(key)));
            } else if (bean.getProperty(key).getClass().isAssignableFrom(ArrayList.class)) {
                List<Object> list = (List<Object>) bean.getProperty(key);
                List<Object> convertedList = new ArrayList<Object>();
                for (int i = 0;  i < list.size(); i++) {
                    if (list.get(i).getClass().isAssignableFrom(DynamicBean.class)) {
                        convertedList.add(convertDynamicBeanToMap((DynamicBean)list.get(i)));
                    } else {
                        convertedList.add(list.get(i));
                    }
                }
                map.put(key, convertedList);
            } else {
                map.put(key, bean.getProperty(key));
            }
        }
        return map;
    }

    public static void convertDynamicBeanListToJsonFile(String path, List<DynamicBean> list) {
        FileUtil.writeToFile(path, convertListToJsonString(list));
    }
}
