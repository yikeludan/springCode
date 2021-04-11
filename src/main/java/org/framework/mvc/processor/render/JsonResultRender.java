package org.framework.mvc.processor.render;

import com.google.gson.Gson;
import org.framework.mvc.RequestProcessorChain;

import java.io.PrintWriter;

public class JsonResultRender implements ResultRender {
    private Object jsonData;
    public JsonResultRender(Object result) {
        this.jsonData = result;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        //设置响应头
        requestProcessorChain.getResponse().setContentType("application/json");
        //
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");
        PrintWriter printWriter =  requestProcessorChain.getResponse().getWriter();
        Gson gson = new Gson();
        printWriter.write(gson.toJson(this.jsonData));
        printWriter.flush();
    }
}
