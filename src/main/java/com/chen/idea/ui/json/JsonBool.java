package com.chen.idea.ui.json;

/**
 *
 * @author chen
 */
public class JsonBool extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonBool";
    private boolean value;

    public JsonBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return value ? "true" : "false";
    }

    @Override
    public double asDouble() {
        return value ? 1.0 : 0;
    }

    @Override
    public float asFloat() {
        return value ? 1.0f : 0;
    }

    @Override
    public long asLong() {
        return value ? 1 : 0;
    }

    @Override
    public int asInt() {
        return value ? 1 : 0;
    }

    @Override
    public boolean asBool() {
        return value;
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append(value ? "true" : "false");
    }

    @Override
    public int getType() {
        return TYPE_BOOL;
    }

    @Override
    public Object toObject() {
        return value;
    }
}
