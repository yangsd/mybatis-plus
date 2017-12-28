package com.mybatisplus.dynamic;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by tommy's father on 2017/5/18.
 */
@Component
public class DynamicSqlHelper {

    @Autowired
    private SqlSession sqlSession;

    private static DynamicSqlHelper sqlHelper = null;

    @PostConstruct
    public void init() {
        sqlHelper = this;
    }

    public static Map<String,Object> selectOne(String sql){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectOne(sql);
    }

    public static Map<String,Object> selectOne(String sql, Object value){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectOne(sql,value);
    }

    public static <T> T selectOne(String sql, Class<T> resultType){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectOne(sql,resultType);
    }

    public static <T> T selectOne(String sql, Object value, Class<T> resultType){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectOne(sql,value,resultType);
    }

    public static List<Map<String,Object>> selectList(String sql){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectList(sql);
    }

    public static List<Map<String,Object>> selectList(String sql, Object value){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectList(sql,value);
    }

    public static <T> List<T> selectList(String sql, Class<T> resultType){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectList(sql,resultType);
    }

    public static <T> List<T> selectList(String sql, Object value, Class<T> resultType){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.selectList(sql,value,resultType);
    }

    public static int insert(String sql){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.insert(sql);
    }

    public static int insert(String sql, Object value){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.insert(sql,value);

    }

    public static int update(String sql){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.update(sql);
    }

    public static int update(String sql, Object value){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.update(sql,value);
    }

    public static int delete(String sql){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.update(sql);
    }

    public static int delete(String sql, Object value){
        SqlMapper sqlMapper = new SqlMapper(sqlHelper.sqlSession);
        return sqlMapper.delete(sql,value);
    }
}
