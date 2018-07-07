package com.mistong.lintjar.detector;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.mistong.lintjar.LintConfig;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_ATTR;
import static com.android.SdkConstants.TAG_STRING;

/**
 * library的string命名需要加前缀
 * 防止资源冲突
 */
public class MSTAttrPrefixDetector extends ResourceXmlDetector {
    public static final Issue ISSUE = Issue.create("AttrNotPrefixed",
            "string的name需要前缀",
            "组件化项目中不加前缀很容易出现资源冲突",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            new Implementation(MSTAttrPrefixDetector.class,
                    Scope.RESOURCE_FILE_SCOPE)).addMoreInfo("https://github.com/liulingfeng/lint");

    private LintConfig lintConfig;

    @Override
    public void beforeCheckProject(Context context) {
        lintConfig = new LintConfig(context);
    }

    @Override
    public void beforeCheckLibraryProject(Context context) {
        lintConfig = new LintConfig(context);
    }

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return folderType.getName().equalsIgnoreCase(String.valueOf(ResourceFolderType.VALUES));
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_STRING);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(ATTR_NAME);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        super.visitAttribute(context, attribute);
    }

    @Override
    public void visitDocument(XmlContext context, Document document) {
        super.visitDocument(context, document);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        final Attr attr = element.getAttributeNode(ATTR_NAME);
        if (attr != null) {
            String val = attr.getValue();
            if (!"".equals(lintConfig.getConfig("value-prefix"))) {
                if (!val.startsWith("android:") && !val.startsWith(lintConfig.getConfig("value-prefix"))) {
                    String msg = "你需要在string的名字前加前缀" + lintConfig.getConfig("value-prefix");
                    context.report(ISSUE, attr, context.getLocation(attr), msg);
                }
            }
        }
        super.visitElement(context, element);
    }

    @Override
    public void visitElementAfter(XmlContext context, Element element) {
        super.visitElementAfter(context, element);
    }
}
