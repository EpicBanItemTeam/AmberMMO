package io.izzel.amber.mmo.stats;

import com.flowpowered.math.GenericMath;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.commons.conf.ConfigValue;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

@Singleton
final class InternalPlayerStatsApplier {

    @Inject
    private PluginContainer container;

    @Inject @Setting("stats.healthControl.enable")
    private ConfigValue<Boolean> enableHealthControl = ConfigValue.of(true);

    @Inject @Setting("stats.healthControl.baseMaxHealth")
    private ConfigValue<Double> baseMaxHealth = ConfigValue.of(20D);

    @Inject @Setting("stats.healthControl.baseRegeneration")
    private ConfigValue<Double> baseRegeneration = ConfigValue.of(0D);

    @Inject @Setting("stats.walkspeedControl.enable")
    private ConfigValue<Boolean> enableWalkspeedControl = ConfigValue.of(true);

    @Inject @Setting("stats.walkspeedControl.baseValue")
    private ConfigValue<Double> baseWalkspeed = ConfigValue.of(0.1D);

    void registerAll() {
        if (enableHealthControl.get()) {
            Task.builder().delayTicks(20).intervalTicks(20).execute(() -> {
                for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
                    double lastHealth = onlinePlayer.get(Keys.HEALTH).orElse(baseMaxHealth.get());
                    double lastMax = onlinePlayer.get(Keys.MAX_HEALTH).orElse(baseMaxHealth.get());
                    double newValue = AmberStatsService.instance().collectKeyValue(onlinePlayer, Keys.MAX_HEALTH, baseMaxHealth.get());
                    if (Math.abs(lastMax - newValue) > GenericMath.DBL_EPSILON) {
                        onlinePlayer.offer(Keys.MAX_HEALTH, newValue);
                    }
                    double newHealth = AmberStatsService.instance().collectMagicValue(onlinePlayer, TypeToken.of(Double.class),
                        CollectAttributeEvent.Identifiers.REGENERATION, baseRegeneration.get(), Double::sum);
                    if (Math.abs(newHealth) > GenericMath.DBL_EPSILON) {
                        onlinePlayer.offer(Keys.HEALTH, lastHealth + newHealth);
                    }
                }
            }).submit(container);
        }
        if (enableWalkspeedControl.get()) {
            Task.builder().delayTicks(20).intervalTicks(20).execute(() -> {
                for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
                    double lastWalkspeed = onlinePlayer.get(Keys.WALKING_SPEED).orElse(baseWalkspeed.get());
                    double newWalkspeed = AmberStatsService.instance().collectKeyValue(onlinePlayer, Keys.WALKING_SPEED, baseWalkspeed.get());
                    if (Math.abs(lastWalkspeed - newWalkspeed) > GenericMath.DBL_EPSILON) {
                        onlinePlayer.offer(Keys.WALKING_SPEED, newWalkspeed);
                    }
                }
            }).submit(container);
        }
    }

}
