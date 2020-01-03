
## File

All drop tables are in `config/ambermmo/drop_tables`, rules are in `config/ambermmo/drop_rules`

## Add custom `drop table`

Any `.conf` file in `config/ambermmo/drop_tables` will be loaded as drop table.

There are two types of drop table, `chance table` and `weighted table`

#### Chance table

```hocon
chance_table {
  items = [
 // [ drop item,              chance, amount ]
    [ { command = "say hello" }, 0.15, [0,3]] // Send hello 0 - 2 times with chance of 15%
    [ { vanilla { id = "diamond" }}, 0.05, 1] // Drop one diamond with chance of 5%
  ]
  roll = 2 // optional roll count
}
```

One or more or none drop item may drop in a chance table.

#### Weighted table

```hocon
weighted_table {
  weighted = true // Mark as a weighted table
  items = [
 // [ drop item, weight ]
    [ { vanilla { id="coal" } }, 500]
    [ { vanilla { id="diamond" } }, 3]
  ]
  roll = 1 // optional roll count
}
```

One and only one item may drop every roll in a weighted table.

#### Simplified chance table

Chance table can be simplified as:

```hocon
simple_table {
  items = [
    { vanilla { id = "apple" } }
    { exp = 1 }
  ]
}
```

Every item has chance of 100% and amount of 1.

## Add custom `drop rule`

Any `.conf` file in `config/ambermmo/drop_rules` will be loaded as drop rule.

```hocon
// rule name
example_rule {
  triggers = []
  conditions = []
  actions = [] // the dropped items
}
```

Learn details of [triggers](/en-us/types.md#Trigger), [conditions](/en-us/types.md#Condition) and
[actions](/en-us/types.md#Action).

#### Example

File `config/ambermmo/drop_rules/example.conf`
```hocon
spring_festival {
  triggers = [
    { entity-kill { type = "zombie" } }  // killing a zombie
  ]
  conditions = [
    { date.between { from = "2020.1.24", to = "2020.2.1" } } // this is the date of Chinese New Year, not including 2020.2.1
  ]
  actions = ["festival_bonus"]
}
```

File `config/ambermmo/drop_tables/example.conf`
```hocon
festival_bonus {
  items = [
    { exp = [4,8] } // extra experience
    { vanilla { id = "diamond", amount = 2 } } // 2 diamonds
  ]
}
```

Example above will drop extra exp and diamond when player kills a zombie between specific date.

Drop tables can be written directly in `drop_rules`:

File `config/ambermmo/drop_rules/example.conf`
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
