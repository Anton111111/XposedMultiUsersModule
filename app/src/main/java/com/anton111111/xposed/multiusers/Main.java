package com.anton111111.xposed.multiusers;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
/**
 * Main class for Xposed module for add ability to add several users to Lenovo Devices
 * @author Anton Potekhin (anton.potekhin@gmail.com)
 */
public class Main implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public static int MAX_NUMBER_SUPPORTED_USERS = 2;

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        Class aClass = XposedHelpers.findClass(android.os.UserManager.class.getName(), null);
        Method getMaxSupportedUsersMethod = XposedHelpers.findMethodExact(aClass, "getMaxSupportedUsers");

        getMaxSupportedUsersMethod.setAccessible(true);
        XposedBridge.hookMethod(getMaxSupportedUsersMethod, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if ((int)param.getResult() <= 1) {
                    param.setResult(MAX_NUMBER_SUPPORTED_USERS);
                    XposedBridge.log("Override result of getMaxSupportedUsers to : " + MAX_NUMBER_SUPPORTED_USERS);
                }
            }
        });
    }
}