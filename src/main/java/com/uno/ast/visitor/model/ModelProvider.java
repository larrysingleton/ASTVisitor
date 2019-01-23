package com.uno.ast.visitor.model;

import java.util.ArrayList;
import java.util.List;

public enum ModelProvider {
    INSTANCE;

    private List<ProgramElement> progElements = new ArrayList<>();

    private ModelProvider() {
    }

    public void addProgramElements(String pkgName, String className, String methodName, String fieldName, boolean isRetVoid,
            int parmSize) {
        progElements.add(new ProgramElement(pkgName, className, methodName, fieldName, isRetVoid, parmSize));
    }

    public void clearProgramElements() {
        progElements.clear();
    }

    public List<ProgramElement> getProgramElements() {
        return progElements;
    }
}
