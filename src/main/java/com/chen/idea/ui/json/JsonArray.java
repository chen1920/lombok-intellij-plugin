package com.chen.idea.ui.json;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chen
 */
public class JsonArray extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonArray";
    private final ArrayList<JsonValue> datas;

    public JsonArray(int initSize) {
        datas = new ArrayList<JsonValue>(initSize);
    }

    public JsonArray() {
        datas = new ArrayList<JsonValue>(16);
    }

    public ArrayList<JsonValue> getDatas() {
        return datas;
    }

    public void add(JsonValue value) {
        if (value != null)
        {
            datas.add(value);
        }
    }

    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append("[");
        ArrayList<JsonValue> list = datas;
        int end = list.size();
        for (int i = 0; i < end; i++)
        {
            if (i > 0)
            {
                sb.append(",");
            }
            JsonValue value = list.get(i);
            value.write(sb, encode);
        }
        sb.append("]");
    }

    @Override
    public void writeWithBlank(StringBuilder sb, char blank, int blankLen, int addBlankLen) {
        fillChar(sb.append("\n"), blank, blankLen).append("[");
        ArrayList<JsonValue> list = datas;
        int end = list.size();
        for (int i = 0; i < end; i++)
        {
            if (i > 0)
            {
                sb.append(",");
            }
            fillChar(sb, blank, blankLen);
            JsonValue obj = list.get(i);
            int type = obj.getType();
            int nblankLen = blankLen + addBlankLen;
            if (type == TYPE_OBJECT || type == TYPE_OBJECT_ARRAY)
            {
                if (obj != this)
                {
                    obj.writeWithBlank(sb, blank, nblankLen, addBlankLen);
                } else
                {
                    sb.append("\n");
                    fillChar(sb, blank, nblankLen).append("{\n");
                    fillChar(sb, blank, nblankLen + addBlankLen);
                    sb.append("\"$ref\" : \"@\"\n");
                    fillChar(sb, blank, nblankLen).append("}");
                }
            } else
            {
                fillChar(sb.append("\n"), blank, nblankLen);
                obj.write(sb, false);
            }
        }
        fillChar(sb.append("\n"), blank, blankLen).append("]");
    }

    public int size() {
        return datas.size();
    }

    @Override
    public int getType() {
        return TYPE_OBJECT_ARRAY;
    }

    @Override
    public boolean isEmpty() {
        return datas.isEmpty();
    }

    public List<Object> toList() {
        List<Object> ret = new ArrayList<Object>();
        ArrayList<JsonValue> list = datas;
        int end = list.size();
        for (int i = 0; i < end; i++)
        {
            JsonValue value = list.get(i);
            if (value != null)
            {
                ret.add(value.toObject());
            }
        }
        return ret;
    }

    @Override
    public Object toObject() {
        return toList();
    }

    public JsonValue get(int index) {
        if (index >= 0 && index < datas.size())
        {
            return datas.get(index);
        }
        return null;
    }

    public JsonArray getArray(int index) {
        if (index >= 0 && index < datas.size())
        {
            JsonValue value = datas.get(index);
            return value == null ? null : value.asArray();
        }
        return null;
    }
}
