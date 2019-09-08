package io.izzel.amber.mmo.data

import java.util.Optional

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData
import org.spongepowered.api.data.merge.MergeFunction
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.data.{DataContainer, DataHolder, DataQuery, DataView}

import scala.collection.mutable
import scala.jdk.OptionConverters._

class PlayerData(val data: mutable.Map[String, Double])
  extends AbstractData[PlayerData, Immutable] {

  override def registerGettersAndSetters(): Unit = ()

  override def fill(dataHolder: DataHolder, overlap: MergeFunction): Optional[PlayerData] = {
    val original = dataHolder.get(classOf[PlayerData]).orElse(null)
    val data = overlap.merge(original, this)
    for ((k, v) <- data.data) this.data(k) = v
    Some(data).toJava
  }

  override def from(container: DataContainer): Optional[PlayerData] = Some(Data.from(container, this)).toJava

  override def copy(): PlayerData = new PlayerData(data)

  override def asImmutable(): Immutable = new Immutable(data.to(Map))

  override def getContentVersion: Int = 0

  override def fillContainer(dataContainer: DataContainer): DataContainer = {
    val container = super.fillContainer(dataContainer)
    for ((k, v) <- data) container.set(DataQuery.of(k), v)
    container
  }
}

class Immutable(val data: Map[String, Double]) extends AbstractImmutableData[Immutable, PlayerData] {

  override def registerGetters(): Unit = ()

  override def asMutable(): PlayerData = new PlayerData(data.to(mutable.Map))

  override def getContentVersion: Int = 0

  override def fillContainer(dataContainer: DataContainer): DataContainer = {
    val container = super.fillContainer(dataContainer)
    for ((k, v) <- data) container.set(DataQuery.of(k), v)
    container
  }

}

class Builder extends AbstractDataBuilder[PlayerData](classOf[PlayerData], 0)
  with DataManipulatorBuilder[PlayerData, Immutable] {
  override def buildContent(container: DataView): Optional[PlayerData] = Some(Data.from(container, create())).toJava

  override def create(): PlayerData = new PlayerData(mutable.Map())

  override def createFrom(dataHolder: DataHolder): Optional[PlayerData] = create().fill(dataHolder)
}

object Data {
  def from(container: DataView, data: PlayerData): PlayerData = {
    val service = Sponge.getServiceManager.provideUnchecked(classOf[MMOService])
    for (id <- service.attributes) {
      data.data(id) = container.getDouble(DataQuery.of(id)).orElse(0D)
    }
    data
  }
}