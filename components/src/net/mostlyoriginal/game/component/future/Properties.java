package net.mostlyoriginal.game.component.future;

import com.artemis.Component;
import com.artemis.annotations.FluidMethod;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Generic dynamic properties container.
 * Better to use concrete components in most cases.
 * @author Daan van Yperen
 */
public class Properties extends Component {
    public ObjectMap<String, Object> properties = new ObjectMap<>();

    public void set(String key, boolean value) {
        properties.put(key, value);
    }

    public void set(String key, int value) {
        properties.put(key, value);
    }

    public void set(String key, String value) {
        properties.put(key, value);
    }

    public void set(String key, Object value) {
        properties.put(key, value);
    }

    public int getInt(String key) {
        return (int) properties.get(key);
    }

    @SuppressWarnings("unchecked")
    @FluidMethod(exclude = true)
    // not named `get` cause fluid entity generator can't handle.
    public <T extends Enum> T getEnum(String key, Class<T> enumClazz) {
        return (T) properties.get(key);
    }

    public String getString(String key) {
        return (String) properties.get(key);
    }

    public Boolean getBoolean(String key) {
        return (Boolean) properties.get(key);
    }
}
