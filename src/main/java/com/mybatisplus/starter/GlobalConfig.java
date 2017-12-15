package com.mybatisplus.starter;

import com.mybatisplus.entity.GlobalConfiguration;
import com.mybatisplus.incrementer.IKeyGenerator;
import com.mybatisplus.mapper.ISqlInjector;
import com.mybatisplus.mapper.MetaObjectHandler;
import com.mybatisplus.util.StringUtils;

/**
 * @author sdyang
 * @create 2017-12-12 9:03
 **/
public class GlobalConfig {
    // 主键类型
    private Integer idType;
    // 表名、字段名、是否使用下划线命名
    private Boolean dbColumnUnderline;
    // SQL注入器
    private String sqlInjector;
    // 元对象字段填充控制器
    private String metaObjectHandler;
    // 字段验证策略
    private Integer fieldStrategy;
    // 方便调试
    private Boolean refreshMapper;
    // 是否大写命名
    private Boolean isCapitalMode;
    // 标识符
    private String identifierQuote;
    // 逻辑删除全局值
    private String logicDeleteValue = null;
    // 逻辑未删除全局值
    private String logicNotDeleteValue = null;
    // 表关键词 key 生成器
    private String keyGenerator;

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public Boolean getDbColumnUnderline() {
        return dbColumnUnderline;
    }

    public void setDbColumnUnderline(Boolean dbColumnUnderline) {
        this.dbColumnUnderline = dbColumnUnderline;
    }

    public String getSqlInjector() {
        return sqlInjector;
    }

    public void setSqlInjector(String sqlInjector) {
        this.sqlInjector = sqlInjector;
    }

    public String getMetaObjectHandler() {
        return metaObjectHandler;
    }

    public void setMetaObjectHandler(String metaObjectHandler) {
        this.metaObjectHandler = metaObjectHandler;
    }

    public Integer getFieldStrategy() {
        return fieldStrategy;
    }

    public void setFieldStrategy(Integer fieldStrategy) {
        this.fieldStrategy = fieldStrategy;
    }

    public Boolean getCapitalMode() {
        return isCapitalMode;
    }

    public void setCapitalMode(Boolean capitalMode) {
        isCapitalMode = capitalMode;
    }

    public String getIdentifierQuote() {
        return identifierQuote;
    }

    public void setIdentifierQuote(String identifierQuote) {
        this.identifierQuote = identifierQuote;
    }

    public Boolean getRefreshMapper() {
        return refreshMapper;
    }

    public void setRefreshMapper(Boolean refreshMapper) {
        this.refreshMapper = refreshMapper;
    }

    public String getLogicDeleteValue() {
        return logicDeleteValue;
    }

    public void setLogicDeleteValue(String logicDeleteValue) {
        this.logicDeleteValue = logicDeleteValue;
    }

    public String getLogicNotDeleteValue() {
        return logicNotDeleteValue;
    }

    public void setLogicNotDeleteValue(String logicNotDeleteValue) {
        this.logicNotDeleteValue = logicNotDeleteValue;
    }

    public String getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(String keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public GlobalConfiguration convertGlobalConfiguration() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        if (StringUtils.isNotEmpty(this.getIdentifierQuote())) {
            globalConfiguration.setIdentifierQuote(this.getIdentifierQuote());
        }
        if (StringUtils.isNotEmpty(this.getLogicDeleteValue())) {
            globalConfiguration.setLogicDeleteValue(this.getLogicDeleteValue());
        }
        if (StringUtils.isNotEmpty(this.getLogicNotDeleteValue())) {
            globalConfiguration.setLogicNotDeleteValue(this.getLogicNotDeleteValue());
        }
        if (StringUtils.isNotEmpty(this.getSqlInjector())) {
            globalConfiguration.setSqlInjector((ISqlInjector) Class.forName(this.getSqlInjector()).newInstance());
        }
        if (StringUtils.isNotEmpty(this.getMetaObjectHandler())) {
            globalConfiguration.setMetaObjectHandler((MetaObjectHandler) Class.forName(this.getMetaObjectHandler()).newInstance());
        }
        if (StringUtils.isNotEmpty(this.getKeyGenerator())) {
            globalConfiguration.setKeyGenerator((IKeyGenerator) Class.forName(this.getKeyGenerator()).newInstance());
        }
        if (StringUtils.checkValNotNull(this.getIdType())) {
            globalConfiguration.setIdType(this.getIdType());
        }
        if (StringUtils.checkValNotNull(this.getDbColumnUnderline())) {
            globalConfiguration.setDbColumnUnderline(this.getDbColumnUnderline());
        }
        if (StringUtils.checkValNotNull(this.getFieldStrategy())) {
            globalConfiguration.setFieldStrategy(this.getFieldStrategy());
        }
        if (StringUtils.checkValNotNull(this.getRefreshMapper())) {
            globalConfiguration.setRefresh(this.getRefreshMapper());
        }
        if (StringUtils.checkValNotNull(this.getCapitalMode())) {
            globalConfiguration.setCapitalMode(this.getCapitalMode());
        }
        return globalConfiguration;
    }

}
