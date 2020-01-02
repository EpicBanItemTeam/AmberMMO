
在掉落表和掉落规则中，可以使用动态的值，只需要简单的在原有的值外加上一个 `[]`，就像这样：

```hocon
festival_bonus {
  items = [
    { exp = [[4,8]] } // 动态的范围值
    { vanilla { id = "diamond", amount = [2] } } // 动态的固定值
  ]
}
```

之后，你可以使用两种方法对动态值进行更改，掉落表和命令。但在此之前，你需要了解动态值的[路径](https://github.com/ustc-zzzz/HOCON-CN-Translation/blob/master/HOCON.md#%E8%B7%AF%E5%BE%84%E8%A1%A8%E8%BE%BE%E5%BC%8F)。

一些简单的示例：

* 本篇第一个示例中的 `[[4,8]]`，它的路径是 `festival_bonus.items.0.exp`
* 本篇第一个示例中的 `[2]`，它的路径是 `festival_bonus.items.1.vanilla.amount`
* 下文掉落表的示例中的 `[0.15]`，它的路径是 `chance_table.items.0.1`
* 下文掉落表的示例中的 `[[0,3]]`，它的路径是 `chance_table.items.0.2`

## 掉落表

```hocon
chance_table {
  items = [
    [ { vanilla { id = "diamond" }}, [0.15], [[0,3]]]
    { dynamic.add { key = "chance_table.items.0.1", duration = "3.5s", expression = "*1.4" } }
    { dynamic.set { key = "chance_table.items.0.2", duration = "1m30s", expression = "+1" } }
  ]
}
```

以上的示例，为第一个钻石的掉落增加了 3.5 秒 40% 的掉落概率，以及固定 +1 的掉落数量。

注意为 0.15 这个动态值添加的方式是 `add`，因此快速调用这个掉落表将可以获得累加的掉率加成。

## 命令

```
/drops dynamic add <玩家> chance_table.items.0.1 3.5s *1.4
/drops dynamic set <玩家> chance_table.items.0.2 1m30s +1
```

效果与上一个示例相同。

----

动态值的更改分为三种，分别是 `添加` `设置` 和 `清除`。

当**添加**一个动态值，不同时间添加的量将会累计，直至超时。

**设置**一个动态值的效果等同于清除后添加。

----

持续时间表达式为 `XwXdXhXmX.Xs`

其中 `w` `d` `h` `m` `s` 分别是周、天、时、分、秒。秒可以是小数。

----

修改表达式是一个运算符和一个数值，运算符可以是 `+` `-` `*`，数值是可正可负的小数。
