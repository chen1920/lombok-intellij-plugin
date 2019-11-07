package com.chen.idea.ui.json;

/**
 *
 * @author chen
 */
public abstract class JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonValue";
    public static final int TYPE_BOOL = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_LONG = 3;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_DOUBLE = 5;
    public static final int TYPE_STRING = 6;
    public static final int TYPE_OBJECT = 7;
    public static final int TYPE_OBJECT_ARRAY = 8;
    public static final int TYPE_NUMBER = 9;
    public static final int TYPE_NULL = 10;
    public static final int TYPE_BYTE_ARRAY = 11;

    public JsonValue() {

    }

    public static JsonValue valueOf(String value) {
        return new JsonString(value);
    }

    public static JsonValue valueOf(int value) {
        return new JsonInt(value);
    }

    public static JsonValue valueOf(long value) {
        return new JsonLong(value);
    }

    public static JsonValue valueOf(float value) {
        return new JsonFloat(value);
    }

    public static JsonValue valueOf(double value) {
        return new JsonDouble(value);
    }

    public static JsonValue valueOf(boolean value) {
        return new JsonBool(value);
    }

    public abstract int getType();

    public boolean asBool() {
        return false;
    }

    public int asInt() {
        return 0;
    }

    public long asLong() {
        return 0;
    }

    public float asFloat() {
        return 0;
    }

    public double asDouble() {
        return 0;
    }

    public String asString() {
        return "";
    }

    public JsonObject asObject() {
        return null;
    }

    public JsonArray asArray() {
        return null;
    }

    public void write(StringBuilder sb, boolean encode) {

    }

    public void writeWithBlank(StringBuilder sb, char blank, int blankLen, int addBlankLen) {
        write(sb, false);
    }

    public boolean isNull() {
        return false;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder(256);
        write(sb, false);
        return sb.toString();
    }

    public final String toString(boolean encode) {
        StringBuilder sb = new StringBuilder(256);
        write(sb, encode);
        return sb.toString();
    }

    public final String toBlankString(char blank, int blankLen, int addBlankLen) {
        StringBuilder sb = new StringBuilder(256);
        writeWithBlank(sb, blank, blankLen, addBlankLen);
        return sb.toString();
    }

    public final String toBlankString() {
        return this.toBlankString(' ', 0, 4);
    }

    protected static StringBuilder fillChar(StringBuilder sb, char blank, int blankLen) {
        for (int i = 0; i < blankLen; i++)
        {
            sb.append(blank);
        }
        return sb;
    }

    public boolean isEmpty() {
        return false;
    }

    public abstract Object toObject();

}
