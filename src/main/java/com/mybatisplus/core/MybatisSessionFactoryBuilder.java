package com.mybatisplus.core;

import com.mybatisplus.entity.GlobalConfiguration;
import com.mybatisplus.util.GlobalConfigUtils;
import com.mybatisplus.util.IOUtils;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * @author sdyang
 * @create 2017-12-11 11:47
 **/
public class MybatisSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    private static final Log logger = LogFactory.getLog(MybatisSessionFactoryBuilder.class);
    private GlobalConfiguration globalConfig = GlobalConfigUtils.defaults();

    @Override
    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
        try {
            MybatisXMLConfigBuilder parser = new MybatisXMLConfigBuilder(reader, environment, properties);
            GlobalConfigUtils.setGlobalConfig(parser.getConfiguration(), this.globalConfig);
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        try {
            MybatisXMLConfigBuilder parser = new MybatisXMLConfigBuilder(inputStream, environment, properties);
            GlobalConfigUtils.setGlobalConfig(parser.getConfiguration(), this.globalConfig);
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            IOUtils.closeQuietly(inputStream);
        }
    }

    // TODO 注入全局配置
    public void setGlobalConfig(GlobalConfiguration globalConfig) {
        this.globalConfig = globalConfig;
    }

}
