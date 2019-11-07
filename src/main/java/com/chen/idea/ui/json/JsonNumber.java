package com.chen.idea.ui.json;

/**
 *
 * @author chen
 */
public class JsonNumber extends JsonString {

    @SuppressWarnings("unused")
    private static final String TAG = "JsonNumber";

    public JsonNumber(String value) {
        super(value);
    }

    @Override
    public int getType() {
        return TYPE_NUMBER;
    }

    @Override
    public void write(StringBuilder sb, boolean encode) {
        sb.append(value);
    }

}
