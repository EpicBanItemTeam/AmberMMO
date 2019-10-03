package io.izzel.amber.mmo.profession;

import com.google.inject.ImplementedBy;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;

import java.util.List;
import java.util.Optional;

@ImplementedBy(ProfessionServiceImpl.class)
public interface ProfessionService {

    Optional<ProfessionSubject> getSubject(Entity entity);

    ProfessionSubject getOrCreate(Entity entity);

    List<StoredProfession> listAll();

    Optional<StoredProfession> getById(String id);

    static ProfessionService instance() {
        return Sponge.getServiceManager().provideUnchecked(ProfessionService.class);
    }

}
