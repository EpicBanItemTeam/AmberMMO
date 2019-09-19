package io.izzel.amber.mmo.guild.convention;

public interface IConvention {

    /**
     * 检查与其他条约是否冲突
     * @param convention
     * @return
     */
    boolean conflictWith(IConvention convention);

    /**
     * 获取条约类型
     * @return
     */
    IConventionType type();
}
