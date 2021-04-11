package org.framework.mvc.processor;

import org.framework.mvc.RequestProcessorChain;

/**
 * 请求执行器
 */
public interface RequestProcessor {
    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;


}
