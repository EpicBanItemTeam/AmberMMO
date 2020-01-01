package io.izzel.amber.mmo;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.mmo.drops.DropTableService;
import org.spongepowered.api.plugin.Plugin;

import java.util.Objects;

@Plugin(id = "ambermmo", name = "AmberMMO", description = "An AmberMMO mmo plugin.")
public class AmberMMO {

    @Inject
    public AmberMMO(AmberLocale locale, DropTableService drop) {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(drop);
    }

}
