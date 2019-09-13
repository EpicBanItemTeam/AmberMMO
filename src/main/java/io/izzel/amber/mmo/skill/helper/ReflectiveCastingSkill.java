package io.izzel.amber.mmo.skill.helper;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillSubject;
import io.izzel.amber.mmo.skill.data.EntitySkill;

public abstract class ReflectiveCastingSkill<E extends EntitySkill<?, ?>> extends CastingSkill<E> {

    final void setOwning(E owning) {
        this.owning = owning;
    }

    final void setSubject(SkillSubject subject) {
        this.subject = subject;
    }

}
