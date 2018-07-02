package com.mistong.lintjar.detector;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.mistong.lintjar.LintConfig;

/**
 * 图片大小检测
 */
public class MSTImageFileDetector extends Detector implements Detector.BinaryResourceScanner {
    public static Issue ISSUE = Issue.create("ImageFileSizeOut",
            "图片资源过大",
            "使用Tiny插件压缩一下图片",
            Category.CORRECTNESS, 5, Severity.ERROR,
            new Implementation(MSTImageFileDetector.class, Scope.BINARY_RESOURCE_FILE_SCOPE));

    private String reportStr;
    private LintConfig lintConfig;

    @Override
    public void beforeCheckProject(Context context) {
        lintConfig = new LintConfig(context);
        reportStr = "图片文件过大: %d" + "KB,超过了项目限制的:" + lintConfig.getConfig("image-file-maxsize") + "KB,请进行压缩或找设计重新出图.";
    }

    @Override
    public void beforeCheckLibraryProject(Context context) {
        lintConfig = new LintConfig(context);
        reportStr = "图片文件过大: %d" + "KB,超过了项目限制的:" + lintConfig.getConfig("image-file-maxsize") + "KB,请进行压缩或找设计重新出图.";
    }

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return folderType.getName().equalsIgnoreCase(String.valueOf(ResourceFolderType.MIPMAP)) || folderType.getName().equalsIgnoreCase(String.valueOf(ResourceFolderType.DRAWABLE));
    }

    @Override
    public void checkBinaryResource(ResourceContext context) {
        String filename = context.file.getName();

        if (filename.contains(".png")
                || filename.contains(".jpeg")
                || filename.contains(".jpg")
                ) {
            long fileSize = context.file.length() / 1024;
            if (fileSize > Integer.parseInt(lintConfig.getConfig("image-file-maxsize"))) {

                String repS = String.format(reportStr, fileSize);

                Location fileLocation = Location.create(context.file);
                context.report(ISSUE,
                        fileLocation,
                        repS);
            }
        }
    }
}
