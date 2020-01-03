
```hocon
{ ID { arg1 = xxxx, arg2 = xxxx } }
```

## Trigger

| ID | Description | Argument |
| --- | --- | --- |
| `timer` |  | `delay` Initial delay(ticks) <br> `period` interval between every drop |
| `block-break` |  | `type` Block type id |
| `entity-kill` |  | `type` Mob type id |
| `fishing` |  | |

## Condition

| ID | Description | Usage/Argument |
| --- | --- | --- |
| `cooldown.global` | Global cooldown | `tick` |
| `cooldown.player` | Per player cooldown | `tick` <br> `id` (optional) used same group of cooldown timer in several drop table |
| `any` | Match any condition | ```{any=[{condition1}, {condition2}, ...]}``` |
| `not` | Inverse condition | ```{not.cooldown.xxxx{...}}``` |
| `in-region.coord` | In box region | `from`, `to` Corner of the region(inclusive) <br> ```{in-region.coord{from=['world',x,y,z],to=[world,x,y,z]}} ``` |
| `in-region.world` | In world | ```{in-region.world='world'}``` <br> ```{in-region.world=['world1','world2']}``` |
| `date.between` | In certain period of time | `from`, `to` half-open duration <br> Date format `hh:mm:ss`, `yyyy-MM-dd`, `yyyy-MM-dd hh:mm:ss` <br> second is optional |
| `date.in-week`| In certain week | ```{date.in-week=[1,3,5,7]}``` <br> Monday - Sunday: 1 - 7 |
| `permisiion` |  | ```{permission="test"}``` <br> ```{permission=["test", "test2"]}``` |

## Action

| ID | Description | Usage/Argument |
| --- | --- | --- |
| `drop-table` | Refer to a drop table | `id` table name |
| `vanilla` | item stack drop | `id` Item type id <br> `amount` optional amount |
| `exp` | ExpOrb | `{exp=N}` |
| `command` | Execute command | `{command="say hello"}` <br> `{command.console="stop"}` |
| `command.console` | Execute console command | `{command="say hello"}` <br> `{command.console="stop"}` |
| `override` | Prevent vanilla drop | `{override{}}` |
| `dynamic.add` <br> `dynamic.set` | Modify [dynamic value](/zh-cn/dynamic.md) | `key` key of dynamic value <br> `duration` <br> `expression` |
| `dynamic.reset` | Reset [dynamic value](/zh-cn/dynamic.md) |  `key` key of dynamic value |