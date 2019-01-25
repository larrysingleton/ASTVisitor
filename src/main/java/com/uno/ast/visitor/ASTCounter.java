package com.uno.ast.visitor;

public class ASTCounter {

	private int methodCounter;
	private int classCounter;
	private int fieldCounter;

	public ASTCounter(int classCount, int methodCount, int fieldCount) {
		this.setClassCounter(classCount);
		this.setMethodCounter(methodCount);
		this.setFieldCounter(fieldCount);
	}

	public ASTCounter() {
		this.setClassCounter(0);
		this.setMethodCounter(0);
		this.setFieldCounter(0);
	}

	public int getMethodCounter() {
		return methodCounter;
	}

	public void setMethodCounter(int methodCounter) {
		this.methodCounter = methodCounter;
	}

	public int getClassCounter() {
		return classCounter;
	}

	public void setClassCounter(int classCounter) {
		this.classCounter = classCounter;
	}

	public int getFieldCounter() {
		return fieldCounter;
	}

	public void setFieldCounter(int fieldCounter) {
		this.fieldCounter = fieldCounter;
	}
	
	public void addCounter(ASTCounter input) {
		this.classCounter += input.getClassCounter();
		this.methodCounter += input.getMethodCounter();
		this.fieldCounter += input.getFieldCounter();
	}
	
	public String toString() {
		return this.classCounter + "," + this.methodCounter + "," + this.fieldCounter;
	}

}
