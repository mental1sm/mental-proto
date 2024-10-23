package com.mentalism.server.pipeline;

@FunctionalInterface
public interface PipelineConfigurator {
    void configure(PipelineHolder holder);
}
