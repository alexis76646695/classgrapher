package core;

import java.io.Serializable;

public enum Tool implements Serializable {
    CLASS, ABSTRACT_CLASS, INTERFACE_CLASS, RELATION, INHERIT_RELATION,
    INTERFACE_RELATION, AGGREGATION_RELATION, COMPOSITION_RELATION, ANY
}
