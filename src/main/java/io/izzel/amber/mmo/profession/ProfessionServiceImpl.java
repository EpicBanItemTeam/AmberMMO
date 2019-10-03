package io.izzel.amber.mmo.profession;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.profession.data.EntityProfessionData;
import io.izzel.amber.mmo.profession.data.EntityProfessionImpl;
import io.izzel.amber.mmo.profession.storage.ProfessionStorage;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ServiceManager;

import java.util.List;
import java.util.Optional;

@Singleton
final class ProfessionServiceImpl implements ProfessionService {

    private final ProfessionStorage storage;

    @Inject
    public ProfessionServiceImpl(PluginContainer container, ServiceManager serviceManager, EventManager eventManager,
                                 DataManager dataManager, ProfessionStorage storage) {
        this.storage = storage;
        serviceManager.setProvider(container, ProfessionService.class, this);
        eventManager.registerListener(container, GameInitializationEvent.class, event -> {
            DataRegistration.builder()
                .dataClass(EntityProfessionData.Mutable.class)
                .immutableClass(EntityProfessionData.Immutable.class)
                .builder(new EntityProfessionData.Builder())
                .id("ambermmo_prof")
                .name("AmberMMO Profession Data")
                .build();
            dataManager.registerBuilder(EntityProfessionImpl.class, new EntityProfessionImpl.Builder());
            storage.load();
        });
    }

    @Override
    public Optional<ProfessionSubject> getSubject(Entity entity) {
        return entity.get(EntityProfessionData.Mutable.class).map(WrappedProfessionSubject::new);
    }

    @Override
    public ProfessionSubject getOrCreate(Entity entity) {
        Optional<EntityProfessionData.Mutable> optional = entity.getOrCreate(EntityProfessionData.Mutable.class);
        if (optional.isPresent()) {
            EntityProfessionData.Mutable mutable = optional.get();
            return new WrappedProfessionSubject(mutable);
        } else {
            throw new IllegalStateException("Cannot create profession data for " + entity);
        }
    }

    @Override
    public List<StoredProfession> listAll() {
        return ImmutableList.copyOf(storage.getLoaded().values());
    }

    @Override
    public Optional<StoredProfession> getById(String id) {
        return Optional.ofNullable(storage.getLoaded().get(id));
    }

}
