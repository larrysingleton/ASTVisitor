package com.uno.ast.visitor;

import java.nio.file.Path;
import java.text.ParseException;
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
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.uno.ast.visitor.util.UTFile;

public class ASTSimpleVisitorCounter {

	public static int methodCounter;
	public static int classCounter;
	public static int fieldCounter;

	public static void main(String[] args) {

		String targetPath = parseInputArgs(args);
		List<Path> javaFiles = UTFile.getFileListRecursive(targetPath, "*.java");
		resetCounters();

		for (Path path : javaFiles) {
			String javaFilePath = path.toAbsolutePath().toString();

			final CompilationUnit cu = getCompilationUnit(javaFilePath);
			cu.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration typeDecl) {
					++classCounter;
					return true;
				}

				@Override
				public boolean visit(MethodDeclaration methodDecl) {
					++methodCounter;
					return true;
				}

				@Override
				public boolean visit(FieldDeclaration fieldDecl) {
					++fieldCounter;
					return true;
				}
			});

			System.out.println(javaFilePath + "," + classCounter + "," + methodCounter + "," + fieldCounter);
		}
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

	private static CompilationUnit getCompilationUnit(String sourcePath) {
		ASTParser parser = ASTParser.newParser(AST.JLS11);

		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(
				"public class A { int i = 9;  \n int j;\n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; public void A() {int x = 5;}\n }"
						.toCharArray());

		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}

	private static void resetCounters() {
		ASTSimpleVisitorCounter.fieldCounter = 0;
		ASTSimpleVisitorCounter.classCounter = 0;
		ASTSimpleVisitorCounter.methodCounter = 0;
	}
}
