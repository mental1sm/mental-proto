package com.mentalism.server;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class PipelineHolder {
    private final Class<? extends ChannelHandler>[] handlerClasses;


    @SafeVarargs
    public PipelineHolder(Class<? extends ChannelHandler>... handlerClasses) {
        this.handlerClasses = handlerClasses;
    }

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
}
