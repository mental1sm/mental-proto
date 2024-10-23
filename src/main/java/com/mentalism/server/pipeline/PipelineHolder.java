package com.mentalism.server.pipeline;
import com.mentalism.server.handlers.ChannelHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class PipelineHolder {
    private final ArrayList<Class<? extends ChannelHandler>> handlerClasses = new ArrayList<>();


    public PipelineHolder() {}

    public ChannelPipeline createPipeline() {
        List<ChannelHandler> handlers = new ArrayList<>();

        for (Class<? extends ChannelHandler> handlerClass : handlerClasses) {
            try {
                Constructor<? extends ChannelHandler> constructor = handlerClass.getConstructor();
                ChannelHandler handler = constructor.newInstance();
                handlers.add(handler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return new ChannelPipeline(handlers.toArray(new ChannelHandler[0]));
    }

    public PipelineHolder addLast(Class<? extends ChannelHandler> handlerClass) {
        handlerClasses.add(handlerClass);
        return this;
    }
}
