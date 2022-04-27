package com.foco.context.asserts.extensions;


public class AssertObject extends AbstractIAssert<AssertObject, Object> {

    public AssertObject(Object param) {
        super(param);
        super.child = this;
    }

}
