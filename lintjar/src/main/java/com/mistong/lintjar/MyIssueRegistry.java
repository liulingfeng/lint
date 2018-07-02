package com.mistong.lintjar;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.mistong.lintjar.detector.MSTAttrPrefixDetector;
import com.mistong.lintjar.detector.MSTImageFileDetector;
import com.mistong.lintjar.detector.MSTLogDetector;
import com.mistong.lintjar.detector.MSTNewThreadDetector;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jzj on 2017/7/4.
 */
public class MyIssueRegistry extends IssueRegistry {

    @Override
    public synchronized List<Issue> getIssues() {
        System.out.println("==== my lint start ====");
        return Arrays.asList(MSTAttrPrefixDetector.ISSUE, MSTImageFileDetector.ISSUE, MSTLogDetector.ISSUE, MSTNewThreadDetector.ISSUE);
    }
}
