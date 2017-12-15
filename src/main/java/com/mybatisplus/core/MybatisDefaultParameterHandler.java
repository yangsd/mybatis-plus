package com.mybatisplus.core;

import com.mybatisplus.entity.TableInfo;
import com.mybatisplus.enums.IdType;
import com.mybatisplus.mapper.MetaObjectHandler;
import com.mybatisplus.util.*;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author sdyang
 * @create 2017-12-11 11:46
 **/
public class MybatisDefaultParameterHandler extends DefaultParameterHandler {

    private static final Log logger = LogFactory.getLog(MybatisDefaultParameterHandler.class);

    private static final Field additionalParametersField = getAdditionalParametersField();
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private BoundSql boundSql;
    private Configuration configuration;

    public MybatisDefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        super(mappedStatement, processBatch(mappedStatement, parameterObject), boundSql);
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }

    /**
     * 反射获取BoundSql中additionalParameters参数字段
     *
     * @return
     * @see org.apache.ibatis.mapping.BoundSql
     */
    private static Field getAdditionalParametersField() {
        try {
            Field additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
            return additionalParametersField;
        } catch (NoSuchFieldException e) {
            // ignored, Because it will never happen.
        }
        return null;
    }

    /**
     * <p>
     * 批量（填充主键 ID）
     * </p>
     *
     * @param ms
     * @param parameterObject 插入数据库对象
     * @return
     */
    protected static Object processBatch(MappedStatement ms, Object parameterObject) {
        //检查parameterObject
        if (null == parameterObject) {
            return null;
        }
        boolean isFill = false;
        // 全局配置是否配置填充器
        MetaObjectHandler metaObjectHandler = GlobalConfigUtils.getMetaObjectHandler(ms.getConfiguration());
        /* 只处理插入或更新操作 */
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            isFill = true;
        } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE
                && metaObjectHandler.openUpdateFill()) {
            isFill = true;
        }
        if (isFill) {
            Collection<Object> parameters = getParameters(parameterObject);
            if (null != parameters) {
                List<Object> objList = new ArrayList<Object>();
                for (Object parameter : parameters) {
                    TableInfo tableInfo = TableInfoHelper.getTableInfo(parameter.getClass());
                    if (null != tableInfo) {
                        objList.add(populateKeys(metaObjectHandler, tableInfo, ms, parameter));
                    } else {
                        /*
                         * 非表映射类不处理
						 */
                        objList.add(parameter);
                    }
                }
                return objList;
            } else {
                TableInfo tableInfo = null;
                if (parameterObject instanceof Map) {
                    Map map = (Map) parameterObject;
                    if (map.containsKey("et")) {
                        Object et = map.get("et");
                        if (et != null) {
                            if (et instanceof Map) {
                                Map realEtMap = (Map) et;
                                if (realEtMap.containsKey("MP_OPTLOCK_ET_ORIGINAL")) {//refer to OptimisticLockerInterceptor.MP_OPTLOCK_ET_ORIGINAL
                                    tableInfo = TableInfoHelper.getTableInfo(realEtMap.get("MP_OPTLOCK_ET_ORIGINAL").getClass());
                                }
                            } else {
                                tableInfo = TableInfoHelper.getTableInfo(et.getClass());
                            }
                        }
                    }
                } else {
                    tableInfo = TableInfoHelper.getTableInfo(parameterObject.getClass());
                }
                return populateKeys(metaObjectHandler, tableInfo, ms, parameterObject);
            }
        }
        return parameterObject;
    }

    /**
     * <p>
     * 处理正常批量插入逻辑
     * </p>
     * <p>
     * org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap 该类方法
     * wrapCollection 实现 StrictMap 封装逻辑
     * </p>
     *
     * @param parameter 插入数据库对象
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static Collection<Object> getParameters(Object parameter) {
        Collection<Object> parameters = null;
        if (parameter instanceof Collection) {
            parameters = (Collection) parameter;
        } else if (parameter instanceof Map) {
            Map parameterMap = (Map) parameter;
            if (parameterMap.containsKey("collection")) {
                parameters = (Collection) parameterMap.get("collection");
            } else if (parameterMap.containsKey("list")) {
                parameters = (List) parameterMap.get("list");
            } else if (parameterMap.containsKey("array")) {
                parameters = Arrays.asList((Object[]) parameterMap.get("array"));
            }
        }
        return parameters;
    }

    /**
     * <p>
     * 自定义元对象填充控制器
     * </p>
     *
     * @param metaObjectHandler 元数据填充处理器
     * @param tableInfo         数据库表反射信息
     * @param ms                MappedStatement
     * @param parameterObject   插入数据库对象
     * @return Object
     */
    protected static Object populateKeys(MetaObjectHandler metaObjectHandler, TableInfo tableInfo,
                                         MappedStatement ms, Object parameterObject) {
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getKeyProperty()) || null == tableInfo.getIdType()) {
            /* 不处理 */
            return parameterObject;
        }
        /* 自定义元对象填充控制器 */
        MetaObject metaObject = ms.getConfiguration().newMetaObject(parameterObject);
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            if (tableInfo.getIdType().getKey() >= 2) {
                Object idValue = metaObject.getValue(tableInfo.getKeyProperty());
                /* 自定义 ID */
                if (StringUtils.checkValNull(idValue)) {
                    if (tableInfo.getIdType() == IdType.ID_WORKER) {
                        metaObject.setValue(tableInfo.getKeyProperty(), IdWorker.getId());
                    } else if (tableInfo.getIdType() == IdType.UUID) {
                        metaObject.setValue(tableInfo.getKeyProperty(), IdWorker.get32UUID());
                    }
                }
            }
            // 插入填充
            if (metaObjectHandler.openInsertFill()) {
                metaObjectHandler.insertFill(metaObject);
            }
        } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE && metaObjectHandler.openUpdateFill()) {
            // 更新填充
            metaObjectHandler.updateFill(metaObject);
        }
        return metaObject.getOriginalObject();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void setParameters(PreparedStatement ps) {
        // 反射获取动态参数
        Map<String, Object> additionalParameters = null;
        try {
            additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
        } catch (IllegalAccessException e) {
            // ignored, Because it will never happen.
        }
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {//issue#448 ask first for additional params
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                        if (value == null && MapUtils.isNotEmpty(additionalParameters)) {
                            // issue #138
                            value = additionalParameters.get(propertyName);
                        }
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    try {
                        typeHandler.setParameter(ps, i + 1, value, jdbcType);
                    } catch (SQLException e) {
                        throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
                    }
                }
            }
        }
    }
}
