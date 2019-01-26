package com.uno.ast.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ASTVisitorCounter extends ASTVisitor {

    private int methodCounter;
    private int classCounter;
    private int fieldCounter;

    public ASTVisitorCounter() {
        this.methodCounter = 0;
        this.classCounter = 0;
        this.fieldCounter = 0;
    }

    public ASTCounter getASTCounter() {
        return new ASTCounter(classCounter, methodCounter, fieldCounter);
    }

    public void incClassCounter() {
        ++classCounter;
    }

    public void incFieldCounter() {
        ++fieldCounter;
    }

    public void incMethodCounter() {
        ++methodCounter;
    }

    @Override
    public String toString() {
        return classCounter + "," + methodCounter + "," + fieldCounter;
    }

    @Override
    public boolean visit(FieldDeclaration fieldDecl) {
        incFieldCounter();
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration methodDecl) {

        if (methodDecl.getBody() == null || methodDecl.getLength() == 0) {
            return false;
        }

        // guard against Anonymous Types
        try {
            // Used to exclude method declarations in an interface class
            TypeDeclaration parentTypeDecl = (TypeDeclaration) methodDecl.getParent();
            if (parentTypeDecl.isInterface()) {
                return false;
            }

        } catch (Exception e) {
            // Do nothing
        }

        incMethodCounter();
        return true;
    }

    @Override
    public boolean visit(TypeDeclaration typeDecl) {
        incClassCounter();
        return true;
    }
}
