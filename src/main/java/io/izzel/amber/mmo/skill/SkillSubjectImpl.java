package io.izzel.amber.mmo.skill;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import io.izzel.amber.mmo.profession.ProfessionService;
import io.izzel.amber.mmo.profession.ProfessionSubject;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.data.SkillTree;
import io.izzel.amber.mmo.skill.event.SkillEvent;
import io.izzel.amber.mmo.skill.op.SkillOperation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.lang.ref.WeakReference;
import java.util.*;

@NonnullByDefault
final class SkillSubjectImpl implements SkillSubject {

    private WeakReference<Entity> entityWf;
    private final UUID owner;
    private final SetMultimap<Class<EntitySkill<?, ?>>, CastingSkill<?>> multimap = MultimapBuilder.hashKeys().linkedHashSetValues().build();
    private final Operator operator = new Operator();

    public SkillSubjectImpl(Entity entity) {
        this.entityWf = new WeakReference<>(entity);
        this.owner = entity.getUniqueId();
    }

    @Override
    public Collection<CastingSkill<?>> getCastingSkills() {
        return Collections.unmodifiableCollection(multimap.values());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends CastingSkill<E>, E extends EntitySkill<?, C>> Collection<C> getCastingSkills(Class<E> cl) {
        return Collections.unmodifiableCollection(Optional.ofNullable(multimap.get((Class) cl)).orElse(ImmutableSet.of()));
    }

    @Override
    public <C extends CastingSkill<E>, E extends EntitySkill<?, C>> Optional<C> operate(Class<E> cl, SkillOperation<? super C> operation) throws UnsupportedOperationException {
        Optional<Entity> entity = getEntity();
        if (entity.isPresent()) {
            if (multimap.containsKey(cl)) {
                Collection<C> castingSkills = getCastingSkills(cl);
                if (castingSkills.size() > 0) {
                    C skill = castingSkills.iterator().next();
                    return Optional.of(operate(skill, operation));
                }
            }
            List<ProfessionSubject> professions = ProfessionService.instance().getProfessions(entity.get());
            SkillTree skillTree = professions.stream().map(ProfessionSubject::getSkillTree).reduce(SkillTree.empty(), SkillTree::merge);
            Optional<E> optional = skillTree.find(cl);
            if (optional.isPresent()) {
                E skill = optional.get();
                C cast = skill.createCast(this);
                return Optional.of(operate(cast, operation));
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends CastingSkill<E>, E extends EntitySkill<?, C>> C operate(C skill, SkillOperation<? super C> operation) {
        multimap.put((Class) skill.getOwning().getClass(), skill);
        try (CauseStackManager.StackFrame stackFrame = Sponge.getCauseStackManager().pushCauseFrame()) {
            SkillEvent.Operate event = SkillEvent.createOperate(stackFrame.getCurrentCause(),
                skill.getSubject().getEntity().orElseThrow(UnsupportedOperationException::new), skill, operation);
            Sponge.getEventManager().post(event);
            if (!event.isCancelled()) {
                event.getCastingSkill().perform(event.getOperation(), operator);
            }
        }
        return skill;
    }

    @Override
    public boolean isValid() {
        return getEntity().isPresent();
    }

    @SuppressWarnings("unchecked")
    public Optional<Entity> getEntity() {
        Entity identifiable = entityWf.get();
        if (identifiable != null) return Optional.of(identifiable);
        else {
            Optional<Entity> player = (Optional) Sponge.getServer().getPlayer(owner);
            if (player.isPresent()) {
                this.entityWf = new WeakReference<>(player.get());
                return player;
            }
        }
        return Optional.empty();
    }

    private class Operator implements CastOperator {

        @SuppressWarnings("unchecked")
        @Override
        public void add(CastingSkill skill) {
            multimap.put((Class) skill.getOwning().getClass(), skill);
        }

        @Override
        public void remove(CastingSkill skill) {
            multimap.remove(skill.getOwning().getClass(), skill);
        }
    }

}
