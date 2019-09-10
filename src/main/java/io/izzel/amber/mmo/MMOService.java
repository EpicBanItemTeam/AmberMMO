package io.izzel.amber.mmo;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.data.PlayerData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NonnullByDefault
@Singleton
public class MMOService {

    private final List<String> attributes = new ArrayList<>();

    @Inject
    public MMOService(PluginContainer container, ServiceManager serviceManager, EventManager eventManager) {
        serviceManager.setProvider(container, MMOService.class, this);
        eventManager.registerListener(container, GameInitializationEvent.class, event -> {
            DataRegistration.builder()
                .dataClass(PlayerData.Mutable.class)
                .immutableClass(PlayerData.Immutable.class)
                .builder(new PlayerData.Builder())
                .id("ambermmo")
                .name("AmberMMO")
                .build();
            try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                eventManager.post(new RegistryEvent(frame.getCurrentCause()));
            }
        });
    }

    public List<String> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    public PlayerData.Mutable getPlayerData(Player player) {
        return player.getOrCreate(PlayerData.Mutable.class).orElseThrow(RuntimeException::new);
    }

    public class RegistryEvent implements Event {

        private final Cause cause;

        public RegistryEvent(Cause cause) {
            this.cause = cause;
        }

        public void register(String id) {
            attributes.add(id);
        }

        @Override
        public Cause getCause() {
            return this.cause;
        }
    }
}
