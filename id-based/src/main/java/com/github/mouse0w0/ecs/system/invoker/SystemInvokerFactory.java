package com.github.mouse0w0.ecs.system.invoker;

import com.github.mouse0w0.ecs.component.ComponentMapper;

import java.lang.reflect.Method;

public interface SystemInvokerFactory {
    SystemInvoker create(Object owner, Method method, ComponentMapper[] mappers);
}
