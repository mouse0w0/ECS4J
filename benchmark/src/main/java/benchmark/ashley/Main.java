package benchmark.ashley;

import com.badlogic.ashley.core.Engine;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        engine.addSystem(new MoveSystem());

        for (int i = 0; i < 0x10000; i++) {
            engine.addEntity(engine.createEntity()
                    .add(new Position(Math.random(), Math.random(), Math.random()))
                    .add(new Velocity(Math.random(), Math.random(), Math.random())));
        }
        System.out.println("Generated entities.");

        int count = 0;
        double totalTime = 0;
        while (count < 10000) {
            long startTime = System.nanoTime();
            engine.update(0);
            totalTime += (System.nanoTime() - startTime) / 1e6;
            count++;
        }
        System.out.println(count + ":" + totalTime / count);
    }
}
