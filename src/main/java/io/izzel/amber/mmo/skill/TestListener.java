package io.izzel.amber.mmo.skill;

import com.google.inject.Inject;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;

public class TestListener {

    @Inject private SkillService skillService;

    @Listener
    public void on(MoveEntityEvent event) {
        Entity entity = event.getTargetEntity();
        SkillSubject skillSubject = skillService.getOrCreate(entity).get();


    }
}
