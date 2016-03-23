package com.samsunguet.sev_user.mycloud.object;

import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.log.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by sev_user on 3/5/2016.
 */
public class Token {
    static final String ISSUED      = "issued_at";
    static final String EXPIRES     = "expires";
    static final String ID          = "id";


    String id;
    String expires;
    String issued;

    public Token(){}
    public Token(String tokenId, String expires, String issued){
        this.id = tokenId;
        this.expires = expires;
        this.issued = issued;
    }

    public Token(String id){
        this.id = id;
    }
    public void setToken(String jsonstr){
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            id          = jsonObject.getString(ID);
            issued      = jsonObject.getString(ISSUED);
            expires     = jsonObject.getString(EXPIRES);
            MyLog.log(expires);

        } catch (JSONException e) {
            //e.printStackTrace();
            MyLog.log(e.toString());
        }
    }


    public String getId(){return id!=null ? id:"not set token";}
    public String getExpires(){return expires!=null ? expires:"";}
    public String getIssued(){return issued!=null ? issued:"";}
    public boolean isExpire(){
        MyLog.log("get in isExpire");
        MyLog.log(expires);
        try{

            Date ep = DataConstant.getDatefromString(expires);
            MyLog.log("expire:" + ep.toString());
            Date now = new Date(System.currentTimeMillis());

            MyLog.log("now: " + now.toString());
            if(ep.before(now)) return false;

        }catch (Exception e){
            MyLog.log(e.toString());
        }
        return true;
    }
}
