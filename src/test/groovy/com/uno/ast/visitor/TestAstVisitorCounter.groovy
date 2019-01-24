package com.uno.ast.visitor

import java.nio.file.Path
import java.nio.file.Paths

import spock.lang.Specification

class TestAstVisitorCounter extends Specification {

    ASTSimpleVisitorCounter visitorCounter = new ASTSimpleVisitorCounter();

    def "analyze method declarations in an interface class"() {
        setup:
        ClassLoader classLoader = getClass().getClassLoader();
        Path path = Paths.get(classLoader.getResource("IBaseView.java.testfile").toURI());

        when:
        String ret = visitorCounter.analyzeAST(path);
        // println ret;

        then:
        ret == (path.toString() + ",1,0,0")
    }
}
