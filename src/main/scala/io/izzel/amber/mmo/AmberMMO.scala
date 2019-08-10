package io.izzel.amber.mmo

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin

@Plugin(id = "ambermmo", name = "AmberMMO", description = "An AmberMMO mmo plugin.")
class AmberMMO @Inject
(
  private val logger: Logger
) {

  @Listener
  def onServerStart(event: GameStartedServerEvent): Unit = {

  }

}