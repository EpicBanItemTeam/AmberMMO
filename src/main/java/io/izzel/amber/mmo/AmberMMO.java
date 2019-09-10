package io.izzel.amber.mmo;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "ambermmo", name = "AmberMMO", description = "An AmberMMO mmo plugin.")
public class AmberMMO {

    private final AmberLocale locale;
    private final MMOService service;

    @Inject
    public AmberMMO(AmberLocale locale, MMOService service) {
        this.locale = locale;
        this.service = service;
    }

}
