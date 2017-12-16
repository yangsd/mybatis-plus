package com.mybatisplus.model;

import com.mybatisplus.annotations.TableField;
import com.mybatisplus.annotations.TableName;

import java.util.Date;

/**
 * @author sdyang
 * @create 2017-12-12 16:11
 **/
@TableName("user")
public class UserModel extends BaseModel<UserModel> {

    private String name;

    private int age;

    private String phone;

    @TableField(exist = false)
    private String address;

    private Date birth_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }
}
