package com.example.demo.utils;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name = "threadContext", category = StrLookup.CATEGORY)
public class Log4jThreadLookup implements StrLookup {
    @Override
    public String lookup(String key) {
        return null;
    }

    @Override
    public String lookup(LogEvent event, String key) {
        return event.getContextData().getValue("fileCreatePosition");
    }
}
