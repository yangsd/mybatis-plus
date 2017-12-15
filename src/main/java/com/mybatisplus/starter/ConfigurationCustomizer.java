package com.mybatisplus.starter;

import org.apache.ibatis.session.Configuration;

public interface ConfigurationCustomizer {
    void customize(Configuration configuration);
}
