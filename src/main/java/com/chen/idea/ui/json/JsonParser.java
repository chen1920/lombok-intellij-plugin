package com.chen.idea.ui.json;

/**
 * @author chen
 */
public class JsonParser {

    private static final String TAG = "JsonParser";
    private final String source;
    private int off;
    private final int end;
    private boolean backed = false;
    private int backValue = 0;
    private StringBuilder unicode;

    public JsonParser(String source, int off, int end) {
        this.source = source;
        this.off = off;
        this.end = end;
    }

    private void back(int backValue) {
        this.backValue = backValue;
        backed = true;
    }

    public int read() {
        if (backed) {
            backed = false;
            return backValue;
        }
        if (off < end) {
            return source.charAt(off++);
        }
        return -1;
    }

    public int readSkipBlank() {
        if (backed) {
            backed = false;
            return backValue;
        }
        while (off < end) {
            int ret = source.charAt(off++);
            if (ret != '\n' && ret != '\t' && ret != ' ') {
                return ret;
            }
        }
        return -1;
    }

    public JsonObject parseObject() {
        for (; ; ) {
            int c = readSkipBlank();
            switch (c) {
                case -1:
                    return null;
                case '{':
                    return readObject(new StringBuilder(64));
                default:
                    break;
            }
        }
    }

    private JsonObject readObject(StringBuilder sb) {
        JsonObject obj = new JsonObject();
        for (; ; ) {
            String key = null;
            int c = readSkipBlank();
            switch (c) {
                case '\"':
                case '\'':
                    key = readString(sb, c);
                    break;
                case '}':
                    return obj;
                case ',':
                    continue;
                default:
                    break;
            }
            if (key == null) {
                break;
            }
            c = this.readSkipBlank();
            if (c != ':') {
                break;
            }
            JsonValue value = readJsonValue(sb);
            if (value == null) {
                break;
            }
            obj.put(key, value);
        }
        return obj;
    }

    private String readString(StringBuilder sb, int end) {
        sb.setLength(0);
        for (; ; ) {
            int c = read();
            if (c == end) {
                return sb.toString();
            }
            switch (c) {
                case -1:
                    return null;
                case '\\':
                    c = read();
                    switch (c) {
                        case -1:
                            return null;
                        case '\'':
                            sb.append('\'');
                            break;
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'u':
                            if (unicode == null) {
                                unicode = new StringBuilder();
                            }
                            unicode.setLength(0);
                            unicode.append((char) read());
                            unicode.append((char) read());
                            unicode.append((char) read());
                            unicode.append((char) read());
                            try {
                                sb.append((char) Integer.parseInt(unicode.toString(), 16));
                            } catch (Throwable e) {
                            }
                            break;
                        default:
                            sb.append('\\');
                            sb.append(c);
                    }
                    break;
                default:
                    sb.append((char) c);
            }
        }
    }

    private JsonValue readJsonValue(StringBuilder sb) {
        int c = readSkipBlank();
        switch (c) {
            case -1:
                return null;
            case ',':
            case '}':
                return null;
            case '\"':
            case '\'': {
                String value = readString(sb, c);
                if (value == null) {
                    return null;
                }
                return new JsonString(value);
            }
            case '{':
                return readObject(sb);
            case '[':
                return readArray(sb);
            case ']':
                return null;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
            case '.':
                return readNumber(sb, c);
            default:
                return readId(sb, c);
        }
    }

    private JsonValue readNumber(StringBuilder sb, int c) {
        sb.setLength(0);
        sb.append((char) c);
        for (; ; ) {
            c = read();
            switch (c) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                case '.':
                case 'E':
                    sb.append((char) c);
                    break;
                case ',':
                case '}':
                case ']':
                    back(c);
                case -1:
                    return new JsonNumber(sb.toString());
                default:
                    break;
            }
        }

    }

    private JsonArray readArray(StringBuilder sb) {
        JsonArray array = new JsonArray();
        for (; ; ) {
            int c = this.readSkipBlank();
            switch (c) {
                case ']':
                case -1:
                    return array;
                case ',':
                    continue;
                default:
                    back(c);
            }
            JsonValue value = this.readJsonValue(sb);
            if (value == null) {
                break;
            }
            array.add(value);
        }
        return array;
    }

    private JsonValue readId(StringBuilder sb, int c) {
        sb.setLength(0);
        sb.append((char) c);
        for (; ; ) {
            c = read();
            switch (c) {
                case ',':
                case '}':
                case ']':
                    back(c);
                case -1:
                    String data = sb.toString();
                    if (data.equals("true")) {
                        return new JsonBool(true);
                    } else if (data.equals("false")) {
                        return new JsonBool(false);
                    } else if (data.equals("null")) {
                        return new JsonNull();
                    }
                    return new JsonNumber(data);
                default:
                    sb.append((char) c);
                    break;
            }
        }
    }

    public JsonValue parseValue() {
        for (; ; ) {
            int c = readSkipBlank();
            switch (c) {
                case -1:
                    return null;
                case '{':
                    return readObject(new StringBuilder(64));
                case '[':
                    return readArray(new StringBuilder(64));
                default:
                    break;
            }
        }
    }

}
