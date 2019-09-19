package io.izzel.amber.mmo.guild.convention.lease;

import io.izzel.amber.mmo.guild.convention.IConvention;

/**
 * 租约接口
 */
public interface IConventionLease extends IConvention {
    /**
     * 检查租约是否已过期
     * @return
     */
    boolean expired();


}
