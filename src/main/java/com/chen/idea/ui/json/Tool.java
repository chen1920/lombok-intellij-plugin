package com.chen.idea.ui.json;

/**
 * @author chen
 */
public class Tool {

    private static final String TAG = "Tool";
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * @param num
     * @return
     */
    public static int atoi(String num) {
        int ret = 0;
        if (num != null && num.length() > 0) {
            int l = num.length();
            int i;
            for (i = 0; i < l; i++) {
                char c = num.charAt(i);
                if ((c < '0' || c > '9') && (i != 0 || c != '-')) {
                    break;
                }
            }
            if (i != l) {
                num = num.substring(0, i);
            }
            try {
                ret = Integer.parseInt(num);
            } catch (Exception e) {
            }
        }
        return ret;
    }

    /**
     * @param num
     * @return
     */
    public static long atol(String num) {
        long ret = 0;
        if (num != null && num.length() > 0) {
            int l = num.length();
            int i;
            for (i = 0; i < l; i++) {
                char c = num.charAt(i);
                if ((c < '0' || c > '9') && (i != 0 || c != '-')) {
                    break;
                }
            }
            if (i != l) {
                num = num.substring(0, i);
            }
            try {
                ret = Long.parseLong(num);
            } catch (Exception e) {
            }
        }
        return ret;
    }

    /**
     * @param num
     * @return
     */
    public static float atof(String num) {
        float ret = 0;
        if (num != null && num.length() > 0) {
            int l = num.length();
            int i;
            boolean haveE = false;
            for (i = 0; i < l; i++) {
                char c = num.charAt(i);
                if (c != '.' && c != '-' && c != 'E' && (c < '0' || c > '9')) {
                    break;
                }
                if (c == 'E') {
                    if (i < 1 || haveE) {
                        break;
                    } else {
                        haveE = true;
                    }
                }
            }
            if (i != l) {
                num = num.substring(0, i);
            }
            try {
                ret = Float.parseFloat(num);
            } catch (Exception e) {
            }
        }
        return ret;
    }

    /**
     * @param num
     * @return
     */
    public static double atod(String num) {
        double ret = 0;
        if (num != null && num.length() > 0) {
            int l = num.length();
            int i;
            boolean haveE = false;
            for (i = 0; i < l; i++) {
                char c = num.charAt(i);
                if (c != '.' && c != '-' && c != 'E' && (c < '0' || c > '9')) {
                    break;
                }
                if (c == 'E') {
                    if (i < 1 || haveE) {
                        break;
                    } else {
                        haveE = true;
                    }
                }
            }
            if (i != l) {
                num = num.substring(0, i);
            }
            try {
                ret = Double.parseDouble(num);
            } catch (Exception e) {
            }
        }
        return ret;
    }

}
