package com.uno.ast.visitor.model;

public class ProgramElement {
    private String pkgName;
    private String className;
    private String methodName;
    private String fieldName;
    private boolean isReturnVoid;
    private Integer parameterSize;

    public ProgramElement() {
    }

    public ProgramElement(String pkgName, String className, String methodName, boolean isRetVoid, int parmSize) {
        this.pkgName = pkgName;
        this.className = className;
        this.methodName = methodName;
        this.isReturnVoid = isRetVoid;
        this.parameterSize = parmSize;
    }

    public String getClassName() {
        return className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Integer getParameterSize() {
        return parameterSize;
    }

    public String getPkgName() {
        return pkgName;
    }

    public boolean isReturnVoid() {
        return isReturnVoid;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterSize(Integer parameterSize) {
        this.parameterSize = parameterSize;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setReturnVoid(boolean isReturnVoid) {
        this.isReturnVoid = isReturnVoid;
    }

    @Override
    public String toString() {
        return pkgName + "." + className + "." + methodName + "." + fieldName;
    }
}
