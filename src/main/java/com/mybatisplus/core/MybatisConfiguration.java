package com.mybatisplus.core;

import com.mybatisplus.util.GlobalConfigUtils;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * @author sdyang
 * @create 2017-12-11 11:46
 **/
public class MybatisConfiguration extends Configuration {

    private static final Log logger = LogFactory.getLog(MybatisConfiguration.class);

    /*
     * Mapper 注册
     */
    public final MybatisMapperRegistry mybatisMapperRegistry = new MybatisMapperRegistry(this);

    /**
     * 初始化调用
     */
    public MybatisConfiguration() {
        this.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        logger.debug("Mybatis-plus init success.");
    }

    /**
     * <p>
     * MybatisPlus 加载 SQL 顺序：
     * </p>
     * 1、加载XML中的SQL<br>
     * 2、加载sqlProvider中的SQL<br>
     * 3、xmlSql 与 sqlProvider不能包含相同的SQL<br>
     * <br>
     * 调整后的SQL优先级：xmlSql > sqlProvider > curdSql <br>
     */
    @Override
    public void addMappedStatement(MappedStatement ms) {
        logger.debug("addMappedStatement: " + ms.getId());
        if (GlobalConfigUtils.isRefresh(ms.getConfiguration())) {
            /*
             * 支持是否自动刷新 XML 变更内容，开发环境使用【 注：生产环境勿用！】
			 */
            this.mappedStatements.remove(ms.getId());
        } else {
            if (this.mappedStatements.containsKey(ms.getId())) {
                /*
                 * 说明已加载了xml中的节点； 忽略mapper中的SqlProvider数据
				 */
                logger.error("mapper[" + ms.getId() + "] is ignored, because it's exists, maybe from xml file");
                return;
            }
        }
        super.addMappedStatement(ms);
    }

    @Override
    public void setDefaultScriptingLanguage(Class<?> driver) {
        if (driver == null) {
            /* 设置自定义 driver */
            driver = MybatisXMLLanguageDriver.class;
        }
        super.setDefaultScriptingLanguage(driver);
    }

    @Override
    public MapperRegistry getMapperRegistry() {
        return mybatisMapperRegistry;
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        mybatisMapperRegistry.addMapper(type);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mybatisMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        mybatisMapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mybatisMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return mybatisMapperRegistry.hasMapper(type);
    }
}
