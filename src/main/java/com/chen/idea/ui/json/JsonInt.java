package com.chen.idea.ui.json;

/**
 *
 * @author chen
 */
public class JsonInt extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonInt";
    private int value;

    public JsonInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public double asDouble() {
        return value;
    }

    @Override
    public float asFloat() {
        return (float) value;
    }

    @Override
    public long asLong() {
        return value;
    }

    @Override
    public int asInt() {
        return value;
    }

    @Override
    public boolean asBool() {
        return value == 1;
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append(value);
    }

    @Override
    public int getType() {
        return TYPE_INT;
    }

    @Override
    public Object toObject() {
        return value;
    }
}
