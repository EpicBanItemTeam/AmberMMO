package io.izzel.amber.mmo.skill;

import com.google.inject.Inject;
import io.izzel.amber.mmo.skill.data.SkillTree;
import io.izzel.amber.mmo.skill.data.SkillTreeLeaf;
import io.izzel.amber.mmo.skill.event.SkillEvent;
import io.izzel.amber.mmo.skill.storage.SkillStorage;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NonnullByDefault
final class SkillServiceImpl implements SkillService {

    private final SkillStorage storage;
    private final Map<UUID, SkillSubject> map = new HashMap<>();

    @Inject
    public SkillServiceImpl(PluginContainer container, EventManager eventManager, DataManager dataManager, SkillStorage storage) {
        this.storage = storage;
        eventManager.registerListener(container, GameInitializationEvent.class, event -> {
            dataManager.registerBuilder(SkillTree.class, new SkillTreeLeaf.Builder());
            try (CauseStackManager.StackFrame stackFrame = Sponge.getCauseStackManager().pushCauseFrame()) {
                Map<String, Class<?>> map = new HashMap<>();
                eventManager.post(SkillEvent.createRegistry(stackFrame.getCurrentCause(), map));
                storage.loadAll(map);
            }
        });
        Task.builder().intervalTicks(10).execute(() -> map.values().removeIf(it -> !it.isValid())).submit(container);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends StoredSkill> Optional<S> getStored(String id) {
        return Optional.ofNullable((S) this.storage.get(id));
    }

    @Override
    public Optional<SkillSubject> getSubject(Entity entity) {
        return Optional.ofNullable(map.get(entity.getUniqueId()));
    }

    @Override
    public SkillSubject getOrCreate(Entity entity) {
        return map.computeIfAbsent(entity.getUniqueId(), uuid -> new SkillSubjectImpl(entity));
    }

}
