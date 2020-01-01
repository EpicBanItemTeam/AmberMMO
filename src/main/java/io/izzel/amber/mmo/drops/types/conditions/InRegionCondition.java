package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import lombok.Data;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Set;

public interface InRegionCondition extends DropCondition {

    class Serializer implements TypeSerializer<InRegionCondition> {

        @Nullable
        @Override
        public InRegionCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            val node = value.getChildrenMap().entrySet().iterator().next();
            val nodeType = node.getKey().toString();
            if (nodeType.equalsIgnoreCase("coord")) {
                val coord = node.getValue();
                val from = coord.getNode("from").getValue(TypeToken.of(Coord.class));
                val to = coord.getNode("to").getValue(TypeToken.of(Coord.class));
                if (from != null && to != null && !from.getWorld().equals(to.getWorld())) {
                    return new CoordRegion(from, to);
                }
            } else if (nodeType.equalsIgnoreCase("world")) {
                val worlds = node.getValue().getList(TypeToken.of(String.class));
                return new WorldRegion(worlds);
            }
            return null;
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable InRegionCondition obj, @NonNull ConfigurationNode value) {
        }

        private static class WorldRegion implements InRegionCondition {
            private final Set<String> worlds;

            private WorldRegion(List<String> worlds) {
                this.worlds = Sets.newHashSet(worlds);
            }

            @Override
            public boolean test() {
                return DropContext.current()
                    .get(DropContext.Key.LOCATION)
                    .map(Location::getExtent)
                    .map(World::getName)
                    .filter(worlds::contains)
                    .isPresent();
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("worlds", worlds)
                    .toString();
            }
        }

        private static class CoordRegion implements InRegionCondition {

            private final Coord from, to;

            private CoordRegion(Coord from, Coord to) {
                this.from = from;
                this.to = to;
            }

            @Override
            public boolean test() {
                val loc = DropContext.current().get(DropContext.Key.LOCATION);
                if (!loc.isPresent()) return true;
                val optional = loc.filter(location -> location.getExtent().getName().equals(from.getWorld())
                    && Math.min(from.getX(), to.getX()) <= location.getX() && location.getX() <= Math.max(from.getX(), to.getX())
                    && Math.min(from.getY(), to.getY()) <= location.getY() && location.getY() <= Math.max(from.getY(), to.getY())
                    && Math.min(from.getZ(), to.getZ()) <= location.getZ() && location.getZ() <= Math.max(from.getZ(), to.getZ()));
                return optional.isPresent();
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("from", from)
                    .add("to", to)
                    .toString();
            }
        }

    }

    @Data
    class Coord {

        private final String world;
        private final double x, y, z;

        public static class Serializer implements TypeSerializer<Coord> {

            @Nullable
            @Override
            public Coord deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
                val list = value.getChildrenList().listIterator();
                val world = list.next().getString();
                val x = list.next().getDouble();
                val y = list.next().getDouble();
                val z = list.next().getDouble();
                return new Coord(world, x, y, z);
            }

            @Override
            public void serialize(@NonNull TypeToken<?> type, @Nullable Coord obj, @NonNull ConfigurationNode value) {
            }
        }
    }
}
