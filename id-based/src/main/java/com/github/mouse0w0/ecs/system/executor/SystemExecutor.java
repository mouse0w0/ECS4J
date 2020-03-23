package com.github.mouse0w0.ecs.system.executor;

import java.lang.reflect.Method;

public interface SystemExecutor {
    Object getOwner();

    Method getMethod();

    void execute();
}
