package com.easymanager.client;

import android.content.Context;
import android.os.IBinder;

import com.easymanager.core.entity.TransmissionEntity;
import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.entity.easyManagerServiceEntity;
import com.easymanager.core.utils.CMD;
import com.easymanager.entitys.MyPackageInfo;
import com.easymanager.enums.easyManagerEnums;
import com.easymanager.mylife.adbClient;

import java.util.List;


/**
 * <pre>
 *     easyManagerClient是用来对接easymanager服务的.<br/>
 *     它提供了easymanager所提供的功能接口实现,你需要做的，只是调用相同的api接口即可使用easy manager的功能.<br/>
 *     详情可以查看公开的函数帮助.<br/>
 *
 * </pre>
 *
 *
 * */
public class easyManagerClient {

    /**
     *
     * 简易封装的客户端请求函数
     * */
    private easyManagerServiceEntity putOptionOnServer(easyManagerClientEntity adben2){
        adbClient ac = new adbClient(adben2, new adbClient.SocketListener() {
            @Override
            public easyManagerServiceEntity getAdbEntity(easyManagerServiceEntity adfb) {
                return null;
            }
        });
        return ac.getAdbEntity();
    }

    /**
     * 获取授权服务是否在运行
     *
     * */
    public Boolean getServerStatus(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.GET_SERVER_STATUS);
        return checkBool(adben2);
    }

    /**
     *
     * 判断当前环境是否为ADB工作模式
     * */
    public boolean isADB(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null, easyManagerEnums.IS_ADB);
        return checkBool(adben2);
    }

    /**
     *
     * 判断当前环境是否为ROOT工作模式
     * */
    public boolean isROOT(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.IS_ROOT);
        return checkBool(adben2);
    }

    private boolean checkBool(easyManagerClientEntity adben2){
        easyManagerServiceEntity serviceEntity = putOptionOnServer(adben2);
        getError(serviceEntity);
        boolean a = false;
        try{
            a =  (boolean) serviceEntity.getObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }

    /**
     * 执行系统命令<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param cmdstr 需要传入一个命令字符串
     *
     * <pre>
     *               在使用这个函数的时候，你应该按照以下示例编写:<br/><br/>
     *               String cmdstr = "ls /data/data/";<br/>
     *               runCMD(cmdstr);<br/><br/>
     *
     *               最终你应该这样使用的: <br/>
     *                      String cmdstr = "ls /data/data/"; <br/>
     *
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      CMD cmd = ec.runCMD(cmdstr); <br/>
     *                      if(cmd.getResultCode()==0){<br/>
     *                           Log.d("cmd",cmd.getResult());<br/>
     *                      }<br/>
     *                      Log.d("cmd",cmd.toString());<br/><br/>
     *
     *
     *</pre>
     *
     * */
    public CMD runCMD(String cmdstr){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(cmdstr,null,-1);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        return eee.getCmd();
    }

    /**
     * 清理后台进程<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,null,reqpkg,0,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要终止掉的应用包名<br/>
     *                      比如: com.abc <br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = " com.abc";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, null ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.killpkg(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void killpkg(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.KILL_PROCESS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 配置本地应用权限(直接调用本地IAppOpsService实现)<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,opt_str,reqpkg,0，uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要操作的包名.<br/>
     *               opt_str :  这个参数为需要权限名称加上授权的状态.<br/>
     *                      授权状态为: {<br/>
     *                          default : 默认状态.<br/>
     *                          ignore  : 始终忽略状态.<br/>
     *                          allow : 始终允许状态.<br/>
     *                          foreground :  运行时允许.<br/>
     *                      };<br/>
     *                      权限名字示例: android:write_sms
     *                          <br/><br/>
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/><br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *               最终你应该这样使用的: <br/>
     *               这样可以拒绝掉包名为com.abc应用的短信写入权限.<br/>
     *                      String pkgname = "com.abc";<br/>
     *                      String opt_str = "android:write_sms---ignore";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, opt_str ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.setAppopsModeCore(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void setAppopsModeCore(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_APPOPS_CORE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
    }
    /**
     * 配置本地应用权限<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,opt_str,reqpkg,APP_PERMIS_INDEX,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要操作的包名.<br/>
     *               opt_str :  这个参数为需要授权的状态.<br/>
     *                      0-15的授权状态为: {<br/>
     *                          default : 默认状态.<br/>
     *                          ignore  : 始终忽略状态.<br/>
     *                          allow : 始终允许状态.<br/>
     *                          foreground :  运行时允许.<br/>
     *                      };<br/><br/>
     *                      16的授权状态为:{<br/>
     *                          true: 允许状态.<br/>
     *                          false: 拒绝状态.<br/>
     *                      }<br/>
     *                      17的授权状态为:{<br/>
     *                          active : 活跃状态.<br/>
     *                          working_set : 工作状态.<br/>
     *                          frequent : 常用状态.<br/>
     *                          rare : 极少使用状态.<br/>
     *                          restricted : 受限状态.<br/>
     *                      }       <br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               APP_PERMIS_INDEX: 这个参数为权限数字.每一个数字对应一个权限集合.<br/>
     *                      0-15分别为: {<br/>
     *                          0:"通话/短信相关",<br/>
     *                          1:"存储",<br/>
     *                          2:"剪切板",<br/>
     *                          3:"电池优化",<br/>
     *                          4:"后台运行",<br/>
     *                          5:"摄像头/麦克风",<br/>
     *                          6:"定位",<br/>
     *                          7:"日历",<br/>
     *                          8:"传感器扫描",<br/>
     *                          9:"通知"<br/>
     *                          10:"生物指纹识别"<br/>
     *                          11:"弹窗"<br/>
     *                          12:"无障碍"<br/>
     *                          13:"读取账户"<br/>
     *                          14:"写入系统设置"<br/>
     *                          15:"读取设备标识"<br/>
     *                      };
     *
     *              <br/><br/>
     *                      16-17分别为: {<br/>
     *                         16:"强制应用待机",<br/>
     *                         17:"应用待机活动"<br/>
     *                     };
     *
     *               <br/>
     *
     *                uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *               这样可以拒绝掉包名为com.abc应用的通话/短信权限.<br/>
     *                      String pkgname = "com.abc";<br/>
     *                      String opt_str = "ignore";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      int APP_PERMIS_INDEX = 0;<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, opt_str ,reqpkg,APP_PERMIS_INDEX,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.setAppopsMode(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void setAppopsMode(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_APPOPS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 安装本地apk文件<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(apkpath,null,reqpkg,0,uid);<br/><br/>
     *               参数说明:<br/>
     *               apkpath : 本地apk完整路径<br/>
     *                      比如: /data/local/tmp/base.apk <br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String apkpath = "/data/local/tmp/base.apk";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(apkpath, null ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.installAPK(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void installAPK(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.INSTALL_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 卸载已安装的应用<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,null,reqpkg,0,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要卸载的包名<br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, null ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.uninstallAPK(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void uninstallAPK(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.UNINSTALL_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 设置已安装软件或者程序组件是否为冻结或者禁用状态<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,null,reqpkg,state,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要操作的包名<br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *
     *               state: 设置应用是否为隐藏状态.<br/>
     *                      1为启用.<br/>
     *                      3为禁用. <br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      int state = 3; <br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, null ,reqpkg,state,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.setComponentOrPackageEnabledState(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void setComponentOrPackageEnabledState(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_COMPONENT_OR_PACKAGE_ENABLE_STATE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 设置已安装软件是否显示在桌面上<br/>
     * 调用该函数,必须要ROOT工作模式才行.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,null,reqpkg,state,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要操作的包名<br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *
     *               state: 设置应用是否为隐藏状态.<br/>
     *                      0为显示.<br/>
     *                      1为隐藏. <br/>
     *
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      int state = 0; <br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, null ,reqpkg,state,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.setPackageHideState(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void setPackageHideState(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_HIDE_STATE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 撤销已安装软件某个权限<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,permission_str,reqpkg,0);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要操作的包名<br/>
     *
     *               permission_str: 字符串拼接的完整权限名称;<br/>
     *                      比如:android.Manifest.permission.UPDATE_APP_OPS_STATS <br/>
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String permission_str = "android.Manifest.permission.UPDATE_APP_OPS_STATS";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, permission_str ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.revokeRuntimePermission(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    @Deprecated
    public void revokeRuntimePermission(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_REVOKE_RUNTIME_PERMISSION);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 授予已安装软件某个权限<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,permission_str,reqpkg,0,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要操作的包名<br/>
     *
     *               permission_str: 字符串拼接的完整权限名称;<br/>
     *                      比如:android.Manifest.permission.UPDATE_APP_OPS_STATS <br/>
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String permission_str = "android.Manifest.permission.UPDATE_APP_OPS_STATS";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, permission_str ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.grantRuntimePermission(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    @Deprecated
    public void grantRuntimePermission(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.SET_PACKAGE_GRANT_RUNTIME_PERMISSION);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }


    /**
     * 备份已安装的软件<br/>
     *  该函数必须要在ROOT模式下才能使用.<br/>
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname, oop,reqpkg,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要备份的包名<br/>
     *
     *               oop: 字符串拼接的参数内容,以---来分割参数;<br/>
     *                      第一个参数内容:<br/>
     *                              full: 完全恢复，包括安装包程序、软件数据.<br/>
     *                              data: 仅恢复数据,不包括安装程序，需要已经安装好了应用程序才可以恢复数据.<br/>
     *                              apk: 仅恢复安装程序，不包括数据恢复.<br/>
     *                      第二个参数内容:<br/>
     *                              txz: 选择带tar.xz结尾命名的备份文件进行恢复.<br/>
     *                              tgz: 选择带tar.gz结尾命名的备份文件进行恢复.<br/>
     *                              tbz: 选择带tar.gz结尾命名的备份文件进行恢复.<br/>
     *                      第三个参数内容:<br/>
     *                              apkpath: 这个为已安装文件的完整路径.在低版本安卓上,完整路径应该为:<br/>
     *                                      /data/app/com.explam-1.apk   <br/>
     *                      第四个参数内容:<br/>
     *                              sdpath: 内部存储根路径(一般是这个,你也可以修改成其它的),它最终拼接后的路径则是:<br/>
     *                                      sdpath+"/easyManager/backup/",程序会从这个路径下查找之前备份过的压缩包文件,然后进行恢复.<br/>
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 这个参数要传入你当前的用户uid.一般默认为0.它同时支持多用户的应用数据恢复.<br/><br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String oop = "full---tgz---/data/app/com.explam-1.apk---/storage/emulated/0";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      int uid = 0;<br/>
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, oop,reqpkg,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.backupApk(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void backupApk(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.BACKUP_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }


    /**
     * 恢复已备份的软件<br/>
     *  该函数必须要在ROOT模式下才能使用. <br/>
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname, oop,reqpkg,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要恢复备份的包名<br/>
     *
     *               oop: 字符串拼接的参数内容,以---来分割参数;<br/>
     *                      第一个参数内容:<br/>
     *                              full: 完全恢复，包括安装包程序、软件数据.<br/>
     *                              data: 仅恢复数据,不包括安装程序，需要已经安装好了应用程序才可以恢复数据.<br/>
     *                              apk: 仅恢复安装程序，不包括数据恢复.<br/>
     *                      第二个参数内容:<br/>
     *                              txz: 选择带tar.xz结尾命名的备份文件进行恢复.<br/>
     *                              tgz: 选择带tar.gz结尾命名的备份文件进行恢复.<br/>
     *                              tbz: 选择带tar.gz结尾命名的备份文件进行恢复.<br/>
     *                      第三个参数内容:<br/>
     *                              sdpath: 内部存储根路径(一般是这个,你也可以修改成其它的),它最终拼接后的路径则是:<br/>
     *                                      sdpath+"/easyManager/backup/",程序会从这个路径下查找之前备份过的压缩包文件,然后进行恢复.<br/>
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 这个参数要传入你当前的用户uid.一般默认为0.它同时支持多用户的应用数据恢复.<br/><br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String oop = "full---tgz---/storage/emulated/0";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      int uid = 0;<br/>
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, oop,reqpkg,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      ec.restoryApp(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void restoryApp(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.RESTORY_APK);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 客户端应该先调用该函数，使其被添加到授权列表当中
     * 然后由授权工具管理授权，确认是否允许调用其它功能api函数。
     *
     * @param context 需要传入一个Context对象，用于获取请求者包名以及其它内容
     *
     * */
    public void requestGrantUser(Context context){
        TransmissionEntity entity = new TransmissionEntity(context.getPackageName(), context.getFilesDir().toString(), context.getPackageName(), -1,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GRANT_USER);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 创建一个分身用户<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param context 需要传入一个Context对象，用于获取请求者包名以及其它内容
     *
     * <pre>
     *               在使调用这个函数的时候，你应该这样使用的: <br/>
     *                Context context = this;<br/>
     *                easyManagerClient ec = new easyManagerClient(); <br/>
     *                ec.createAppClone(context); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void createAppClone(Context context) {
        TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,getCurrentUserID());
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_CLONE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 删除一个分身用户<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param context 需要传入一个Context对象，用于获取请求者包名以及其它内容
     * @param removeuid 需要传入一个你想删除的分身UID
     *
     * <pre>
     *               在使调用这个函数的时候，你应该这样使用的: <br/>
     *                Context context = this;<br/>
     *                int removeuid = 10; //这个是需要被删除的分身用户UID,必须是整数类型<br/>
     *                easyManagerClient ec = new easyManagerClient(); <br/>
     *                ec.removeAppClone(context,removeuid); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void removeAppClone(Context context , int removeuid) {
        TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,removeuid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.APP_CLONE_REMOVE);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 启动一个分身用户<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param context 需要传入一个Context对象，用于获取请求者包名以及其它内容
     * @param startuid 需要传入一个你想启动的分身UID
     *
     * <pre>
     *               在使调用这个函数的时候，你应该这样使用的: <br/>
     *                Context context = this;<br/>
     *                int startuid = 10; //这个是需要被启动的分身用户UID,必须是整数类型<br/>
     *                easyManagerClient ec = new easyManagerClient(); <br/>
     *                ec.startAppClone(context,startuid); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public void startAppClone(Context context , int startuid) {
        TransmissionEntity entity = new TransmissionEntity(null,null,context.getPackageName(),0,startuid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.START_USER_ID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
    }

    /**
     * 获取本地所有分身用户的UID<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     *
     * <pre>
     *               在使调用这个函数的时候，你应该这样使用的: <br/>
     *                easyManagerClient ec = new easyManagerClient(); <br/>
     *                String[] users = ec.getAppCloneUsers();//会返回一个字符串数组,里面包含了本地所有分身用户的UID字符串 <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public String[] getAppCloneUsers() {
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.APP_CLONE_GETUSERS);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        try {
            return (String[]) eee.getObject();
        }catch (Exception e){
            return new String[]{"0"};
        }
    }

    /**
     * 获取本地所有已安装的应用<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(null,null,reqpkg,0,uid);<br/><br/>
     *               参数说明:<br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(null, null ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      List<MyPackageInfo> pkgs = ec.getInstalledPackages(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public List<MyPackageInfo> getInstalledPackages(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.QUERY_PACKAGES_UID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        return (List<MyPackageInfo>) eee.getObject();
    }

    /**
     * 将应用信息转化为自定义的MyPackageInfo类型<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param entity 需要传入一个TransmissionEntity实体类变量
     *
     * <pre>
     *               在使用这个函数的时候，你的TransmissionEntity对象应该按照以下示例编写:<br/><br/>
     *               TransmissionEntity te = new TransmissionEntity(pkgname,null,reqpkg,0,uid);<br/><br/>
     *               参数说明:<br/>
     *               pkgname : 你想要转化的应用包名<br/>
     *                      比如: com.abc <br/>
     *
     *               reqpkg: 这个参数要传入你客户端的包名.<br/>
     *                      如果你的客户端包名为"com.explam"那你就需要传入这个包名.<br/>
     *               uid: 需要操作的用户uid. <br/><br/>
     *
     *
     *               最终你应该这样使用的: <br/>
     *                      String pkgname = " com.abc";<br/>
     *                      String reqpkg = "com.explam";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      TransmissionEntity te = new TransmissionEntity(pkgname, null ,reqpkg,0,uid);<br/>
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      MyPackageInfo myPackageInfo = ec.getMyPackageInfo(te); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public MyPackageInfo getMyPackageInfo(TransmissionEntity entity){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,entity,easyManagerEnums.GET_PACKAGEINFO_UID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        return (MyPackageInfo) eee.getObject();
    }

    /**
     * 获取当前正在使用的用户UID<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     *
     * <pre>
     *               在使调用这个函数的时候，你应该这样使用的: <br/>
     *                easyManagerClient ec = new easyManagerClient(); <br/>
     *                int uid = ec.getCurrentUserID(); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public int getCurrentUserID(){
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,null,easyManagerEnums.GET_CURRENT_USER_ID);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        return (Integer) eee.getObject();
    }

    /**
     * 获取软件或者程序组件状态<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param context 需要传入一个Context对象，用于获取请求者包名以及其它内容
     * @param pkgname 需要传入一个你想获取的程序包名
     * @param componentName 需要传入一个程序组件名称
     * @param uid 需要操作的用户uid
     *
     * <pre>
     *               在使用这个函数的时候，你应该这样使用的: <br/>
     *                      Context context =  this;<br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String componentName = ".MainActivity";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      int state = ec.getComponentEnabledSetting(context,pkgname,componentName,uid); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public int getComponentEnabledSetting(Context context , String pkgname,String componentName,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, componentName, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.GET_COMPONENT_ENABLED_SETTING);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        return (Integer) eee.getObject();
    }

    /**
     * 获取软件权限的状态<br/>
     * 调用该函数,最好要ROOT工作模式,国内部分定制ui存在权限不足的问题.<br/>
     *
     * @param context 需要传入一个Context对象，用于获取请求者包名以及其它内容
     * @param pkgname 需要传入一个你想获取的程序包名
     * @param opstr 需要传入一个程序的权限名称
     * @param uid 需要操作的用户uid
     *
     * <pre>
     *               在使用这个函数的时候，你应该这样使用的: <br/>
     *                      Context context =  this;<br/>
     *                      String pkgname = "com.eeee";<br/>
     *                      String opstr = "android.permission.WRITE_EXTERNAL_STORAGE";<br/>
     *                      //如果你想操作当前用户的应用,应该调用getCurrentUserID()函数,否则,就需要填入一个整数型的用户UID.<br/>
     *                      int uid = getCurrentUserID();<br/>
     *
     *                      easyManagerClient ec = new easyManagerClient(); <br/>
     *                      int state = ec.checkOp(context,pkgname,AppOpsManager.permissionToOp(opstr),uid); <br/><br/><br/>
     *
     *
     *</pre>
     *
     * */
    public int checkOp(Context context , String pkgname,String opstr,int uid){
        TransmissionEntity transmissionEntity = new TransmissionEntity(pkgname, opstr, context.getPackageName(), 0, uid);
        easyManagerClientEntity adben2 = new easyManagerClientEntity(null,transmissionEntity,easyManagerEnums.CHECK_OP);
        easyManagerServiceEntity eee = putOptionOnServer(adben2);
        getError(eee);
        return (Integer) eee.getObject();
    }






    /**
     * 抛出运行时异常，获取服务端返回的错误信息内容
     *
     * @param eee 需要传入一个负责接收服务端的实体类
     * */
    private void getError(easyManagerServiceEntity eee){
        Object object = eee.getObject();
        if(object != null){
            if(object instanceof String){
                String obj  = (String) object;
                if(!obj.equals("0 --- 0")){
                    throw new RuntimeException((String) object);
                }
            }
        }

    }


}
