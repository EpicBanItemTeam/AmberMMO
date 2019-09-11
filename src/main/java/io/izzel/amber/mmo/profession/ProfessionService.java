package io.izzel.amber.mmo.profession;

import com.google.inject.ImplementedBy;
import org.spongepowered.api.entity.Entity;

import java.util.List;
import java.util.Optional;

@ImplementedBy(ProfessionServiceImpl.class)
public interface ProfessionService {

    List<ProfessionSubject> getProfessions(Entity entity);

    List<Profession> listAll();

    Optional<Profession> getById(String id);

}
