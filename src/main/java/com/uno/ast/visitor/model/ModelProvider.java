package com.uno.ast.visitor.model;

import java.util.ArrayList;
import java.util.List;

public enum ModelProvider {
   INSTANCE;

   private List<ProgramElement> progElements = new ArrayList<ProgramElement>();;

   private ModelProvider() {
   }

   public void addProgramElements(String pkgName, String className, String methodName, boolean isRetVoid, int parmSize) {
      progElements.add(new ProgramElement(pkgName, className, methodName, isRetVoid, parmSize));
   }

   public List<ProgramElement> getProgramElements() {
      return progElements;
   }

   public void clearProgramElements() {
      progElements.clear();
   }
}
