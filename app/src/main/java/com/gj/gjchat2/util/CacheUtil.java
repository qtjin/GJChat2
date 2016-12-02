package com.gj.gjchat2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.entity.LoginEntity;
import com.gj.gjlibrary.util.AbGsonUtil;
import com.gj.gjlibrary.util.PreferencesUtils;


/**
 * Created by qiyunfeng on 15/11/16.
 */
public class CacheUtil {

    public CacheUtil() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PreferencesUtils.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    //获取用昵称
    public static String getNickname(Context context){
        return PreferencesUtils.getString(context, "nickname", "");
    }


    //获取用昵称
    public static String getqq(Context context){
        return PreferencesUtils.getString(context, "qq", "");
    }

    //获取老师ID
    public static String getTeacherId(Context context){
        return PreferencesUtils.getString(context, "id", "");
    }


    //获取用昵称
    public static String getemail(Context context){
        return PreferencesUtils.getString(context, "email", "");
    }


    //获取用昵称
    public static String getphone(Context context){
        return PreferencesUtils.getString(context, "phone", "");
    }

    //保存用户名
    public static void saveNickname(Context context, String userName){
        PreferencesUtils.putString(context, "nickname", userName);
    }

    //保存qq
    public static void saveQQ(Context context, String qq){
        PreferencesUtils.putString(context, "qq", qq);
    }


    //保存专业
    public static void saveZhuanye(Context context, String qq){
        PreferencesUtils.putString(context, "qq", qq);
    }

    //保存班级
    public static void saveBanji(Context context, String qq){
        PreferencesUtils.putString(context, "qq", qq);
    }

    //保存电话
    public static void savePhone(Context context, String phone){
        PreferencesUtils.putString(context, "phone", phone);
    }

    //保存邮箱3
    public static void saveEmail(Context context, String email){
        PreferencesUtils.putString(context, "email", email);
    }

    //获取密码
    public static String getPassword(Context context){
        return PreferencesUtils.getString(context, "password", "");
    }

    //保存密码
    public static void savePassword(Context context, String password){
        PreferencesUtils.putString(context, "password", password);
    }

    //保存老师id
    public static void saveTeacherId(Context context, String id){
        PreferencesUtils.putString(context, "id", id);
    }

    /**
     * 保存首次启动标记
     */
    public static void Launched() {
        PreferencesUtils.putBoolean(AppContext.getInstance(), "isLaunched", true);
    }

    /**
     * 获取首次启动标记
     */
    public static boolean getLaunched() {
        return PreferencesUtils.getBoolean(AppContext.getInstance(), "isLaunched", false);
    }


    /**
     * 保存首次启动标记
     */
    public static void LaunchedCourseDetail() {
        PreferencesUtils.putBoolean(AppContext.getInstance(), "isLaunchedCourseDetail", true);
    }

    /**
     * 获取首次启动标记
     */
    public static boolean getLaunchedCourseDetail() {
        return PreferencesUtils.getBoolean(AppContext.getInstance(), "isLaunchedCourseDetail", false);
    }

    /**
     *  判断是否免登陆
     * @return
     */
    public static boolean isLogin() {
        if(!TextUtils.isEmpty(getUserId(AppContext.getInstance()))) {
            return true;  //已经登录
        }else{
            return false;  //没有登录
        }
    }

    //获取用户手机号
    public static String getUserPhone(Context context){
        return PreferencesUtils.getString(context, "userPhone", "");
    }

    //保存教研组名称
    public static void saveTeacherJyzName(Context context, String JyzName){
        PreferencesUtils.putString(context, "JyzName", JyzName);
    }

    //获取教研组名称
    public static String getTeacherJyzName(Context context){
        return PreferencesUtils.getString(context, "JyzName", "");
    }


    public static String getUserSex(Context context){
        int sex = PreferencesUtils.getInt(context, "userSex", 0);
        if(sex== 1){
            return "男";
        }else if(sex == 2){
            return "女";
        }else{
            return "";
        }
    }

