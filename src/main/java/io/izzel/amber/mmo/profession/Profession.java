package io.izzel.amber.mmo.profession;

import io.izzel.amber.mmo.util.Tagged;
import org.spongepowered.api.text.Text;

import java.util.List;

public interface Profession extends Tagged {

    String id();

    Text getName();

    List<Text> getDescription();

}
