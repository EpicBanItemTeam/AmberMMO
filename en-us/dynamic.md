
Add `[]` outside of number in order to use dynamic values:

```hocon
festival_bonus {
  items = [
    { exp = [[4,8]] } // Dynamic ranged value
    { vanilla { id = "diamond", amount = [2] } } // Dynamic fixed value
  ]
}
```

There are two ways to modify certain dynamic value, `drop action` and `command`.

Before modifying that, you need to have a basic understanding of the
[path](https://github.com/lightbend/config/blob/master/HOCON.md#path-expressions) of a dynamic value.

Simple example:

* `[[4,8]]` in the example above whose path is `festival_bonus.items.0.exp`
* `[2]` in the example above whose path is `festival_bonus.items.1.vanilla.amount`
* `[0.15]` in the example below whose path is `chance_table.items.0.1`
* `[[0,3]]` in the example below whose path is `chance_table.items.0.2`

## Drop action

```hocon
chance_table {
  items = [
    [ { vanilla { id = "diamond" }}, [0.15], [[0,3]]]
    { dynamic.add { key = "chance_table.items.0.1", duration = "3.5s", expression = "*1.4" } }
    { dynamic.set { key = "chance_table.items.0.2", duration = "1m30s", expression = "+1" } }
  ]
}
```

Example above add 40% drop chance to the first vanilla diamond drop with 3.5 seconds timeout
and a fixed 1 more drop amount.

## Command

```
/drops dynamic add <player> chance_table.items.0.1 3.5s *1.4
/drops dynamic set <player> chance_table.items.0.2 1m30s +1
```

Command version of action example.

----

There are 3 actions to modify a dynamic value, `add` `set` and `reset`.

Differences between add and set:

* Basic value is `3.0`
* **Add** an expression `+1.4`, the final value is `3.0+1.4`
* then **add** an expression `*0.6`, the final value is `(3.0+1.4)*0.6`
* then **set** an expression `-0.1`, the final value is `3.0-0.1`
* then **add** an expression `+1.2`, the final value is `(3.0-0.1)+1.2`

Set is same as reset then add.

----

Duration format: `XwXdXhXmX.Xs`

`w` `d` `h` `m` `s` refer to week, day, hour, minute, second. second can be a decimal.

----

Expression format: an operator and a number. Operator can be `+` `-` `*`.
