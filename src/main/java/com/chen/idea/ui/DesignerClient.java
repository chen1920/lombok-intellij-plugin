package com.chen.idea.ui;

import com.sankuai.win.ui.json.JsonObject;

/**
 * @author chen
 */
public class DesignerClient {

    private static final String TAG = "DesignerClient";

    public static String openLayout(String xmlPath){
        JsonObject json = new JsonObject();
        json.put(Constants.XML_PATH_KEY, xmlPath);
        byte[] ret = HttpTool.doPost("http://127.0.0.1:" + Constants.PORT +
                Constants.OPEN_UI_XML, json.toString().getBytes(Constants.UTF8));
        if (ret != null) {
            System.out.println("ret=" + new String(ret, Constants.UTF8));
        } else {
            System.out.println("do post fail");
        }
        return null;
    }

}
