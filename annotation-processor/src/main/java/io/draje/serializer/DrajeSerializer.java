package io.draje.serializer;

import io.draje.model.Draje;

import java.io.IOException;
import java.util.List;

public interface DrajeSerializer {

    void serialize(List<Draje> drajes) throws IOException;

}
