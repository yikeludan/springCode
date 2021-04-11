package org.framework.mvc.processor.impl;

import org.framework.mvc.RequestProcessorChain;
import org.framework.mvc.processor.RequestProcessor;

/**
 * 请求预处理 包括编码
 */
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
