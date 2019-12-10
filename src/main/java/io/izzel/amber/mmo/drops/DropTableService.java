package io.izzel.amber.mmo.drops;

import com.google.inject.ImplementedBy;
import io.izzel.amber.mmo.util.Propertied;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;

import java.util.Optional;
import java.util.Set;

@ImplementedBy(DropTableServiceImpl.class)
public interface DropTableService {

    <T extends DropTable> void registerDropTableType(String id, Class<T> cl, TypeSerializer<T> deserializer);

    Set<String> availableTypes();

    <T extends DropTable> Class<T> getTypeById(String id);

    Optional<DropTable> getDropTableById(String id);

    Propertied queryDroptable(String query);

    void reloadDropTables() throws Exception;

    static DropTableService instance() {
        return Sponge.getServiceManager().provideUnchecked(DropTableService.class);
    }

}
