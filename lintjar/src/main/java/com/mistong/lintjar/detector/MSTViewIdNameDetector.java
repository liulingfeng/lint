package com.mistong.lintjar.detector;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import org.w3c.dom.Attr;
import java.util.Collection;
import java.util.Collections;
import static com.android.SdkConstants.VALUE_ID;

/**
 * view id的规范
 * 依据阿里的代码规范
 */
public class MSTViewIdNameDetector extends ResourceXmlDetector{
    /*linearlayout*/
    private static final String IDHEADER_LINEARLAYOUT_ABBREVIATION = "ll";
    private static final String ID_LAYOUT_LINEARLAYOUT = "LinearLayout";
    /*relativelayout*/
    private static final String IDHEADER_RELATIVELAYOUT_ABBREVIATION = "rl";
    private static final String ID_LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    /*button*/
    private static final String IDHEADER_BUTTON_ABBREVIATION = "btn";
    private static final String ID_BUTTON_BUTTON = "Button";
    /*radiobuttion*/
    private static final String IDHEADER_RADIOBUTTON_ABBREVIATION = "rb";
    private static final String ID_BUTTON_RADIOBUTTON = "RadioButton";
    /*imagebuttion*/
    private static final String IDHEADER_IMAGEBUTTON_ABBREVIATION = "ibtn";
    private static final String ID_BUTTON_IMAGEBUTTON = "ImageButton";
    /*text*/
    private static final String IDHEADER_TEXT_ABBREVIATION = "tv";
    private static final String ID_TEXT_TEXTVIEW = "TextView";
    /*edtext*/
    private static final String IDHEADER_EDITTEXT_ABBREVIATION = "et";
    private static final String ID_TEXT_EDITTEXT = "EditText";
    /*imageView*/
    private static final String IDHEADER_IMAGEVIEW_ABBREVIATION = "iv";
    private static final String ID_IMAGEVIEW = "ImageView";
    /*webView*/
    private static final String IDHEADER_WEBVIEW_ABBREVIATION = "wv";
    private static final String ID_WEBVIEW = "WebView";
    /*checkBox*/
    private static final String IDHEADER_CHECKBOX_ABBREVIATION = "cb";
    private static final String ID_CHECKBOX = "CheckBox";
    /*progressBar*/
    private static final String IDHEADER_PROGRESSBAR_ABBREVIATION = "progress_bar";
    private static final String ID_PROGRESSBAR = "ProgressBar";
    /*seekBar*/
    private static final String IDHEADER_SEEKBAR_ABBREVIATION = "seek_bar";
    private static final String ID_SEEKBAR = "SeekBar";
    /*ScrollView*/
    private static final String IDHEADER_SCROLLVIEW_ABBREVIATION = "sv";
    private static final String ID_SCROLLVIEW = "ScrollView";
    /*GridView*/
    private static final String IDHEADER_GRIDVIEW_ABBREVIATION = "gv";
    private static final String ID_GRIDVIEW = "GridView";
    /*listView*/
    private static final String IDHEADER_LISTVIEW_ABBREVIATION = "lv";
    private static final String ID_LISTVIEW = "ListView";

    private static final String ANDROID_ID = "android:id";
    String reportStrFormat = "id命名错误!: {%s} 前缀必须为: {%s}";

    public static final Issue ISSUE = Issue.create(
            "IdNameErrors",
            "viewId命名不规范",
            "viewId命名请遵循阿里规范",
            Category.CORRECTNESS,
            5,
            Severity.WARNING,
            new Implementation(
                    MSTViewIdNameDetector.class,
                    Scope.RESOURCE_FILE_SCOPE
            )
    );

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return folderType.getName().equalsIgnoreCase(String.valueOf(ResourceFolderType.LAYOUT));
    }

    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(VALUE_ID);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        super.visitAttribute(context, attribute);

        String prnMain = context.getMainProject().getDir().getPath();
        String prnCur = context.getProject().getDir().getPath();
        if (attribute.getName().startsWith(ANDROID_ID) && prnMain.equals(prnCur)) {
            checkNameSpace(context, attribute);
        }
    }

    private void checkNameSpace(XmlContext context, Attr attribute) {
        String tagName = attribute.getOwnerElement().getTagName();
        int startIndex = 0;
        String idName = attribute.getValue().substring(5);
        String attrRight = "";
        switch (tagName) {
            case ID_LAYOUT_LINEARLAYOUT:
                attrRight = IDHEADER_LINEARLAYOUT_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_LAYOUT_RELATIVELAYOUT:
                attrRight = IDHEADER_RELATIVELAYOUT_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_BUTTON_BUTTON:
                attrRight = IDHEADER_BUTTON_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_BUTTON_IMAGEBUTTON:
                attrRight = IDHEADER_IMAGEBUTTON_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_BUTTON_RADIOBUTTON:
                attrRight = IDHEADER_RADIOBUTTON_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_TEXT_TEXTVIEW:
                attrRight = IDHEADER_TEXT_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_TEXT_EDITTEXT:
                attrRight = IDHEADER_EDITTEXT_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_IMAGEVIEW:
                attrRight = IDHEADER_IMAGEVIEW_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_WEBVIEW:
                attrRight = IDHEADER_WEBVIEW_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_CHECKBOX:
                attrRight = IDHEADER_CHECKBOX_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_PROGRESSBAR:
                attrRight = IDHEADER_PROGRESSBAR_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_SEEKBAR:
                attrRight = IDHEADER_SEEKBAR_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_LISTVIEW:
                attrRight = IDHEADER_LISTVIEW_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_GRIDVIEW:
                attrRight = IDHEADER_GRIDVIEW_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
            case ID_SCROLLVIEW:
                attrRight = IDHEADER_SCROLLVIEW_ABBREVIATION;
                startIndex = idName.indexOf(attrRight);
                break;
        }

        if (startIndex != 0) {
            String reportStr = String.format(reportStrFormat, idName, attrRight);
            context.report(ISSUE,
                    attribute,
                    context.getLocation(attribute),
                    reportStr);
        }
    }
}
