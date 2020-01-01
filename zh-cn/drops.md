
## 文件格式

所有的掉落表保存在 `config/ambermmo/drop_tables`，所有的掉落规则保存在 `config/ambermmo/drop_rules` 中。

## 添加自定义掉落表

在 `config/ambermmo/drop_tables` 创建的任意 `.conf` 文件将用于加载掉落表。

掉落表分为两种，`概率掉落表` 和 `权重掉落表`。

### 概率掉落表

```hocon
chance_table {
  items = [
 // [ 掉落表项,              概率, 数量 ]
    [ { command = "say hello" }, 0.15, [0,3]] // 有 15% 的几率，发送 0 到 2 次 hello
    [ { vanilla { id = "diamond" }}, 0.05, 1] // 有 5% 的几率掉落一个钻石
  ]
  roll = 2 // roll 项是可选的，用于控制掉落表掉落的次数
}
```

概率掉落表中，可能随机选中一个或多个掉落表项，也可能什么也没有。

### 权重掉落表

```hocon
weighted_table {
  weighted = true // 标记这是一个带权重的掉落表
  items = [
 // [ 掉落表项, 权重 ]
    [ { vanilla { id="coal" } }, 500]
    [ { vanilla { id="diamond" } }, 3]
  ]
  roll = 1 // roll 项是可选的，用于控制掉落表掉落的次数
}
```

权重掉落表中，每一次掉落只会且一定会选中其中的一个掉落表项。上面的示例中，有 3/503 的几率掉落钻石，500/503 的几率掉落煤炭。

通过更改 `roll` 可以增加掉落的数量。

### 简写的掉落表

概率掉落表可以简写为

```hocon
simple_table {
  items = [
    { vanilla { id = "apple" } }
    { exp = 1 }
  ]
}
```

其中每一项都是概率 1 数量 1，也就是 “挨个掉落”。

## 添加自定义掉落规则

在 `config/ambermmo/drop_rules` 创建的任意 `.conf` 文件将用于加载掉落规则。

所有的规则形如下：

```hocon
// 规则名称
example_rule {
  triggers = [] // 触发器
  conditions = [] // 条件
  actions = [] // 掉落表
}
```

其中所有 [触发器](/zh-cn/types.md#触发器)、[条件](/zh-cn/types.md#条件) 及
[掉落表](/zh-cn/types.md#掉落表) 可在对应的链接内查看所有可用的格式。

## 示例

文件 `config/ambermmo/drop_rules/example.conf`
```hocon
spring_festival {
  triggers = [
    { entity-kill { type = "zombie" } }  // 击杀僵尸
  ]
  conditions = [
    { date.between { from = "2020.1.24", to = "2020.2.1" } } // 除夕至初八，不包含初八当天
  ]
  actions = ["festival_bonus"]
}
```

文件 `config/ambermmo/drop_tables/example.conf`
```hocon
festival_bonus {
  items = [
    { exp = [4,8] } // 提供额外的经验值
    { vanilla { id = "diamond", amount = 2 } } // 掉落两个钻石
  ]
}

```

以上示例将会在春节期间，任何击杀僵尸的玩家都会获得额外的经验值和钻石。

同时，掉落表如果不需要多处引用的话，可以直接写入 `drop_rules` 中，如

文件 `config/ambermmo/drop_rules/example.conf`
```hocon
spring_festival {
  triggers = [
    { entity-kill { type = "zombie" } } 
  ]
  conditions = [
    { date.between { from = "2020.1.24", to = "2020.2.1" } }
  ]
  actions = {
     items = [
        { exp = [4,8] }
        { vanilla { id = "diamond", amount = 2 } }
     ]
  }
}
```
