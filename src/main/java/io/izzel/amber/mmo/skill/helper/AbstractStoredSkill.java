package io.izzel.amber.mmo.skill.helper;

import io.izzel.amber.mmo.skill.storage.StoredSkill;
import lombok.Getter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.spongepowered.api.text.Text;

import java.util.List;

@ConfigSerializable
public abstract class AbstractStoredSkill implements StoredSkill {

    @Getter @Setting private String id;
    @Getter @Setting private Text name;
    @Getter @Setting private List<Text> description;
    @Getter @Setting private List<String> tags;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
