package io.izzel.amber.mmo.drops;

import com.google.inject.ImplementedBy;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;

import java.util.Optional;

@ImplementedBy(DropTableServiceImpl.class)
public interface DropTableService {

    <T extends DropTable> Class<T> getTypeById(String id);

    Optional<DropTable> getDropTableById(String id);

    void reloadDropTables() throws Exception;

    static DropTableService instance() {
        return Sponge.getServiceManager().provideUnchecked(DropTableService.class);
    }

    interface RegistryEvent extends Event {

        <T extends DropTable> void registerDropTableType(String id, Class<T> cl, TypeSerializer<T> deserializer);



    }

}