    //保存性别
    public static void saveUserSex(Context context, int sex){
        PreferencesUtils.putInt(context, "userSex", sex);
    }

    public static String getUserUnit(Context context){
        return PreferencesUtils.getString(context, "userUnit", "暂无填写单位");
    }

    public static void saveUserUnit(Context context , String UserUnit){
        PreferencesUtils.putString(context, "userUnit", UserUnit);
    }

    /**
     * 老师的用户ID
     * @param context
     * @return
     */
    public static String getUserId(Context context) {
        return PreferencesUtils.getString(context, "userId", "");
    }

    public static void cleanLogin() {
        PreferencesUtils.putString(AppContext.getInstance(), "userId", "");
    }

    /**
     * 老师的用户ID
     * @param context
     * @return
     */
    public static void saveUserId(Context context , String UserId){
        PreferencesUtils.putString(context, "userId", UserId);
    }

    //--------------
    public static void savestartTime(Context context , String startTime){
        PreferencesUtils.putString(context, "startTime", startTime);
    }

    public static void saveendTime(Context context , String endTime){
        PreferencesUtils.putString(context, "endTime", endTime);
    }

    public static void savePracticeStatus(Context context , String status){
        PreferencesUtils.putString(context, "savePracticeStatus", status);
    }

    public static void savePracticeId(Context context , String id){
        PreferencesUtils.putString(context, "savePracticeId", id);
    }

    public static void savepostName(Context context , String postName){
        PreferencesUtils.putString(context, "postName", postName);
    }

    public static void savecpName(Context context , String cpName){
        PreferencesUtils.putString(context, "cpName", cpName);
    }

    public static void savedepName(Context context , String depName){
        PreferencesUtils.putString(context, "depName", depName);
    }

    public static void saveCTeacherName(Context context , String className){
        PreferencesUtils.putString(context, "cteacherName", className);
    }

    public static void saveSchoolTeacherNames(Context context , String depName){
        PreferencesUtils.putString(context, "steacherNames", depName);
    }

    public static void saveclassName(Context context , String className){
        PreferencesUtils.putString(context, "className", className);
    }

    public static void saveApplyStatus(Context context , int applyStatus){
        PreferencesUtils.putInt(context, "applyStatus", applyStatus);
    }

    public static void saveApplyId(Context context , int applyId){
        PreferencesUtils.putInt(context, "applyId", applyId);
    }
    //---

    /**
     * 获取专业名称
     * @param context
     * @return
     */
    public static String getdepName(Context context) {
        return PreferencesUtils.getString(context, "depName", "");
    }

    /**
     * 获取班级名称
     * @param context
     * @return
     */
    public static String getclassName(Context context) {
        return PreferencesUtils.getString(context, "className", "");
    }

    public static String getSchoolTeacherName(Context context) {
        return PreferencesUtils.getString(context, "steacherNames", "");
    }

    public static String getCTeacherName(Context context) {
        return PreferencesUtils.getString(context, "cteacherName", "");
    }

    public static String getstartTime(Context context) {
        return PreferencesUtils.getString(context, "startTime", "");
    }

    public static String getendTime(Context context) {
        return PreferencesUtils.getString(context, "endTime", "");
    }

    public static String getPracticeStatus(Context context) {
        return PreferencesUtils.getString(context, "savePracticeStatus", "实习前");
    }

    public static String getPracticeId(Context context) {
        return PreferencesUtils.getString(context, "savePracticeId", "-1");
    }

    public static String getpostName(Context context) {
        return PreferencesUtils.getString(context, "postName", "");
    }

    public static String getcpName(Context context) {
        return PreferencesUtils.getString(context, "cpName", "");
    }

    public static int getApplyStatus(Context context) {
        return PreferencesUtils.getInt(context, "applyStatus", -2);
    }

    public static int getApplyId(Context context) {
        return PreferencesUtils.getInt(context, "applyId", -1);
    }

    /**
     * 获取用户账户
     * @param context
     * @return
     */
    public static String getUsername(Context context){
        return PreferencesUtils.getString(context, "username", "");
    }

