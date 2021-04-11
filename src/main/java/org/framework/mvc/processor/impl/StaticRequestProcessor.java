package org.framework.mvc.processor.impl;

import org.framework.mvc.RequestProcessorChain;
import org.framework.mvc.processor.RequestProcessor;

/**
 * 静态文件处理类
 */
public class StaticRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
