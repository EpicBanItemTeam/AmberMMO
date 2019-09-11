package io.izzel.amber.mmo.profession;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import io.izzel.amber.mmo.profession.data.EntityProfessionData;
import io.izzel.amber.mmo.profession.data.MutableProfession;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ServiceManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

final class ProfessionServiceImpl implements ProfessionService {

    @Inject
    public ProfessionServiceImpl(PluginContainer container, ServiceManager serviceManager, EventManager eventManager) {
        serviceManager.setProvider(container, ProfessionService.class, this);
        eventManager.registerListener(container, GameInitializationEvent.class, event -> {
            DataRegistration.builder()
                .dataClass(EntityProfessionData.Mutable.class)
                .immutableClass(EntityProfessionData.Immutable.class)
                .builder(new EntityProfessionData.Builder())
                .id("ambermmo_prof")
                .name("AmberMMO Profession Data")
                .build();
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
        // todo 实现关于职业的存储
        return ImmutableList.of();
    }

    @Override
    public Optional<Profession> getById(String id) {
        return Optional.empty();
    }

}
