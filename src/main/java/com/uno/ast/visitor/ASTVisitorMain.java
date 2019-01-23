package com.uno.ast.visitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTVisitorMain {

    public static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

    public static void main(String[] args) {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setSource("public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }"
                .toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        cu.accept(new ASTVisitor() {

            Set<String> names = new HashSet<>();

            @Override
            public boolean visit(SimpleName node) {
                if (this.names.contains(node.getIdentifier())) {
                    System.out.println("Usage of '" + node + "' at line " + cu.getLineNumber(node.getStartPosition()));
                }
                return true;
            }

            @Override
            public boolean visit(VariableDeclarationFragment node) {
                SimpleName name = node.getName();
                this.names.add(name.getIdentifier());
                System.out.println("Declaration of '" + name + "' at line" + cu.getLineNumber(name.getStartPosition()));
                return false; // do not continue to avoid usage info
            }

        });
    }

    public void analyze() throws CoreException {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) {
            if (!project.isOpen() || !project.isNatureEnabled(JAVA_NATURE)) {
                continue;
            }
            analyzeJavaProject(JavaCore.create(project).getPackageFragments());
        }
    }

    private void analyzeComplicationUnit(ICompilationUnit[] compilationUnits) {
        for (ICompilationUnit iUnit : compilationUnits) {
            CompilationUnit cUnit = parse(iUnit);
            DeclarationVisitor declVisitor = new DeclarationVisitor();
            cUnit.accept(declVisitor);
        }

    }

    private void analyzeJavaProject(IPackageFragment[] packageFragments) throws JavaModelException {
        for (IPackageFragment iPackage : packageFragments) {
            if (iPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                if (iPackage.getCompilationUnits().length < 1) {
                    continue;
                }
                analyzeComplicationUnit(iPackage.getCompilationUnits());
            }
        }

    }

    private CompilationUnit parse(ICompilationUnit iUnit) {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        parser.setCompilerOptions(options);
        parser.setSource(iUnit);

        return (CompilationUnit) parser.createAST(null);
    }

}
