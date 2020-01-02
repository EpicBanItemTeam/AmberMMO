
掉落模块的主命令为 `/amberdrops`，或者 `/drops`

可用的子命令如下

#### `reload`

**权限** `ambermmo.admin.drops.reload`

重载所有的掉落表和掉落规则。

#### `dynamic`

**权限** `ambermmo.admin.drops.dynamic`

**参数** `add|set [<玩家>] <键> <持续时间> <表达式>`

**参数** `reset [<玩家>] <键>`

添加、设置或清除玩家某个键的[动态值](/zh-cn/dynamic.md)。

#### `execute`

**权限** `ambermmo.admin.drops.execute`

**参数** `<规则> [<玩家>]`

直接执行一个掉落规则。