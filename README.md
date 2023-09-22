easyManagerSDK是用来对接easyManager应用程序对外开放开发接口用的

它支持调用easyManager应用里已有程序功能的api,更加方便与快捷的使用

例子:

```Java
    //如果你想终止一个后台进程,你应该这样做
    easyManagerClient ec = new easyManagerClient();
    if(ec.getServerStatus()){
        String pkgname = "com.abc";
        Context context = this;
        TransmissionEntity te = new TransmissionEntity(pkgname, null ,context.getPackageName(),0);
        ec.killpkg(te);
    }
```

更多的示例详情会在源码注释中说明,可以查阅源码.

[部分源码注释](https://github.com/MrsEWE44/easyManagerSDK/blob/master/src/main/java/com/easymanager/client/easyManagerClient.java#L193)



