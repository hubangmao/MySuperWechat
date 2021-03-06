/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hbm.superwechat;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.easemob.EMCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hbm.superwechat.bean.GroupAvatar;
import cn.hbm.superwechat.bean.MemberUserAvatar;
import cn.hbm.superwechat.bean.UserAvatar;
import cn.hbm.superwechat.utils.Utils;

public class SuperWeChatApplication extends Application {

    public static Context applicationContext;
    private static SuperWeChatApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
    public static Utils mMyUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("main", "SuperWeChatApplication.onCreate()");
        applicationContext = this;
        instance = this;
        mMyUtils = new Utils();

        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         *
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         * for example:
         * 例子：
         *
         * public class DemoHXSDKHelper extends HXSDKHelper
         *
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        hxSDKHelper.onInit(applicationContext);
    }

    public static SuperWeChatApplication getInstance() {
        return instance;
    }


    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     * @param
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final boolean isGCM, final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(isGCM, emCallBack);
    }

    /**
     * user储存用户信息
     */
    private UserAvatar user = new UserAvatar();

    public UserAvatar getUser() {
        return user;
    }

    public void setUser(UserAvatar user) {
        this.user = user;
    }

    /**
     * 储存好友集合
     */
    List<UserAvatar> userList;

    public List<UserAvatar> getUserList() {
        return userList;
    }

    public void setUserList(List<UserAvatar> userList) {
        this.userList = userList;
        if (userList != null) {
            Log.i("main", "储存好友集合信息" + userList.toString());
        }
    }

    /**
     * 储存好友Map集合
     */
    static Map<String, UserAvatar> map = new HashMap<String, UserAvatar>();

    public Map<String, UserAvatar> getMap() {
        return map;
    }

    public void setMap(Map<String, UserAvatar> map) {
        this.map = map;
    }

    /**
     * 储存群成员信息集合
     */
    List<GroupAvatar> groupAvatarList = new ArrayList<GroupAvatar>();

    public List<GroupAvatar> getGroupAvatarList() {
        return groupAvatarList;
    }

    public void setGroupAvatarList(List<GroupAvatar> groupAvatarList) {
        this.groupAvatarList = groupAvatarList;
    }

    /**
     * 储存该用户群的群成员
     */
    Map<String, HashMap<String, MemberUserAvatar>> memberMap = new HashMap<String, HashMap<String, MemberUserAvatar>>();

    public Map<String, HashMap<String, MemberUserAvatar>> getMemberMap() {
        return memberMap;
    }

    public void setMemberMap(Map<String, HashMap<String, MemberUserAvatar>> memberMap) {
        this.memberMap = memberMap;
    }

    /**
     * 储存当前用户的所有群组Map集合
     */
    HashMap<String, GroupAvatar> groupMap = new HashMap<String, GroupAvatar>();

    public HashMap<String, GroupAvatar> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(HashMap<String, GroupAvatar> groupMap) {
        this.groupMap = groupMap;
    }
}
