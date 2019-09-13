package io.izzel.amber.mmo.skill.op;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

abstract class AbstractOperation<C extends CastingSkill> implements SkillOperation<C> {

    @Override
    public String getId() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
