package com.github.mouse0w0.ecs.util;

import java.util.Arrays;

public class BitArray {
    private long[] bits;

    public BitArray() {
        this(64);
    }

    public BitArray(int bitCount) {
        bits = new long[bitCount >>> 6];
    }

    public BitArray(BitArray copyFrom) {
        bits = Arrays.copyOf(copyFrom.bits, copyFrom.bits.length);
    }

    public boolean get(int index) {
        final int bitsIndex = index >>> 6;
        return bitsIndex < bits.length && (bits[bitsIndex] & 1L << index) != 0L;
    }

    public boolean unsafeGet(int index) {
        return (bits[index >>> 6] & 1L << index) != 0L;
    }

    public void set(int index, boolean value) {
        if (value) mark(index);
        else clear(index);
    }

    public void unsafeSet(int index, boolean value) {
        if (value) unsafeMark(index);
        else unsafeClear(index);
    }

    public void mark(int index) {
        final int bitsIndex = index >>> 6;
        checkCapacity(bitsIndex);
        bits[bitsIndex] |= 1L << index;
    }

    public void unsafeMark(int index) {
        bits[index >>> 6] |= 1L << index;
    }

    public void clear(int index) {
        final int bitsIndex = index >>> 6;
        if (bitsIndex >= bits.length) return;
        bits[bitsIndex] &= ~(1L << index);
    }

    public void unsafeClear(int index) {
        bits[index >>> 6] &= ~(1L << index);
    }

    public void clear() {
        Arrays.fill(bits, 0L);
    }

    public void ensureCapacity(int bitCount) {
        checkCapacity(bitCount >>> 6);
    }

    private void checkCapacity(int minBitsLength) {
        if (minBitsLength >= bits.length) {
            grow(minBitsLength);
        }
    }

    private void grow(int minBitsLength) {
        int oldCapacity = bits.length;
        int newCapacity = oldCapacity + oldCapacity << 1;
        if (newCapacity < minBitsLength) newCapacity = minBitsLength;
        long[] newBits = new long[newCapacity];
        System.arraycopy(bits, 0, newBits, 0, oldCapacity);
        bits = newBits;
    }

    public int size() {
        return bits.length << 6;
    }

    public void and(BitArray other) {
        long[] otherBits = other.bits;
        int minLength = Math.min(bits.length, otherBits.length);
        for (int i = 0; i < minLength; i++) {
            bits[i] &= otherBits[i];
        }
        if (bits.length <= minLength) {
            for (int i = minLength, length = bits.length; i < length; i++) {
                bits[i] = 0L;
            }
        }
    }

    public void andNot(BitArray other) {
        long[] otherBits = other.bits;
        int minLength = Math.min(bits.length, otherBits.length);
        for (int i = 0; i < minLength; i++) {
            bits[i] &= ~otherBits[i];
        }
    }

    public void or(BitArray other) {
        long[] otherBits = other.bits;
        int minLength = Math.min(bits.length, otherBits.length);
        for (int i = 0; i < minLength; i++) {
            bits[i] |= otherBits[i];
        }
        if (minLength < otherBits.length) {
            checkCapacity(otherBits.length);
            for (int i = minLength, length = otherBits.length; i < length; i++) {
                bits[i] = otherBits[i];
            }
        }
    }

    public void xor(BitArray other) {
        long[] otherBits = other.bits;
        int minLength = Math.min(bits.length, otherBits.length);
        for (int i = 0; i < minLength; i++) {
            bits[i] ^= otherBits[i];
        }
        if (minLength < otherBits.length) {
            checkCapacity(otherBits.length);
            for (int i = minLength, length = otherBits.length; i < length; i++) {
                bits[i] = otherBits[i];
            }
        }
    }

    public boolean intersects(BitArray other) {
        long[] otherBits = other.bits;
        int minLength = Math.min(bits.length, otherBits.length);
        for (int i = 0; i < minLength; i++) {
            if ((bits[i] & otherBits[i]) != 0L) return true;
        }
        return false;
    }

    public boolean contains(BitArray other) {
        long[] otherBits = other.bits;
        int bitsLength = bits.length;
        int otherBitsLength = otherBits.length;

        for (int i = bitsLength; i < otherBitsLength; i++) {
            if (otherBits[i] != 0L) return false;
        }

        int minLength = Math.min(bitsLength, otherBitsLength);
        for (int i = 0; i < minLength; i++) {
            if ((bits[i] & otherBits[i]) != otherBits[i]) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitArray bitArray = (BitArray) o;
        return Arrays.equals(bits, bitArray.bits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }
}
