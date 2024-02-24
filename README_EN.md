easyManagerSDK is used to connect the easyManager application to the external open development interface.

It supports calling APIs with existing program functions in the easyManager application, making it more convenient and faster to use.

example:

```Java
    //If you want to kill a background process you should do this
    easyManagerClient ec = new easyManagerClient();
    if(ec.getServerStatus()){
        String pkgname = "com.abc";
        Context context = this;
        int uid = ec.getCurrentUserID();
        TransmissionEntity te = new TransmissionEntity(pkgname, null ,context.getPackageName(),0,uid);
        ec.killpkg(te);
    }
```

More example details will be explained in the source code comments, you can check the source code.

[Some source code comments](https://github.com/MrsEWE44/easyManagerSDK/blob/master/src/main/java/com/easymanager/client/easyManagerClient.java#L193)



