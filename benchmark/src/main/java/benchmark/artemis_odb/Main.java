package benchmark.artemis_odb;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;

public class Main {

    public static void main(String[] args) {
        WorldConfiguration configuration = new WorldConfigurationBuilder()
                .with(new MoveSystem()).build();

        World world = new World(configuration);

        for (int i = 0; i < 0x10000; i++) {
            int entityId = world.create();
            world.edit(entityId)
                    .add(new Position(Math.random(), Math.random(), Math.random()))
                    .add(new Velocity(Math.random(), Math.random(), Math.random()));
        }
        System.out.println("Generated entities.");

        int count = 0;
        double totalTime = 0;
        while (count < 10000) {
            long startTime = System.nanoTime();
            world.process();
            totalTime += (System.nanoTime() - startTime) / 1e6;
            count++;
        }
        System.out.println(count + ":" + totalTime / count);
    }
}
