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
    disable 'TypographyFractions','TypographyQuotes'
    enable 'RtlHardcoded','RtlCompat', 'RtlEnabled'
    check 'NewApi', 'InlinedApi'
    quiet true
    abortOnError false //遇到错误gradle暂停
    ignoreWarnings true //忽略所有warning的检查
  }
</pre>

# lint.xml可配置项
<pre>
<lint>
    <!-- 忽略指定Issueid的整个项目的检查 -->
    <issue id="IconMissingDensityFolder" severity="ignore" />

    <!-- 忽略指定Issueid的指定文件的检查 -->
    <issue id="ObsoleteLayoutParam">
        <ignore path="res/layout/activation.xml" />
        <ignore path="res/layout-xlarge/activation.xml" />
    </issue>

    <!-- 更改指定issue的Severity -->
    <issue id="HardcodedText" severity="error" />
</lint>
</pre>

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
