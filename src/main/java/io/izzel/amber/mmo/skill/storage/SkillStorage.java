package io.izzel.amber.mmo.skill.storage;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SkillStorage {

    @Inject @ConfigDir(sharedRoot = false)
    private Path root;

    @Inject
    private AmberLocale locale;

    private final Map<String, StoredSkill> map = new HashMap<>();

    public void loadAll(Map<String, Class<?>> map) {
        try {
            Path resolve = root.resolve("skills.conf");
            if (!Files.exists(resolve)) Files.createFile(resolve);
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(resolve).build();
            CommentedConfigurationNode load = loader.load();
            if (load.getValueType() == ValueType.MAP) {
                loadRec(load.getChildrenMap(), map);
            }
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    private void loadRec(Map<Object, ? extends CommentedConfigurationNode> map, Map<String, Class<?>> types) throws ObjectMappingException {
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> entry : map.entrySet()) {
            String key = Arrays.stream(entry.getValue().getPath()).map(String::valueOf).collect(Collectors.joining("."));
            if (entry.getValue().getValueType() == ValueType.MAP) {
                Map<Object, ? extends CommentedConfigurationNode> children = entry.getValue().getChildrenMap();
                if (children.containsKey("type")) {
                    String type = children.get("type").getString();
                    if (types.containsKey(type)) {
                        CommentedConfigurationNode skillNode = children.get("skill");
                        skillNode.getNode("id").setValue(key);
                        Object skill = skillNode.getValue(TypeToken.of(types.get(type)));
                        this.map.put(key, ((StoredSkill) skill));
                    } else {
                        locale.log("unknown-type", type, key);
                    }
                } else {
                    loadRec(children, types);
                }
            }
        }
    }

    public StoredSkill get(String id) {
        return this.map.get(id);
    }

}
