package io.izzel.amber.mmo.skill;

import com.google.inject.Inject;
import io.izzel.amber.mmo.skill.data.SkillTree;
import io.izzel.amber.mmo.skill.data.SkillTreeLeaf;
import io.izzel.amber.mmo.skill.event.SkillRegistryEventImpl;
import io.izzel.amber.mmo.skill.storage.SkillStorage;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
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
                eventManager.post(new SkillRegistryEventImpl(stackFrame.getCurrentCause(), map));
                storage.loadAll(map);
            }
        });
    }

    @Override
    public Optional<StoredSkill> getStored(String id) {
        return Optional.ofNullable(this.storage.get(id));
    }

    @Override
    public Optional<SkillSubject> getSubject(Entity entity) {
        return Optional.ofNullable(map.get(entity.getUniqueId()));
    }

    @Override
    public Optional<SkillSubject> getOrCreate(Entity entity) {
        return Optional.of(map.computeIfAbsent(entity.getUniqueId(), uuid -> new SkillSubjectImpl(entity)));
    }

}
