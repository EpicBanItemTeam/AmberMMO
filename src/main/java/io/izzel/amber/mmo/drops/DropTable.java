package io.izzel.amber.mmo.drops;

import com.google.common.collect.ImmutableList;
import io.izzel.amber.mmo.util.Identified;
import org.spongepowered.api.event.cause.Cause;

import java.util.List;
import java.util.Random;

// todo add Propertied
public interface DropTable extends Identified {

    Random RANDOM = new Random();

    void accepts(Cause cause);

    default List<DropTable> queryChild() {
        return ImmutableList.of();
    }

    default String getId() {
        return "";
    }

}
