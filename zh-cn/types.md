
```hocon
{ ID { 参数1 = xxxx, 参数2 = xxxx } }
```

## 触发器

| ID | 描述 | 可用参数 |
| --- | --- | --- |
| `timer` | 定时触发 | `delay` 第一次（开服后）触发的时间 <br> `period` 每次触发的间隔时间 <br> 单位为 `tick`，20 `tick` = 1 s |
| `block-break` | 方块破坏时触发 | `type` 方块种类 id |
| `entity-kill` | 生物被击杀时触发 | `type` 生物种类 id |
| `fishing` | 钓鱼 | |

## 条件

| ID | 描述 | 用法/可用参数 |
| --- | --- | --- |
| `cooldown.global` | 全局冷却 | `tick` 冷却时间 |
| `cooldown.player` | 玩家冷却 | `tick` 冷却时间 <br> `id` （可选）用相同 id 在多个掉落表项中使用同一组冷却 |
| `any` | 满足任意一个条件 | ```{any=[{条件1}, {条件2}, ...]}``` |
| `not` | 不满足条件 | ```{not.cooldown.xxxx{...}}``` |
| `in-region.coord` | 在区域中 | `from`, `to` 区域的起点和终点（包含） <br> ```{in-region.coord{from=['world',x,y,z],to=[world,x,y,z]}} ``` |
| `in-region.world` | 在指定世界 | ```{in-region.world='world'}``` <br> ```{in-region.world=['world1','world2']}``` |
| `date.between` | 在指定时间段 | `from`, `to` 时间段的起点（包含）终点（不包含） <br> 时间格式可以是 `时:分:秒`, `年-月-日`, `年-月-日 时:分:秒` 秒是可选的 |
| `date.in-week`| 在某个星期日内 | ```{date.in-week=[1,3,5,7]}``` <br> 周一至周日使用 1 至 7 表示 |
| `permisiion` | 引发掉落的生物拥有权限 | ```{permission="test"}``` <br> ```{permission=["test", "test2"]}``` |

## 掉落表

| ID | 描述 | 用法/可用参数 |
| --- | --- | --- |
| `drop-table` | 引用自身或其他掉落表 | `id` 表名 |
| `vanilla` | 原版掉落物 | `id` 物品种类 id <br> `amount` （可选）物品数量 |
| `exp` | 经验球 | `{exp=N}` |
| `command` | 以引发掉落的玩家或控制台的身份执行命令 | `{command="say hello"}` <br> `{command.console="stop"}` |
| `override` | 覆盖原版掉落，即阻止原版的掉落 | `{override{}}` |
| `dynamic.add` <br> `dynamic.set` | 更改掉落表[动态值](/zh-cn/dynamic.md) | `key` 动态值键 <br> `duration` 持续时间 <br> `expression` 表达式 |
| `dynamic.reset` | 清除掉落表[动态值](/zh-cn/dynamic.md) |  `key` 动态值键 |