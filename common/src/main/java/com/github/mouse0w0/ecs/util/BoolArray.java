package com.github.mouse0w0.ecs.util;

import java.util.Arrays;

public class BoolArray {
    private boolean[] array;

    public BoolArray() {
        this(64);
    }

    public BoolArray(int capacity) {
        array = new boolean[capacity];
    }

    public boolean get(int index) {
        if (array.length <= index) return false;
        return array[index];
    }

    public boolean unsafeGet(int index) {
        return array[index];
    }

    public void set(int index, boolean value) {
        ensureCapacity(index + 1);
        array[index] = value;
    }

    public void mark(int index) {
        ensureCapacity(index + 1);
        array[index] = true;
    }

    public void clear(int index) {
        ensureCapacity(index + 1);
        array[index] = false;
    }

    public void unsafeSet(int index, boolean value) {
        array[index] = value;
    }

    public void unsafeMark(int index) {
        array[index] = true;
    }

    public void unsafeClear(int index) {
        array[index] = false;
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
        boolean[] newArray = new boolean[newCapacity];
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
        BoolArray boolArray = (BoolArray) o;
        return Arrays.equals(array, boolArray.array);
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
