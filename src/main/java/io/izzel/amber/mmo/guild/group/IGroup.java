package io.izzel.amber.mmo.guild.group;

import io.izzel.amber.mmo.guild.convention.IConvention;

import java.util.Optional;
import java.util.Set;

public interface IGroup {
    /**
     * 获取成员
     * 成员可能是: 1.玩家(player) 2.非玩家(必须为生物，且该生物对小组有贡献，否则应为资产) 3.子组(sub group)
     * 成员一般不能是非生物，group的非生物属于资产（asset)
     * @return
     */
    Set<Imember> members();

    /**
     * 获取拥有的资产
     * 资产一般包括:土地(BoundBox) 各种货币(currency) 资源(item block entity)
     * @return
     */
    Set<IAsset> assets();

    /**
     * 获取该group与其他group建立的条约
     * @return
     */
    Set<IConvention> conventions();

    /**
     * 获取上级group
     * @return
     */
    Optional<IGroup> parent();

    /**
     * 获取子group
     * @return
     */
    Set<IGroup> subGroups();
}
