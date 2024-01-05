package io.dragee.serializer;

import io.dragee.exception.SerializationFailed;
import io.dragee.model.Dragee;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface DrageeSerializer {

    void serialize(Collection<Dragee> dragees) throws SerializationFailed;

}
