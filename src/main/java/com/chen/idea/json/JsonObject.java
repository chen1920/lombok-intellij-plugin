package com.chen.idea.json;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author chen
 */
public class JsonObject extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonObject";
    private final HashMap<String, JsonValue> map;

    public JsonObject() {
        map = new HashMap<String, JsonValue>(16);
    }

    public Set<Map.Entry<String, JsonValue>> entrySet() {
        return map.entrySet();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public void put(String key, String[] data) {
        map.put(key, stringToArray(data));
    }

    private JsonArray stringToArray(String[] data) {
        JsonArray array = new JsonArray();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                array.add(new JsonString(data[i]));
            }
        }
        return array;
    }

    public void put(String key, String[][] data) {
        JsonArray array = new JsonArray();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                array.add(stringToArray(data[i]));
            }
        }
        map.put(key, array);
    }

    public void put(String key, int[] data) {
        JsonArray array = new JsonArray();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                array.add(new JsonInt(data[i]));
            }
        }
        map.put(key, array);
    }

    public void put(String key, long[] data) {
        JsonArray array = new JsonArray();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                array.add(new JsonLong(data[i]));
            }
        }
        map.put(key, array);
    }

    public void put(String key, Long[] data) {
        JsonArray array = new JsonArray();
        if (data != null)
        {
            for (int i = 0; i < data.length; i++)
            {
                array.add(new JsonLong(data[i]));
            }
        }
        map.put(key, array);
    }

    public void put(String key, String value) {
        //TODO    if (StringTool.isNotEmpty(value))
        {
            map.put(key, new JsonString(value));
        }
    }

    public void put(String key, long value) {
        map.put(key, new JsonLong(value));
    }

    public void put(String key, int value) {
        map.put(key, new JsonInt(value));
    }

    public void put(String key, JsonObject obj) {
        map.put(key, obj);
    }

    public void put(String key, float value) {
        //TODO       if (value != 0)
        {
            map.put(key, new JsonFloat(value));
        }
    }

    public void put(String key, double value) {
        //TODO    if (value != 0)
        {
            map.put(key, new JsonDouble(value));
        }
    }

    public void put(String key, boolean value) {
        map.put(key, new JsonBool(value));
    }

    public void put(String key, JsonValue value) {
        map.put(key, value);
    }


    public void put(String key, Object obj) {
        map.put(key, toJson(obj));
    }

    public static JsonValue toJson(Object obj) {
        JsonValue value;
        if (obj != null) {
            if (obj instanceof String){
                value = new JsonString(obj.toString());
            }else if (obj instanceof Number) {
                Number num = (Number) obj;
                if (obj instanceof Long) {
                    value = new JsonLong(num.longValue());
                } else if (obj instanceof Double) {
                    value = new JsonDouble(num.doubleValue());
                } else if (obj instanceof Float) {
                    value = new JsonFloat(num.floatValue());
                } else {
                    value = new JsonInt(num.intValue());
                }
            } else if (obj instanceof Boolean) {
                value = new JsonBool((Boolean) obj);
            } else if (obj instanceof List) {
                JsonArray array = new JsonArray();
                List<?> list = (List<?>) obj;
                for (int i = 0; i < list.size(); i++) {
                    array.add(toJson(list.get(i)));
                }
                value = array;
            } else if (obj instanceof Map) {
                JsonObject json = new JsonObject();
                Map<?, ?> dataMap = (Map<?, ?>) obj;
                Iterator<? extends Map.Entry<?, ?>> datas = dataMap.entrySet().iterator();
                while (datas.hasNext()) {
                    Map.Entry<?, ?> entry = datas.next();
                    json.put(entry.getKey().toString(), toJson(entry.getValue()));
                }
                value = json;
            } else if (obj instanceof JsonValue){
                value = (JsonValue)obj;
            } else {
                JsonObject json = new JsonObject();
                readObjValues(json, obj);
                value = json;
            }
        } else {
            value = new JsonNull();
        }
        return value;
    }

    private static void readObjValues(JsonObject json, Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getParameterTypes().length == 0) {
                String name = method.getName();
                if (name.startsWith("get") || name.startsWith("is")) {
                    try {
                        int off = name.startsWith("is") ? 2 : 3;
                        if (name.length() > off && Character.isUpperCase(name.charAt(off))) {
                            name = Character.toLowerCase(name.charAt(off)) + name.substring(off + 1);
                        } else {
                            name = name.substring(off);
                        }
                        json.put(name, method.invoke(obj, (Object[]) null));
                    } catch (Throwable e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }
    }

    public JsonValue getValue(String key) {
        if (key != null)
        {
            JsonValue value = map.get(key);
            if (value == null)
            {
                value = map.get(key.toLowerCase());
            }
            return value;
        }
        return null;
    }

    public String getString(String key) {
        JsonValue value = getValue(key);
        return value == null ? "" : value.asString();
    }

    public int getIntValue(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asInt();
    }

    public int getIntValue(String key, int defaultValue) {
        JsonValue value = getValue(key);
        return value == null ? defaultValue : value.asInt();
    }

    public boolean getBooleanValue(String key) {
        JsonValue value = getValue(key);
        return value == null ? false : value.asBool();
    }

    public long getLongValue(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asLong();
    }

    public float getFloatValue(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asFloat();
    }

    public double getDoubleValue(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asDouble();
    }

    public JsonObject getJSONObject(String key) {
        JsonValue value = getValue(key);
        if (value != null && value.getType() == TYPE_OBJECT)
        {
            return (JsonObject) value;
        }
        return null;
    }

    public JsonArray getJSONArray(String key) {
        JsonValue value = getValue(key);
        if (value != null && value.getType() == TYPE_OBJECT_ARRAY)
        {
            return (JsonArray) value;
        }
        return null;
    }

    @Override
    public void writeWithBlank(StringBuilder sb, char blank, int blankLen, int addBlankLen) {
        fillChar(sb.append("\n"), blank, blankLen).append("{\n");
        Iterator<Map.Entry<String, JsonValue>> it = map.entrySet().iterator();
        boolean first = true;
        while (it.hasNext())
        {
            Map.Entry<String, JsonValue> entry = it.next();
            JsonValue value = entry.getValue();
            if (value != null && !value.isNull())
            {
                int nblankLen = blankLen + addBlankLen;
                String key = entry.getKey();
                if (!first)
                {
                    sb.append(",\n");
                } else
                {
                    first = false;
                }
                fillChar(sb, blank, nblankLen).append('"');
                JsonString.writeString(sb, key, false);
                sb.append('"').append(" : ");
                if (value != this)
                {
                    value.writeWithBlank(sb, blank, nblankLen, addBlankLen);
                } else
                {
                    sb.append("\n");
                    fillChar(sb, blank, nblankLen).append("{\n");
                    fillChar(sb, blank, nblankLen + addBlankLen);
                    sb.append("\"$ref\" : \"@\"\n");
                    fillChar(sb, blank, nblankLen).append("}");
                }
            }

        }
        fillChar(sb.append("\n"), blank, blankLen).append("}");
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append("{");
        Iterator<Map.Entry<String, JsonValue>> it = map.entrySet().iterator();
        boolean first = true;
        while (it.hasNext())
        {
            Map.Entry<String, JsonValue> entry = it.next();
            JsonValue value = entry.getValue();
            if (value != null && !value.isNull())
            {
                String key = entry.getKey();
                if (!first)
                {
                    sb.append(",");
                } else
                {
                    first = false;
                }
                sb.append('"');
                JsonString.writeString(sb, key, false);
                sb.append('"').append(":");
                if (value != this)
                {
                    value.write(sb, encode);
                } else
                {
                    sb.append("{\"$ref\":\"@\"}");
                }
            }

        }
        sb.append("}");
    }

    public void clear() {
        map.clear();
    }

    public String readUTF(String key) {
        JsonValue value = getValue(key);
        return value == null || value.isNull() ? "" : value.asString();
    }

    public byte readByte(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : (byte) value.asInt();
    }

    public short readShort(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : (short) value.asInt();
    }

    public int readInt(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asInt();
    }

    public long readLong(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asLong();
    }

    public int[] readIntArray(String key) {
        JsonValue value = getValue(key);
        if (value != null)
        {
            if (value.getType() == TYPE_OBJECT_ARRAY)
            {
                JsonArray array = (JsonArray) value;
                ArrayList<JsonValue> datas = array.getDatas();
                int len = datas.size();
                int[] ret = new int[len];
                for (int i = 0; i < len; i++)
                {
                    ret[i] = datas.get(i).asInt();
                }
                return ret;
            }
        }
        return null;
    }

    public long[] readLongArray(String key) {
        JsonValue value = getValue(key);
        if (value != null)
        {
            if (value.getType() == TYPE_OBJECT_ARRAY)
            {
                JsonArray array = (JsonArray) value;
                ArrayList<JsonValue> datas = array.getDatas();
                int len = datas.size();
                long[] ret = new long[len];
                for (int i = 0; i < len; i++)
                {
                    ret[i] = datas.get(i).asLong();
                }
                return ret;
            }
        }
        return null;
    }

    public Long[] readLLongArray(String key) {
        JsonValue value = getValue(key);
        if (value != null)
        {
            if (value.getType() == TYPE_OBJECT_ARRAY)
            {
                JsonArray array = (JsonArray) value;
                ArrayList<JsonValue> datas = array.getDatas();
                int len = datas.size();
                Long[] ret = new Long[len];
                for (int i = 0; i < len; i++)
                {
                    ret[i] = datas.get(i).asLong();
                }
                return ret;
            }
        }
        return null;
    }

    private String[] arrayToStringArray(JsonArray array) {
        ArrayList<JsonValue> datas = array.getDatas();
        int len = datas.size();
        String[] ret = new String[len];
        for (int i = 0; i < len; i++)
        {
            ret[i] = datas.get(i).asString();
        }
        return ret;
    }


    public float readFloat(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asFloat();
    }

    public double readDouble(String key) {
        JsonValue value = getValue(key);
        return value == null ? 0 : value.asDouble();
    }

    public boolean readBoolean(String key) {
        JsonValue value = getValue(key);
        return value == null ? false : value.asBool();
    }

    @Override
    public int getType() {
        return TYPE_OBJECT;
    }

    public static JsonObject parse(String jsonStr) {
        return jsonStr == null ? null : parse(jsonStr, 0, jsonStr.length());
    }

    public static JsonObject parse(String jsonStr, int off, int end) {
        JsonParser reader = new JsonParser(jsonStr, off, end);
        return reader.parseObject();
    }

    public static JsonValue parseValue(String jsonStr) {
        return jsonStr == null ? null : parseValue(jsonStr, 0, jsonStr.length());
    }

    public static JsonValue parseValue(String jsonStr, int off, int end) {
        JsonParser reader = new JsonParser(jsonStr, off, end);
        return reader.parseValue();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public JsonObject asObject() {
        return this;
    }

    public Map<String, Object> toMap() {
        Iterator<Map.Entry<String, JsonValue>> it = map.entrySet().iterator();
        Map<String, Object> ret = new HashMap<String, Object>();
        while (it.hasNext())
        {
            Map.Entry<String, JsonValue> entry = it.next();
            JsonValue value = entry.getValue();
            if (value != null)
            {
                ret.put(entry.getKey(), value.toObject());
            }
        }
        return ret;
    }

    @Override
    public Object toObject() {
        return toMap();
    }

}
