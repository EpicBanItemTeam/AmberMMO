package io.izzel.amber.mmo.skill.skills.operations;

import com.google.common.collect.ImmutableList;
import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
public final class MultiOperation<C extends CastingSkill> implements SkillOperation<C> {

    @Getter private final List<SkillOperation> operations;

    public static <T extends CastingSkill> MultiOperation<T> of(SkillOperation... operations) {
        return new MultiOperation<>(ImmutableList.copyOf(operations));
    }

    @Override
    public String getId() {
        return "MultiOperation";
    }
}
