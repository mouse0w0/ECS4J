package com.github.mouse0w0.ecs.system.invoker;

public interface SystemInvoker {
    void update(int entityId) throws Exception;
}
