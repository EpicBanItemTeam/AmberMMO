package io.izzel.amber.mmo;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.mmo.profession.ProfessionService;
import org.spongepowered.api.plugin.Plugin;

import java.util.Objects;

@Plugin(id = "ambermmo", name = "AmberMMO", description = "An AmberMMO mmo plugin.")
public class AmberMMO {

    @Inject
    public AmberMMO(AmberLocale locale, MMOService service, ProfessionService profession) {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(service);
        Objects.requireNonNull(profession);
    }

}
