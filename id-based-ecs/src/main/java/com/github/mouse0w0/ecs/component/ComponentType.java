package com.github.mouse0w0.ecs.component;

public final class ComponentType {

    private final int id;
    private final Class<? extends Component> type;

    public ComponentType(int id, Class<? extends Component> type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Class<? extends Component> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentType that = (ComponentType) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ComponentType{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
