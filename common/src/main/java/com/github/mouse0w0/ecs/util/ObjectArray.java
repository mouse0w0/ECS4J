package com.github.mouse0w0.ecs.util;

import java.util.Arrays;

public class ObjectArray<T> {
    private T[] array;

    public ObjectArray() {
        this(64);
    }

    public ObjectArray(int capacity) {
        array = (T[]) new Object[capacity];
    }

    public T get(int index) {
        return array[index];
    }

    public void set(int index, T value) {
        ensureCapacity(index + 1);
        array[index] = value;
    }

    public void unsafeSet(int index, T value) {
        array[index] = value;
    }

    public void ensureCapacity(int minCapacity) {
        if (array.length < minCapacity) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = array.length;
        int newCapacity = oldCapacity + oldCapacity << 1;
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, oldCapacity);
        array = newArray;
    }

    public int size() {
        return array.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectArray<?> that = (ObjectArray<?>) o;
        return Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
