package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

public class TimerTrigger implements DropTrigger {

    private final int delay, period;

    @Nullable private Task task;

    public TimerTrigger(int delay, int period) {
        this.delay = delay;
        this.period = period;
    }

    @Override
    public void set(Runnable action) {
        if (task == null) {
            task = Task.builder()
                .delayTicks(delay)
                .intervalTicks(period)
                .execute(action)
                .submit(Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new));
        } else throw new IllegalStateException();
    }

    @Override
    public void unset() {
        if (task != null) {
            task.cancel();
            task = null;
        } else throw new IllegalStateException();
    }

    public static class Serializer implements TypeSerializer<TimerTrigger> {

        @Nullable
        @Override
        public TimerTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            int period = value.getNode("period").getInt();
            if (period == 0) throw new ObjectMappingException();
            int delay = value.getNode("delay").getInt(period);
            return new TimerTrigger(delay, period);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable TimerTrigger obj, @NonNull ConfigurationNode value) {

        }
    }
}
