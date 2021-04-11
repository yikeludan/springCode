package org.framework.mvc.processor.impl;

import org.framework.mvc.RequestProcessorChain;
import org.framework.mvc.processor.RequestProcessor;

public class JspRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
