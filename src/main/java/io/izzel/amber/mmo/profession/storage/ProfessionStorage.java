package io.izzel.amber.mmo.profession.storage;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.izzel.amber.mmo.profession.StoredProfession;
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

    private static final TypeToken<StoredProfessionImpl> TOKEN = TypeToken.of(StoredProfessionImpl.class);

    @Inject @ConfigDir(sharedRoot = false) private Path dir;

    private final Map<String, StoredProfession> map = new HashMap<>();

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
                    StoredProfessionImpl storedProfession = entry.getValue().getValue(TOKEN);
                    if (storedProfession != null) {
                        storedProfession.setId(id);
                        storedProfession.getTags().add(id); //
                        map.put(id, storedProfession);
                    }
                }
            } else {
                loader.save(load);
            }
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public Map<String, StoredProfession> getLoaded() {
        return map;
    }
}
