package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.util.IntIterator;

class EntityManagerTest {

    static EntityManager manager;

    public static void main(String[] args) {
        manager = new DefaultEntityManager();
        for (int i = 0; i < 0x10000; i++) {
            manager.createEntity();
        }
        System.out.println(manager.isExistingEntity(0x1000));
        IntIterator entities = manager.getEntities();
        while (entities.hasNext()) {
            int id = entities.next();
        }
    }
}