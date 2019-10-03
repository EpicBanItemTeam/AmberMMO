package io.izzel.amber.mmo;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.mmo.profession.ProfessionService;
import io.izzel.amber.mmo.skill.SkillService;
import org.spongepowered.api.plugin.Plugin;

import java.util.Objects;

@Plugin(id = "ambermmo", name = "AmberMMO", description = "An AmberMMO mmo plugin.")
public class AmberMMO {

    @Inject
    public AmberMMO(AmberLocale locale, ProfessionService profession, SkillService skill) {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(profession);
        Objects.requireNonNull(skill);
    }

}
