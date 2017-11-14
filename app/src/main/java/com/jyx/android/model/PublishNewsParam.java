package com.jyx.android.model;

/**
 * 发布产品
 */
public class PublishNewsParam {
    private String function;
    private String theme;
    private String userid;
    private String imagejson;//发布人ID(不能为空)
    private String discribe;
    private String seemanjson;
    private String remindmanjson;
    private String newstypeid;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImagejson() {
        return imagejson;
    }

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public String getDiscribe() {
        return discribe;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public String getSeemanjson() {
        return seemanjson;
    }

    public void setSeemanjson(String seemanjson) {
        this.seemanjson = seemanjson;
    }

    public String getRemindmanjson() {
        return remindmanjson;
    }

    public void setRemindmanjson(String remindmanjson) {
        this.remindmanjson = remindmanjson;
    }

    public String getNewstypeid() {
        return newstypeid;
    }

    public void setNewstypeid(String newstypeid) {
        this.newstypeid = newstypeid;
    }
}
