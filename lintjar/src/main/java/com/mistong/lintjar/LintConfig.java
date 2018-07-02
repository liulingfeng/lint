package com.mistong.lintjar;

import com.android.tools.lint.detector.api.Context;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 配置文件，自定义提示内容
 */
public final class LintConfig {
    private String content;

    public LintConfig(Context context) {
        File projectDir = context.getProject().getDir();
        File configFile = new File(projectDir, "custom-lint-config.json");
        if (configFile.exists() && configFile.isFile()) {
            // 读取配置文件...
            content = readData(configFile);
        }
    }

    private String readData(File file) {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            /**
             * 注意这里的fileName不要用绝对路径，只需要文件名就可以了，系统会自动到data目录下去加载这个文件
             */
            fileInputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(
                    new InputStreamReader(fileInputStream));
            String result = "";
            while ((result = bufferedReader.readLine()) != null) {
                stringBuilder.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public String getConfig(String node) {
        if(content == null){
            return "";
        }
        JsonObject returnData = new JsonParser().parse(content).getAsJsonObject();
        JsonElement jsonElement = returnData.get(node);
        return jsonElement.getAsString();
    }
}
