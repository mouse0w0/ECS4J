package com.github.mouse0w0.ecs;

import java.util.Random;

public class ObjectAndPrimitiveArrayPerformanceTest {

    public static void main(String[] args) {
        testObjectArrayPerformance();
        testPrimitiveArrayPerformance();
    }

    public static void testObjectArrayPerformance() {
        IntComponent[] array = createArray();
        System.gc();
        long sum = 0;
        long start = System.nanoTime();
        for (int i = 0, length = array.length; i < length; i++) {
            sum += array[i].i;
        }
        System.out.println(System.nanoTime() - start);
    }

    public static void testPrimitiveArrayPerformance() {
        int[] array = createIntArray();
        System.gc();
        long sum = 0;
        long start = System.nanoTime();
        for (int i = 0, length = array.length; i < length; i++) {
            sum += array[i];
        }
        System.out.println(System.nanoTime() - start);
    }

    public static int[] createIntArray() {
        int[] intComponents = new int[32768];
        Random random = new Random();
        for (int i = 32767; i >= 0; i--) {
            intComponents[i] = random.nextInt(32768);
        }
        return intComponents;
    }

    public static IntComponent[] createArray() {
        IntComponent[] intComponents = new IntComponent[32768];
        Random random = new Random();
        for (int i = 32767; i >= 0; i--) {
            intComponents[i] = new IntComponent(random.nextInt(32768));
        }
        return intComponents;
    }

    public static class IntComponent {
        public int i;

        public IntComponent(int i) {
            this.i = i;
        }
    }
}
