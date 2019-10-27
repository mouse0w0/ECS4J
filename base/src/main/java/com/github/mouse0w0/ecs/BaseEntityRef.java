package com.github.mouse0w0.ecs;

public class BaseEntityRef implements EntityRef {

    private final World world;

    int id;

    protected BaseEntityRef(World world, int id) {
        this.world = world;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public <T extends Component> T addComponent(T component) {
        return null;
    }

    @Override
    public void removeComponent(ComponentType type) {

    }

    @Override
    public void saveComponent(Component component) {

    }

    @Override
    public boolean hasComponent(Class<? extends Component> component) {
        return false;
    }

    @Override
    public <T extends Component> T getComponent(ComponentType type) {
        return null;
    }
}
