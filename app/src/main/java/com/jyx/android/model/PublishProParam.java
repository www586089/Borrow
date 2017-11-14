package com.jyx.android.model;

/**
 * 发布产品
 */
public class PublishProParam {
    private String function;
    private String name;//名称(不能为空)
    private String operatype;//买卖类型标识(不能为空)
    private String userid;//发布人ID(不能为空)
    private String featureid;//成色标识
    private String imagejson;//图片URL集合
    private String discribe;//描述
    private String price;//原价(分为单位)(不能为空)
    private String deposit;//押金(分为单位)(不能为空)
    private String typeid;//分类标识
    private String renttypeid;//租金类型标识
    private String rent;//租金(分为单位)(不能为空)
    private String discountprice;
    private String address;//物品所在地
    private String gettypeid;//取物方式标识
    private String operatypename;//买卖类型描述(不能为空)
    private String featurename;//成色描述
    private String typename;//分类描述
    private String renttypename;//租金类型描述
    private String gettypename;//取物方式描述
    private String remarks;
    private String stockNumber;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperatype() {
        return operatype;
    }

    public void setOperatype(String operatype) {
        this.operatype = operatype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFeatureid() {
        return featureid;
    }

    public void setFeatureid(String featureid) {
        this.featureid = featureid;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getRenttypeid() {
        return renttypeid;
    }

    public void setRenttypeid(String renttypeid) {
        this.renttypeid = renttypeid;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getDiscountPrice() {
        return discountprice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountprice = discountPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGettypeid() {
        return gettypeid;
    }

    public void setGettypeid(String gettypeid) {
        this.gettypeid = gettypeid;
    }

    public String getOperatypename() {
        return operatypename;
    }

    public void setOperatypename(String operatypename) {
        this.operatypename = operatypename;
    }

    public String getFeaturename() {
        return featurename;
    }

    public void setFeaturename(String featurename) {
        this.featurename = featurename;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getRenttypename() {
        return renttypename;
    }

    public void setRenttypename(String renttypename) {
        this.renttypename = renttypename;
    }

    public String getGettypename() {
        return gettypename;
    }

    public void setGettypename(String gettypename) {
        this.gettypename = gettypename;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }
}