    /**
     * 保存角色
     * @param context
     * @param groupCode
     */
    public static void saveRole(Context context , String groupCode){
        PreferencesUtils.putString(context, "role", groupCode);
    }
    /**
     * 获取角色
     * @param context
     * @return
     */
    public static String getRole(Context context){
        return PreferencesUtils.getString(context, "role", "");
    }

    /**
     * 保存用户账户
     * @param context
     * @param account
     */
    public static void saveUsername(Context context , String account){
        PreferencesUtils.putString(context, "username", account);
    }

    /**
     * 储存用户头像
     * @param context
     * @param url
     */
    public static void saveUserHeadImageUri(Context context, String url) {
        PreferencesUtils.putString(context, "head_url", url);
    }

    public static String getUserHeadImageUri(Context context) {
        return PreferencesUtils.getString(context, "head_url", "");
    }

    /**
     *  取出用户登录信息
     */
    public static LoginEntity getLoginEntity(Context context) {
        String json = PreferencesUtils.getString(context, "login_entity", "");
        if(TextUtils.isEmpty(json)) {
            return null;
        }else{
            return AbGsonUtil.json2Bean(json, LoginEntity.class);
        }
    }

    /**
     * 缓存用户登录信息
     */
    public static void saveLoginEntity(Context context, LoginEntity loginEntity) {
        String json = AbGsonUtil.bean2Json(loginEntity);
        PreferencesUtils.putString(context, "login_entity", json);
    }

    /**
     * 缓存用户登录信息
     */
    public static void clearLoginEntity(Context context) {
        PreferencesUtils.putString(context, "login_entity", "");
    }

    /**
     * 缓存通讯录信息（不包含自己）
     */
    public static void clearAddressBookJson(Context context) {
        PreferencesUtils.putString(context, "address_book_entity", "");
    }


    /**
     * 清除通讯录信息
     */
    public static void clearAddressBookEntity(Context context) {
        PreferencesUtils.putString(context, "address_book_entity", "");
    }

    /**
     * 获取IP
     */
    public static String getIP(Context context) {
        return PreferencesUtils.getString(context, "ip", "");
    }

    /**
     * 保存IP
     */
    public static void saveIP(Context context, String ip) {
        PreferencesUtils.putString(context, "ip", ip);
    }
    /**
     * 获取环信账号
     */
    public static String getHuanxinAccount(Context context) {
        return PreferencesUtils.getString(context, "huanxin_account", "");
    }

    /**
     * 保存环信账号
     */
    public static void saveHuanxinAccount(Context context, String huanxin_account) {
        PreferencesUtils.putString(context, "huanxin_account", huanxin_account);
    }
    /**
     * 获取环信密码
     */
    public static String getHuanxinPassword(Context context) {
        return PreferencesUtils.getString(context, "huanxin_password", "");
    }

    /**
     * 保存环信密码
     */
    public static void saveHuanxinPassword(Context context, String huanxin_password) {
        PreferencesUtils.putString(context, "huanxin_password", huanxin_password);
    }

//    /**
//     *  取出searchHistoryEntity信息
//     */
//    public static SearchHistoryEntity getSearchHistoryEntity(Context context) {
//        String json = PreferencesUtils.getString(context, "search_history_Entity", "");
//        if(TextUtils.isEmpty(json)) {
//            return null;
//        } else {
//            return AbGsonUtil.json2Bean(json, SearchHistoryEntity.class);
//        }
//    }
//
//    /**
//     * 缓存searchHistoryEntity信息
//     */
//    public static void saveSearchHistoryEntity(Context context, SearchHistoryEntity searchHistoryEntity) {
//        String json = AbGsonUtil.bean2Json(searchHistoryEntity);
//        PreferencesUtils.putString(context, "search_history_Entity", json);
//    }

    /**
     * 清除searchHistoryEntity信息
     */
    public static void clearSearchHistoryEntity(Context context) {
        PreferencesUtils.putString(context, "search_history_Entity", "");
    }

}
