package io.izzel.amber.mmo.skill.data;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillSubject;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import io.izzel.amber.mmo.util.Identified;
import io.izzel.amber.mmo.util.Propertied;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.text.Text;

import java.util.List;

/**
 * 设计为玩家已经获得，有状态（如等级、熟练）的技能
 *
 * @param <S> 存储类
 * @param <C> 执行类
 */
public interface EntitySkill<S extends StoredSkill, C extends CastingSkill> extends DataSerializable, Identified, Propertied {

    C createCast(SkillSubject subject);

    S getSkill();

    List<Text> getExtendDescription();

    @Override
    default int getContentVersion() {
        return 0;
    }

}
