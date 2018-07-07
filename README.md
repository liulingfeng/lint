# lint
自定义lint规则

# 使用方式
可以项目依赖lintaar，也可以吧lintaar打包成maven使用。然后在app的build.gradle下进行如下配置
<pre>
 android节点下
  
   lintOptions {
        //这边可以对lint进行一些设置
        abortOnError false
    }
    
外层
android.applicationVariants.all { variant ->
    variant.outputs.each { output ->
        def lintTask = tasks["lint${variant.name.capitalize()}"]
        output.assemble.dependsOn lintTask
    }
}

另外可以在custome-lint-config.json中对一些变量进行设置。提高灵活性。命令行执行命名./gradlew lint 运行
</pre>

# lintOptions可配置项
<pre>
lintOptions {
        // true--关闭lint报告的分析进度
        quiet true
        // true--错误发生后停止gradle构建
        abortOnError false
        // true--只报告error
        ignoreWarnings true
        // true--忽略有错误的文件的全/绝对路径(默认是true)
        //absolutePaths true
        // true--检查所有问题点，包含其他默认关闭项
        checkAllWarnings true
        // true--所有warning当做error
        warningsAsErrors true
        // 关闭指定问题检查
        disable 'TypographyFractions', 'TypographyQuotes'
        // 打开指定问题检查
        enable 'RtlHardcoded', 'RtlCompat', 'RtlEnabled'
        // 仅检查指定问题
        check 'NewApi', 'InlinedApi'
        // true--error输出文件不包含源码行号
        noLines true
        // true--显示错误的所有发生位置，不截取
        showAll true
        // 回退lint设置(默认规则)
        lintConfig file("default-lint.xml")
        // true--生成txt格式报告(默认false)
        textReport true
        // 重定向输出；可以是文件或'stdout'
        textOutput 'stdout'
        // true--生成XML格式报告
        xmlReport false
        // 指定xml报告文档(默认lint-results.xml)
        xmlOutput file("lint-report.xml")
        // true--生成HTML报告(带问题解释，源码位置，等)
        htmlReport true
        // html报告可选路径(构建器默认是lint-results.html )
        htmlOutput file("lint-report.html")
        //  true--所有正式版构建执行规则生成崩溃的lint检查，如果有崩溃问题将停止构建
        checkReleaseBuilds true
        // 在发布版本编译时检查(即使不包含lint目标)，指定问题的规则生成崩溃
        fatal 'NewApi', 'InlineApi'
        // 指定问题的规则生成错误
        error 'Wakelock', 'TextViewEdits'
        // 指定问题的规则生成警告
        warning 'ResourceAsColor'
        // 忽略指定问题的规则(同关闭检查)
        ignore 'TypographyQuotes'
  }
</pre>

# lint.xml可配置项

> <lint>
    <!--忽略这个id-->
    <issue id="IconMissingDensityFolder" severity="ignore" />
    <!--特殊路径不检查-->
    <issue id="UselessLeaf">
        <ignore path="res/layout/main.xml" />
    </issue>
    <!--改变严重性-->
    <issue id="HardcodedText" severity="error" />
    <issue id="LogUtilsNotUsed" severity="error" />
    <issue id="AttrNotPrefixed" severity="warning"/>
    <issue id="NewThread" severity="warning"/>
    <issue id="ShowToast" severity="error"/>
    <issue id="ImageFileSizeOut" severity="error"/>
</lint>

# custome-lint-config可配置项
<pre>
{
  "log-usage-message": "请勿使用android.util.Log，建议使用Logger工具类", //log打印工具可替换，根据项目来定
  "value-prefix": "app_", //string前缀，每个module可单独配置
  "image-file-maxsize": "100" //图片文件限制的大小
}
</pre>

# 目前支持
### MSTAttrPrefixDetector
    string的所有name命名加前缀，主要是为了防止组件化之后的资源冲突
    
### MSTBaseActivityDetector
    所有activity必须继承BaseActivity，方便统一管理和防止统计遗漏
    
### MSTBroadcastDetector
    广播成对出现，注册之后要在onDestroy中注销
    
### MSTImageFileDetector
    检查项目中图片的大小，防止过大图片的出现
    
### MSTLogDetector
    统一使用Log打印工具，代码中不出现Log.e之类的代码
    
### MSTNewThreadDetector
    不允许直接new Thread
    
### MSTViewIdNameDetector
    view的id命名规范，参照阿里的命名规范

# 知识点
 <img src="https://github.com/liulingfeng/lint/blob/master/screenshot/lint.png" width="300" height="530">

# 参考

[慕课网Android自定义Lint实践](https://www.imooc.com/article/30302?block_id=tuijian_wz)</br>
[自定义Lint调试与开发](https://www.colabug.com/2109876.html)</br>
[美团外卖Android Lint代码检查实践](https://tech.meituan.com/waimai-android-lint.html)</br>
[Android自定义Lint实践](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0322/4070.html)</br>
[使用 Lint 改进您的代码](https://developer.android.com/studio/write/lint)</br>
[Detector api](https://static.javadoc.io/com.android.tools.lint/lint-api/25.3.0/com/android/tools/lint/detector/api/Detector.html)</br>
[简书Android Lint](https://www.jianshu.com/p/b4c44e62d652)</br>
[Psi查询文档](http://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/psi_files.html)</br>
[Android自定义Lint实践](https://tech.meituan.com/android_custom_lint.html)</br>
[官方Detector](https://android.googlesource.com/platform/tools/base/+/master/lint/libs/lint-checks/src/main/java/com/android/tools/lint/checks)</br>
[Uast树版lint](https://github.com/googlesamples/android-custom-lint-rules/blob/master/android-studio-3/checks/build.gradle)
