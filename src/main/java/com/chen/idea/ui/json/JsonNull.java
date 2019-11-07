package com.chen.idea.ui.json;

/**
 *
 * @author chen
 */
public class JsonNull extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonNull";

    public JsonNull() {
    }

    @Override
    public String asString() {
        return "null";
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public boolean asBool() {
        return false;
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append("null");
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getType() {
        return TYPE_NULL;
    }

    @Override
    public Object toObject() {
       return null;
    }
}
