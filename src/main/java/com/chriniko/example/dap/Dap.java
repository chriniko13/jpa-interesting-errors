package com.chriniko.example.dap;

import com.chriniko.example.domain.Entity;

import java.io.Serializable;
import java.util.Collection;

/*
    Note: Dap == Data Access Pattern.
 */
public interface Dap<ENT extends Entity, ID extends Serializable> {

    void add(ENT ent);

    void delete(ID id);

    void delete(ENT ent);

    void deleteAll();

    ENT update(ENT ent);

    ENT find(ID id);

    Collection<ENT> findAll();

}
