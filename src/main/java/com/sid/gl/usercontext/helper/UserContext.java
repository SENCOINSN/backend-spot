package com.sid.gl.usercontext.helper;



public class UserContext {
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static String getCurrentUser(){
        return currentUser.get();
    }

    public static void setCurrentUser(String  user){
        currentUser.set(user);
    }

    public static void clear(){
        currentUser.remove();
    }
}
