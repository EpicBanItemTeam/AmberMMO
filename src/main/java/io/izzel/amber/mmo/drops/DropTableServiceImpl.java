package io.izzel.amber.mmo.drops;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.drops.types.Amount;
import io.izzel.amber.mmo.drops.types.AmountSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.util.*;

@Singleton
class DropTableServiceImpl implements DropTableService {

    @Inject
    public DropTableServiceImpl() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Amount.class), new AmountSerializer());
    }

    private final Map<String, Class<?>> map = new HashMap<>();

    @Override
    public <T extends DropTable> void registerDropTableType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
        Preconditions.checkArgument(!map.containsKey(id), "duplicate id");
        Preconditions.checkNotNull(cl);
        Preconditions.checkNotNull(deserializer);
        map.put(id, cl);
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(cl), deserializer);
    }

    @Override
    public Set<String> availableTypes() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DropTable> Class<T> getTypeById(String id) {
        return (Class<T>) map.get(id);
    }

    @Override
    public Optional<DropTable> getDropTableById(String id) {
        //todo
        return Optional.empty();
    }

}
