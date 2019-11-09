package com.github.mouse0w0.ecs.util;

import java.util.Arrays;

public class IntArray {

    private int[] array;

    public IntArray() {
        this(64);
    }

    public IntArray(int capacity) {
        array = new int[capacity];
    }

    public int get(int index) {
        return array[index];
    }

    public void set(int index, int value) {
        ensureCapacity(index + 1);
        array[index] = value;
    }

    public void unsafeSet(int index, int value) {
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
        int[] newArray = new int[newCapacity];
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
        IntArray intArray = (IntArray) o;
        return Arrays.equals(array, intArray.array);
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
