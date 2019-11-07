package com.chen.idea.ui.json;

/**
 *
 * @author chen
 */
public class JsonLong extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonLong";
    private long value;

    public JsonLong(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public double asDouble() {
        return (double) value;
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
        return (int) value;
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
        return TYPE_LONG;
    }

    @Override
    public Object toObject() {
        return value;
    }

}
