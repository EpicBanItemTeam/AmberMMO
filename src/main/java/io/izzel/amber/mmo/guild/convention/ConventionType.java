package io.izzel.amber.mmo.guild.convention;

public class ConventionType implements IConventionType{
    private final String type;

    public static final ConventionType RENT = new ConventionType("RENT");//出租(租约),将资产 成员 子小组出租。即周期性的向某个组织提供资源
    public static final ConventionType TRANSFER = new ConventionType("TRANSFER");//转让(转让协议),同上
    public static final ConventionType APPOINT = new ConventionType("APPOINT");//约定，防守约定，研究约定，贸易约定等

    public ConventionType(String type) {
        this.type = type;
    }

    @Override
    public String name() {
        return type;
    }
}
