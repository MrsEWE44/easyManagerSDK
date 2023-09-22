# easyManagerSDK是用来对接easyManager应用程序对外开放开发接口用的

# 它支持调用easyManager应用里已有程序功能的api,更加方便与快捷的使用

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

