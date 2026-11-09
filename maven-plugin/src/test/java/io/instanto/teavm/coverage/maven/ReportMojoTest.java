package io.instanto.teavm.coverage.maven;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class ReportMojoTest {
  @Test
  public void reportsNothingWhenTestsAreSkipped() throws Exception {
    for (String skipping : List.of("skipTests", "skipTestCompilation")) {
      var mojo = mojoWithoutBrowserOutput();
      set(mojo, skipping, true);
      mojo.execute();
    }
  }

  @Test
  public void stillFailsWhenTestsRanWithoutBrowserOutput() throws Exception {
    var mojo = mojoWithoutBrowserOutput();
    assertThrows(MojoExecutionException.class, mojo::execute);
  }

  private static ReportMojo mojoWithoutBrowserOutput() throws Exception {
    File target = Files.createTempDirectory("teacoco-report").toFile();
    var mojo = new ReportMojo();
    set(mojo, "dataDirectory", new File(target, "teavm-coverage"));
    set(mojo, "surefireReports", new File(target, "surefire-reports"));
    set(mojo, "failsafeReports", new File(target, "failsafe-reports"));
    set(mojo, "outputFile", new File(target, "teavm-coverage.xml"));
    set(mojo, "sourceRoots", List.of());
    set(mojo, "mode", "jacoco");
    return mojo;
  }

  private static void set(ReportMojo mojo, String name, Object value) throws Exception {
    var field = ReportMojo.class.getDeclaredField(name);
    field.setAccessible(true);
    field.set(mojo, value);
  }
}
