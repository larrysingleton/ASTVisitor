package com.uno.ast.visitor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.uno.ast.visitor.util.UTFile;

public class ASTSimpleVisitorCounter {

    public static String analyzeAST(Path path) throws IOException {
        String javaFilePath = path.toAbsolutePath().toString();
        final CompilationUnit cu = getCompilationUnit(javaFilePath);

        ASTVisitorCounter visitor = new ASTVisitorCounter();
        cu.accept(visitor);
        return javaFilePath + "," + visitor.toString();
    }

    public static void main(String[] args) throws IOException {

        String targetPath = parseInputArgs(args);
        List<Path> javaFiles = UTFile.getFileListRecursive(targetPath, "*.java");

        for (Path path : javaFiles) {
            String ret = analyzeAST(path);
            System.out.println(ret);
        }

    }

    private static CompilationUnit getCompilationUnit(String sourcePath) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        String entireFile = UTFile.readEntireFile(sourcePath);
        parser.setSource(entireFile.toCharArray());

        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        parser.setCompilerOptions(options);

        return (CompilationUnit) parser.createAST(null);
    }

    private static String parseInputArgs(String[] args) {
        String inputPath = null;
        Options options = new Options();

        Option input = new Option("i", "input", true, "javaProjectRootPath");
        input.setRequired(true);
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            inputPath = cmd.getOptionValue("input");
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ASTSimpleVisitorCounter", options);
            System.exit(1);
        }

        return inputPath;
    }
}
