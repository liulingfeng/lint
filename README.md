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


</pre>

# 目前支持
### 
