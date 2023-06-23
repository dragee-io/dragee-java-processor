package io.dragee.serializer;

import io.dragee.model.Dragee;

import java.io.IOException;
import java.util.List;

public interface DrageeSerializer {

    void serialize(List<Dragee> dragees) throws IOException;

}
