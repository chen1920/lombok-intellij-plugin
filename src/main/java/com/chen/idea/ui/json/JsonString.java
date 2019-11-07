package com.chen.idea.ui.json;


/**
 *
 * @author chen
 */
public class JsonString extends JsonValue {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonString";
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();
    protected String value;

    public JsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public double asDouble() {
        return Tool.atod(value);
    }

    @Override
    public float asFloat() {
        return Tool.atof(value);
    }

    @Override
    public long asLong() {
        return Tool.atol(value);
    }

    @Override
    public int asInt() {
        return Tool.atoi(value);
    }

    @Override
    public boolean asBool() {
        return Tool.isNotEmpty(value) && (value.equals("1") || value.equals("true") || value.equals("是"));
    }

    public static void writeString(StringBuilder sb, String str, boolean encode) {
        if (Tool.isNotEmpty(str))
        {
            int length = str.length();
            for (int i = 0; i < length; i++)
            {
                char c = str.charAt(i);
                switch (c)
                {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    default:
                        if (!encode || c <= 0xff)
                        {
                            sb.append(c);
                        } else
                        {
                            sb.append("\\u").append(HEX[(c >> 12) & 0xf]).append(HEX[(c >> 8) & 0xf]).append(HEX[(c >> 4) & 0xf]).append(HEX[c & 0xf]);
                        }
                }
            }
        }
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append('"');
        writeString(sb, value, encode);
        sb.append('"');
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

        @Override
    public boolean isEmpty() {
        return Tool.isEmpty(value);
    }

    @Override
    public int getType() {
        return TYPE_STRING;
    }

    @Override
    public Object toObject() {
        return value;
    }

}
