
package com.mybatisplus.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.mybatisplus.core.MybatisSqlSessionTemplate;
import com.mybatisplus.enums.DBType;
import com.mybatisplus.enums.FieldStrategy;
import com.mybatisplus.enums.IdType;
import com.mybatisplus.incrementer.IKeyGenerator;
import com.mybatisplus.mapper.ISqlInjector;
import com.mybatisplus.mapper.MetaObjectHandler;
import com.mybatisplus.util.GlobalConfigUtils;
import com.mybatisplus.util.JdbcUtils;
import com.mybatisplus.util.SqlReservedWords;
import com.mybatisplus.util.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


/**
 * <p>
 * Mybatis全局缓存
 * </p>
 *
 * @author Caratacus
 * @since 2016-12-06
 */
public class GlobalConfiguration implements Serializable {

    // 逻辑删除全局值
    private String logicDeleteValue = null;
    // 逻辑未删除全局值
    private String logicNotDeleteValue = null;
    // 数据库类型
    private DBType dbType;
    // 主键类型（默认 ID_WORKER）
    private IdType idType = IdType.ID_WORKER;
    // 表名、字段名、是否使用下划线命名（默认 false）
    private boolean dbColumnUnderline = false;
    // SQL注入器
    private ISqlInjector sqlInjector;
    // 表关键词 key 生成器
    private IKeyGenerator keyGenerator;
    // 元对象字段填充控制器
    private MetaObjectHandler metaObjectHandler = new DefaultMetaObjectHandler();
    // 字段验证策略
    private FieldStrategy fieldStrategy = FieldStrategy.NOT_NULL;
    // 是否刷新mapper
    private boolean isRefresh = false;
    // 是否大写命名
    private boolean isCapitalMode = false;
    // 标识符
    private String identifierQuote;
    // 缓存当前Configuration的SqlSessionFactory
    private SqlSessionFactory sqlSessionFactory;
    // 缓存已注入CRUD的Mapper信息
    private Set<String> mapperRegistryCache = new ConcurrentSkipListSet<String>();
    // 单例重用SqlSession
    private SqlSession sqlSession;

    public GlobalConfiguration() {
        // 构造方法
    }

    public GlobalConfiguration(ISqlInjector sqlInjector) {
        this.sqlInjector = sqlInjector;
    }


    public IKeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(IKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
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

    public DBType getDbType() {
        return dbType;
    }

    /**
     * 根据jdbcUrl设置数据库类型
     *
     * @param jdbcUrl
     */
    public void setDbTypeOfJdbcUrl(String jdbcUrl) {
        this.dbType = JdbcUtils.getDbType(jdbcUrl);
    }

    public void setDbType(String dbType) {
        this.dbType = DBType.getDBType(dbType);
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = IdType.getIdType(idType);
    }

    public boolean isDbColumnUnderline() {
        return dbColumnUnderline;
    }

    public void setDbColumnUnderline(boolean dbColumnUnderline) {
        this.dbColumnUnderline = dbColumnUnderline;
    }

    public ISqlInjector getSqlInjector() {
        return sqlInjector;
    }

    public void setSqlInjector(ISqlInjector sqlInjector) {
        this.sqlInjector = sqlInjector;
    }

    public MetaObjectHandler getMetaObjectHandler() {
        return metaObjectHandler;
    }

    public void setMetaObjectHandler(MetaObjectHandler metaObjectHandler) {
        this.metaObjectHandler = metaObjectHandler;
    }

    public FieldStrategy getFieldStrategy() {
        return fieldStrategy;
    }

    public void setFieldStrategy(int fieldStrategy) {
        this.fieldStrategy = FieldStrategy.getFieldStrategy(fieldStrategy);
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        this.isRefresh = refresh;
    }

    public Set<String> getMapperRegistryCache() {
        return mapperRegistryCache;
    }

    public void setMapperRegistryCache(Set<String> mapperRegistryCache) {
        this.mapperRegistryCache = mapperRegistryCache;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSession = new MybatisSqlSessionTemplate(sqlSessionFactory);
    }

    public boolean isCapitalMode() {
        return isCapitalMode;
    }

    public void setCapitalMode(boolean isCapitalMode) {
        this.isCapitalMode = isCapitalMode;
    }

    public String getIdentifierQuote() {
        if (null == identifierQuote) {
            return dbType.getQuote();
        }
        return identifierQuote;
    }

    public void setIdentifierQuote(String identifierQuote) {
        this.identifierQuote = identifierQuote;
    }

    public void setSqlKeywords(String sqlKeywords) {
        if (StringUtils.isNotEmpty(sqlKeywords)) {
            SqlReservedWords.RESERVED_WORDS.addAll(StringUtils.splitWorker(sqlKeywords.toUpperCase(), ",", -1, false));
        }
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }


    /**
     * <p>
     * 标记全局设置 (统一所有入口)
     * </p>
     *
     * @param sqlSessionFactory
     * @return
     */
    public SqlSessionFactory signGlobalConfig(SqlSessionFactory sqlSessionFactory) {
        if (null != sqlSessionFactory) {
            GlobalConfigUtils.setGlobalConfig(sqlSessionFactory.getConfiguration(), this);
        }
        return sqlSessionFactory;
    }
}
