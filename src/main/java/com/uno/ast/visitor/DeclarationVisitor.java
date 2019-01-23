package com.uno.ast.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.uno.ast.visitor.model.ModelProvider;

public class DeclarationVisitor extends ASTVisitor {

    private String pkgName;
    private String className;
    private String methodName;
    private String fieldName;

    @Override
    public boolean visit(MethodDeclaration methodDecl) {
        methodName = methodDecl.getName().getIdentifier();

        Type returnType = methodDecl.getReturnType2();
        boolean isRetVoid = false;

        if (returnType.isPrimitiveType()) {
            PrimitiveType pt = (PrimitiveType) returnType;
            if (pt.getPrimitiveTypeCode().equals(PrimitiveType.VOID)) {
                isRetVoid = true;
            }
        }

        int parmSize = methodDecl.parameters().size();

        ModelProvider.INSTANCE.addProgramElements(pkgName, className, methodName, isRetVoid, parmSize);

        return super.visit(methodDecl);

    }

    @Override
    public boolean visit(PackageDeclaration pkgDecl) {
        pkgName = pkgDecl.getName().getFullyQualifiedName();
        return super.visit(pkgDecl);
    }

    @Override
    public boolean visit(TypeDeclaration typeDecl) {
        className = typeDecl.getName().getIdentifier();
        return super.visit(typeDecl);
    }
}
