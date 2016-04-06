package com.databricks.bazel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDir {

  public static void zipDir(File dstJar, File baseDir, File[] files) throws FileNotFoundException, IOException {
    FileOutputStream fos = new FileOutputStream(dstJar);
    ZipOutputStream zos = new ZipOutputStream(fos);

    for (File f : files) {
      String relativePath = baseDir.toURI().relativize(f.toURI()).getPath();
      addToZipFile(relativePath, f, zos);
    }

    zos.close();
    fos.close();
  }

  private static void addToZipFile(String fileName, File f, ZipOutputStream zos) throws FileNotFoundException, IOException {
    ZipEntry zipEntry = new ZipEntry(fileName);
    zipEntry.setTime(0);
    zos.putNextEntry(zipEntry);

    if (f.isFile()) {
      FileInputStream fis = new FileInputStream(f);
      byte[] bytes = new byte[1024];
      int length;
      while ((length = fis.read(bytes)) >= 0) {
        zos.write(bytes, 0, length);
      }
      fis.close();
    }

    zos.closeEntry();
  }
}
