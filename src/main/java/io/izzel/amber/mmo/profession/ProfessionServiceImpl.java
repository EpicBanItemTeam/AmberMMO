package io.izzel.amber.mmo.profession;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.profession.data.EntityProfessionData;
import io.izzel.amber.mmo.profession.data.MutableProfession;
import io.izzel.amber.mmo.profession.storage.ProfessionStorage;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ServiceManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
final class ProfessionServiceImpl implements ProfessionService {

    private final ProfessionStorage storage;

    @Inject
    public ProfessionServiceImpl(PluginContainer container, ServiceManager serviceManager, EventManager eventManager, ProfessionStorage storage) {
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
            storage.load();
        });
    }

    @Override
    public List<ProfessionSubject> getProfessions(Entity entity) {
        ImmutableList.Builder<ProfessionSubject> builder = ImmutableList.builder();
        Optional<EntityProfessionData.Mutable> optional = entity.getOrCreate(EntityProfessionData.Mutable.class);
        if (optional.isPresent()) {
            EntityProfessionData.Mutable mutable = optional.get();
            Map<String, MutableProfession> professions = mutable.getProfessions();
            for (Map.Entry<String, MutableProfession> entry : professions.entrySet()) {
                getById(entry.getKey()).ifPresent(it -> builder.add(new WrappedProfessionSubject(it, entry.getValue())));
            }
        }
        return builder.build();
    }

    @Override
    public List<Profession> listAll() {
        return ImmutableList.copyOf(storage.getLoaded().values());
    }

    @Override
    public Optional<Profession> getById(String id) {
        return Optional.ofNullable(storage.getLoaded().get(id));
    }

}
