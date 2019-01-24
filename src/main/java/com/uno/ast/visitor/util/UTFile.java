/**
 */
package com.uno.ast.visitor.util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import input.IRuleInfo;

/**
 * @author
 * @date
 * @since Java SE 8
 */
public class UTFile  {
   /**
    * @ pattern "*.java"
    */
   public static List<Path> getFileListRecursive(String dir, String pattern) {
      List<Path> list = new ArrayList<Path>();
      walkDir(dir, list, pattern);
      return list;
   }

   static void walkDir(String dir, List<Path> list, String pattern) {
      Path startingDir = Paths.get(dir);
      UTFileFinder walkFiles = new UTFileFinder(pattern, list);
      try {
         Files.walkFileTree(startingDir, walkFiles);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   /**
    * Read entire file.
    * 
    * @param filename
    *           the filename
    * @return the string
    * @throws IOException
    *            Signals that an I/O exception has occurred.
    */
   public static String readEntireFile(String filename) throws IOException {
      FileReader in = new FileReader(filename);
      StringBuilder contents = new StringBuilder();
      char[] buffer = new char[4096];
      int read = 0;
      do {
         contents.append(buffer, 0, read);
         read = in.read(buffer);
      } while (read >= 0);
      in.close();
      return contents.toString();
   }

}
