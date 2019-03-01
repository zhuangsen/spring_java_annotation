package com.zs.spring.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class DynamicDataSource extends AbstractRoutingDataSource {

    public static ThreadLocal key = new ThreadLocal<String>();

    @Override
    protected Object determineCurrentLookupKey() {
        return key.get();
    }
}