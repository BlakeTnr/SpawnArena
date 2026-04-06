package me.zeronull.spawnarena;

import de.exlll.configlib.Serializer;
import org.json.JSONObject;

public final class PlayerPreFightDataSerializer implements Serializer<PlayerPreFightData, String> {
    @Override
    public String serialize(final PlayerPreFightData obj) {
        try {
            return obj.toJsonObject().toString(4);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public PlayerPreFightData deserialize(final String str) {
        try {
            return new PlayerPreFightData(new JSONObject(str));
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}