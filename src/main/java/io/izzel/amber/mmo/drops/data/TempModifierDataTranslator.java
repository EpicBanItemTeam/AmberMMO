package io.izzel.amber.mmo.drops.data;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
@NonnullByDefault
public class TempModifierDataTranslator implements DataTranslator<Multimap> {

    public static final Class<Multimap> CLASS;

    static {
        CLASS = (Class<Multimap>) MultimapBuilder.hashKeys().arrayListValues().build().getClass();
    }

    @Override
    public String getId() {
        return "ambermmo:modifier";
    }

    @Override
    public String getName() {
        return "AmberMMO Temp Modifier Translator";
    }

    @Override
    public TypeToken<Multimap> getToken() {
        return TypeToken.of(CLASS);
    }

    @Override
    public Multimap<String, AmountTempModifier> translate(DataView view) throws InvalidDataException {
        Multimap<String, AmountTempModifier> ret = MultimapBuilder.hashKeys().arrayListValues().build();
        for (DataQuery key : view.getKeys(false)) {
            Optional<List<AmountTempModifier>> list = view.getSerializableList(key, AmountTempModifier.class);
            list.ifPresent(modifiers -> ret.putAll(key.toString(), modifiers));
        }
        return ret;
    }

    @Override
    public DataContainer translate(Multimap obj) throws InvalidDataException {
        Multimap<String, AmountTempModifier> map = ((Multimap<String, AmountTempModifier>) obj);
        DataContainer ret = DataContainer.createNew();
        for (Map.Entry<String, Collection<AmountTempModifier>> entry : map.asMap().entrySet()) {
            DataQuery query = DataQuery.of(entry.getKey());
            ret.set(query, entry.getValue());
        }
        return ret;
    }
}
