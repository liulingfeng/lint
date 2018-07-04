package com.mistong.lintjar.detector;

import com.android.tools.lint.detector.api.Detector;

import java.util.List;

/**
 * 避免混淆产生的实体类问题
 * 实体类必须加@Keep
 */
public class MSTEntityDetector extends Detector implements Detector.JavaPsiScanner{

    @Override
    public List<String> getApplicableMethodNames() {
        return super.getApplicableMethodNames();
    }
}
