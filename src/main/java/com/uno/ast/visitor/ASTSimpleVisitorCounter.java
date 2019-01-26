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

	static ASTCounter counter = new ASTCounter();
	static boolean debugFlag = false;

	public static String analyzeAST(Path path) {
		String javaFilePath = path.toAbsolutePath().toString();
		final CompilationUnit cu = getCompilationUnit(javaFilePath);

		if (cu == null) {
			return null;
		}
		
		ASTVisitorCounter visitor = new ASTVisitorCounter();
		cu.accept(visitor);
		counter.addCounter(visitor.getASTCounter());
		return javaFilePath + "," + visitor.toString();
	}

	public static void main(String[] args)  {

		String targetPath = parseInputArgs(args);
		List<Path> javaFiles = UTFile.getFileListRecursive(targetPath, "*.java");

		for (Path path : javaFiles) {
			String ret = analyzeAST(path);

			if (ret != null && debugFlag) {
				System.out.println(ret);
			}
		}

		System.out.println(targetPath + "," + counter.toString());
	}

	private static CompilationUnit getCompilationUnit(String sourcePath)  {
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

		ASTParser parser = ASTParser.newParser(AST.JLS11);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setCompilerOptions(options);

		String entireFile;
		try {
			entireFile = UTFile.readEntireFile(sourcePath);
			parser.setSource(entireFile.toCharArray());
		} catch (IOException e) {
			return null;
		}

		return (CompilationUnit) parser.createAST(null);
	}

	private static String parseInputArgs(String[] args) {
		String inputPath = null;
		Options options = new Options();

		Option input = new Option("i", "input", true, "javaProjectRootPath");
		Option debug = new Option("d", "debug", false, "debug flag");

		input.setRequired(true);
		debug.setRequired(false);

		options.addOption(input);
		options.addOption(debug);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			inputPath = cmd.getOptionValue("input");
			if (cmd.hasOption("debug")) {
				debugFlag = true;
			}
		} catch (org.apache.commons.cli.ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("ASTSimpleVisitorCounter", options);
			System.exit(1);
		}

		return inputPath;
	}
}
