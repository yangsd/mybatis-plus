package com.mybatisplus.model;



import com.mybatisplus.activerecord.Model;
import com.mybatisplus.annotations.TableId;

import java.io.Serializable;

/**
 * @author sdyang
 * @create 2017-12-12 15:55
 **/
public class BaseModel<T extends Model> extends Model<T> {

    @TableId("id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected  Serializable pkVal(){
        return this.id;
    }
}
