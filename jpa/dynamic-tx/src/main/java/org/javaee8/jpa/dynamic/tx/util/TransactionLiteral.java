package org.javaee8.jpa.dynamic.tx.util;

import static javax.transaction.Transactional.TxType.REQUIRED;

import javax.enterprise.util.AnnotationLiteral;
import javax.transaction.Transactional;

@SuppressWarnings("all")
public class TransactionLiteral extends AnnotationLiteral<Transactional> implements Transactional {

    private static final long serialVersionUID = 1L;

    @Override
    public TxType value() {
        return REQUIRED;
    }

    @Override
    public Class<?>[] rollbackOn() {
        return new Class[0];
    }

    @Override
    public Class<?>[] dontRollbackOn() {
        return new Class[0];
    }

}