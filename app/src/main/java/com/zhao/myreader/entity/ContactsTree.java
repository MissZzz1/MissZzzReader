package com.zhao.myreader.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhao on 2016/11/9.
 * APP通迅录树结构
 *
 */

public class ContactsTree implements Serializable {

    //根节点赋值字段（部门节点）
    private String DepartId;//部门id
    private String DepartName;//部门名
    private String orgCode; //部门编码
    private String departOrder; //部门排序
    private boolean select;

    //叶节点赋值字段（个人信息节点）
    private String id;
    private String userDepartId;//用户所属部门ID
    private String userDepartName;//用户所属部门名
    private boolean moreDepartUser;// 用户是否多部门 true是  false不是
    private boolean transpondPerson;//是否转发人员 true是  false不是
    private String userName;//用户名
    private String realName;//姓名
    private String wholeSpellName;//姓名全拼
    private String firstLetterName;//拼音首字母 eg:hzh
    private String mobilePhone;//手机
    private String sex;//性别
    private String email;//邮箱

    private ArrayList<ContactsTree> children;//孩子节点

    private ContactsTree parent;//父节点

    public boolean isTranspondPerson() {
        return transpondPerson;
    }

    public void setTranspondPerson(boolean transpondPerson) {
        this.transpondPerson = transpondPerson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartOrder() {
        return departOrder;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setDepartOrder(String departOrder) {
        this.departOrder = departOrder;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public boolean isMoreDepartUser() {
        return moreDepartUser;
    }

    public void setMoreDepartUser(boolean moreDepartUser) {
        this.moreDepartUser = moreDepartUser;
    }

    public String getUserDepartId() {
        return userDepartId;
    }

    public String getUserDepartName() {
        return userDepartName;
    }

    public void setUserDepartId(String userDepartId) {
        this.userDepartId = userDepartId;
    }

    public void setUserDepartName(String userDepartName) {
        this.userDepartName = userDepartName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public ContactsTree getParent() {
        return parent;
    }

    public void setParent(ContactsTree parent) {
        this.parent = parent;
    }

    public ContactsTree(){
        children = new ArrayList<>();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public ArrayList<ContactsTree> getChildren() {
        return children;
    }

    public String getDepartId() {
        return DepartId;
    }

    public String getDepartName() {
        return DepartName;
    }

    public String getFirstLetterName() {
        return firstLetterName;
    }

    public String getRealName() {
        return realName;
    }

    public String getUserName() {
        return userName;
    }

    public String getWholeSpellName() {
        return wholeSpellName;
    }

    public void setChildren(ArrayList<ContactsTree> children) {
        this.children = children;
    }

    public void setDepartId(String departId) {
        DepartId = departId;
    }

    public void setDepartName(String departName) {
        DepartName = departName;
    }

    public void setFirstLetterName(String firstLetterName) {
        this.firstLetterName = firstLetterName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWholeSpellName(String wholeSpellName) {
        this.wholeSpellName = wholeSpellName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ContactsTree{" +
                "DepartId='" + DepartId + '\'' +
                ", DepartName='" + DepartName + '\'' +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", wholeSpellName='" + wholeSpellName + '\'' +
                ", firstLetterName='" + firstLetterName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", children=" + children +
                '}';
    }
}
