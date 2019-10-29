package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentContainer;

public interface EntityRef extends ComponentContainer {

    int getId();

    boolean isExists();

    void destroy();
}
