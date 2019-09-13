package io.izzel.amber.mmo.skill.storage;

import io.izzel.amber.mmo.util.Displayed;
import io.izzel.amber.mmo.util.Identified;
import io.izzel.amber.mmo.util.Tagged;
import org.spongepowered.api.data.DataSerializable;

/**
 * 设计为用于存储的技能信息类
 */
public interface StoredSkill extends Tagged, Displayed, Identified, DataSerializable {

}
