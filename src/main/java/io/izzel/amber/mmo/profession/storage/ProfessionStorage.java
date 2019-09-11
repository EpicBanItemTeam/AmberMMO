package io.izzel.amber.mmo.profession.storage;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.izzel.amber.mmo.profession.Profession;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ProfessionStorage {

    private static final TypeToken<StoredProfession> TOKEN = TypeToken.of(StoredProfession.class);

    @Inject @ConfigDir(sharedRoot = false) private Path dir;

    private final Map<String, Profession> map = new HashMap<>();

    public void load() {
        try {
            Path resolve = dir.resolve("professions.conf");
            if (!Files.isDirectory(dir)) Files.createDirectories(dir);
            if (!Files.exists(resolve)) Files.createFile(resolve);
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(resolve).build();
            CommentedConfigurationNode load = loader.load();
            if (load.getValueType() == ValueType.MAP) {
                for (Map.Entry<Object, ? extends CommentedConfigurationNode> entry : load.getChildrenMap().entrySet()) {
                    String id = entry.getKey().toString();
                    StoredProfession profession = entry.getValue().getValue(TOKEN);
                    if (profession != null) {
                        profession.setId(id);
                        profession.getTags().add(id); //
                        map.put(id, profession);
                    }
                }
            } else {
                loader.save(load);
            }
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Profession> getLoaded() {
        return map;
    }
}
