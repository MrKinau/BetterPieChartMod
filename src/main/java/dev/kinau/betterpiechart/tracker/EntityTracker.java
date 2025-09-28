package dev.kinau.betterpiechart.tracker;

import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class EntityTracker extends Tracker<Entity> {

    private static final EntityTracker INSTANCE = new EntityTracker();

    public static EntityTracker getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName(Entity entity) {
        return entity.getType().toShortString();
    }

    @Override
    public Optional<String> getTag(Entity tracked) {
        return Optional.empty();
    }
}
