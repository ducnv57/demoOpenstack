package com.samsunguet.sev_user.mycloud.object;


import com.samsunguet.sev_user.mycloud.api.HttpConstant;
import com.samsunguet.sev_user.mycloud.log.MyLog;

/**
 * Created by sev_user on 3/5/2016.
 */
public class User {
    String username;
    String password;
    Token token;
    Tenant tenant;
    String storageUrl;

    public User(){}
    public User(String username, String password, Token token, Tenant tenant){
        this.username = username;
        this.password = password;
        this.token    = token;
        this.tenant   = tenant;
    }

    public User(String username, String password, Token token, Tenant tenant, String storageUrl){
        this.username = username;
        this.password = password;
        this.token    = token;
        this.tenant   = tenant;
        this.storageUrl = storageUrl;
    }

    public User(String username, String password, Tenant tenant){
        this.username = username;
        this.password = password;
        this.tenant   = tenant;
    }

    public String getUsername(){return username!=null ? username:"";}
    public String getPassword(){return password!=null ? password:"";}
    public Token getToken(){return token;}
    public Tenant getTenant() {return tenant;}
    public String getStorageUrl(){return storageUrl!=null ? storageUrl:"";}

    public void setStorageUrl(String url){
        MyLog.log("start set storage url: " + url);
        String arrstr[] = url.split("/");
        //url.replace("http://10.0.2.15", MainActivity.URL_DEFAULT);
        url = HttpConstant.URL_DEFAULT+":8080/"+arrstr[3]+"/"+arrstr[4];
        MyLog.log("after replace: " + url);
        this.storageUrl = url;}
    public void setToken(Token token){this.token = token;}

    public String toString(){
        return "name: "+ username + "\npass: "+password+"\ntokenid: "+token.getId()+
                "\n tenant: "+tenant.getName()+"\nstorageurl: "+storageUrl;
    }
    public void setName(String name){this.username = name;}
    public void setPassword(String password){this.password = password;}
    public void setTenant(Tenant tenant){this.tenant = tenant;}

}