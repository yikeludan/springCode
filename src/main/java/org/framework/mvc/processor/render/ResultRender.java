package org.framework.mvc.processor.render;

import org.framework.mvc.RequestProcessorChain;
import org.framework.mvc.processor.RequestProcessor;

public interface ResultRender {
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
