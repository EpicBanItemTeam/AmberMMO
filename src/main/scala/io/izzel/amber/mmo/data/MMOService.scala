package io.izzel.amber.mmo.data

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataRegistration
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.{Event, EventManager}
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.service.ServiceManager

import scala.collection.mutable
import scala.util.Using

@Singleton
class MMOService @Inject
(
  private val container: PluginContainer,
  private val serviceManager: ServiceManager,
  private val eventManager: EventManager
) {
  private var attr: Set[String] = Set()
  serviceManager.setProvider(container, classOf[MMOService], this)
  eventManager.registerListener(container, classOf[GameInitializationEvent], _ => {
    Using(Sponge.getCauseStackManager.pushCauseFrame()) { frame =>
      DataRegistration.builder().dataClass(classOf[PlayerData])
        .builder(new Builder)
        .immutableClass(classOf[Immutable])
        .id("ambermmo").name("AmberMMO").build()
      val attr = mutable.Set[String]()
      eventManager.post(new RegistryEvent(frame.getCurrentCause, attr))
      this.attr = attr.to(Set)
    }
  })

  def attributes: Set[String] = attr

  def getPlayerData(player: Player): PlayerData = {
    player.getOrCreate(classOf[PlayerData]).get()
  }

  class RegistryEvent(cause: Cause, attr: mutable.Set[String]) extends Event {

    override def getCause: Cause = cause

    def registerAttribute(id: String): Unit = attr += id
  }

}
