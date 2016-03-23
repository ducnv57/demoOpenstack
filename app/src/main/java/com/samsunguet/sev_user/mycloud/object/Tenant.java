package com.samsunguet.sev_user.mycloud.object;

/**
 * Created by sev_user on 3/5/2016.
 */
public class Tenant {
    static final String DESCRIPTION = "description";
    static final String ENABLED     = "enabled";
    static final String ID          = "id";
    static final String NAME        = "demo";

    String id;
    String name;
    boolean enable;
    String description;

    public Tenant(String id, String name, boolean enable, String des){
        this.id     = id;
        this.name   = name;
        this.enable = enable;
        this.description = des;
    }

    public Tenant(){}
    public Tenant(String name){this.name = name;}

    public String getName(){return name!=null ? name:"";}

}
