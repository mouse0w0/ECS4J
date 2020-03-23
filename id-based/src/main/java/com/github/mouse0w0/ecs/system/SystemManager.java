package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.system.invoker.SystemInvokerFactory;

public interface SystemManager {

    SystemInvokerFactory getInvokerFactory();

    void register(Object object);

    void update();
}
