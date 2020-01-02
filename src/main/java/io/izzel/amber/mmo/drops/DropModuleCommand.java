package io.izzel.amber.mmo.drops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.mmo.drops.data.AmountTempModifier;
import io.izzel.amber.mmo.drops.data.DropPlayerData;
import io.izzel.amber.mmo.drops.types.DropRule;
import io.izzel.amber.mmo.util.DurationUtil;
import lombok.val;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.regex.Pattern;

@Singleton
public class DropModuleCommand {

    private static final Pattern EXPRESSION = Pattern.compile("([-+*])(-?\\d+(\\.\\d+)?)");

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Inject
    public DropModuleCommand(PluginContainer container, Game game, AmberLocale locale) {
        game.getCommandManager().register(container, CommandSpec.builder()
            .child(CommandSpec.builder()
                .permission("ambermmo.admin.drops.reload")
                .executor((src, args) -> {
                    try {
                        DropTableService.instance().reloadDrops(src);
                    } catch (Exception e) {
                        locale.to(src, "drops.command.reload.fail", e);
                        e.printStackTrace();
                    }
                    return CommandResult.success();
                })
                .build(), "reload")
            .child(CommandSpec.builder()
                .permission("ambermmo.admin.drops.dynamic")
                .arguments(GenericArguments.string(Text.of("op")),
                    GenericArguments.playerOrSource(Text.of("player")),
                    GenericArguments.string(Text.of("key")),
                    GenericArguments.optionalWeak(GenericArguments.string(Text.of("duration"))),
                    GenericArguments.optionalWeak(GenericArguments.string(Text.of("expression"))))
                .executor((src, args) -> {
                    Player player = args.<Player>getOne("player").orElseThrow(() ->
                        new CommandException(locale.getUnchecked("drops.command.dynamic.no-player")));
                    String op = args.<String>getOne("op").get();
                    String key = args.<String>getOne("key").get();
                    if (op.equalsIgnoreCase("reset")) {
                        DropPlayerData.resetModifier(player, key);
                        locale.to(src, "drops.command.dynamic.reset", player, key);
                    } else {
                        String duration = args.<String>getOne("duration").get();
                        String expression = args.<String>getOne("expression").get();
                        try {
                            long millis = DurationUtil.parseToMillis(duration);
                            if (EXPRESSION.matcher(expression).matches()) {
                                val modifier = new AmountTempModifier(System.currentTimeMillis() + millis, expression);
                                if (op.equalsIgnoreCase("add")) {
                                    DropPlayerData.addModifier(player, key, modifier);
                                    locale.to(src, "drops.command.dynamic.add", player, key);
                                } else if (op.equalsIgnoreCase("set")) {
                                    DropPlayerData.setModifier(player, key, modifier);
                                    locale.to(src, "drops.command.dynamic.set", player, key);
                                } else {
                                    locale.to(src, "drops.command.dynamic.unknown-op");
                                }
                            } else {
                                locale.to(src, "drops.command.dynamic.expression-format");
                            }
                        } catch (Exception e) {
                            locale.to(src, "drops.command.dynamic.duration-format");
                        }
                    }
                    return CommandResult.success();
                })
                .build(), "dynamic")
            .child(CommandSpec.builder()
                .permission("ambermmo.admin.drops.execute")
                .arguments(GenericArguments.string(Text.of("rule")),
                    GenericArguments.playerOrSource(Text.of("target")))
                .executor((src, args) -> {
                    Player player = args.<Player>getOne("target").get();
                    String rule = args.<String>getOne("rule").get();
                    Optional<DropRule> optional = DropTableService.instance().getDropRuleById(rule);
                    if (optional.isPresent()) {
                        DropRule dropRule = optional.get();
                        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                            frame.pushCause(new DropContext().set(player, DropContext.Key.OWNER)
                                .set(player.getLocation(), DropContext.Key.LOCATION));
                            dropRule.apply();
                            locale.to(src, "drops.command.execute.success");
                        }
                    } else {
                        locale.to(src, "drops.command.execute.not-found");
                    }
                    return CommandResult.success();
                })
                .build(), "execute")
            .build(), "amberdrops", "drops");
    }
}
