package io.github.eliux.mega.cmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.eliux.mega.error.MegaInvalidResponseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Exported info")
@Tag("export")
public class ExportInfoTest {

  @DisplayName("Parse export Info")
  @Test
  public void parseExportInfoShouldBeOk() {
    //Given
    String validExportInfo = "Exported /megacmd4j/level2: https://mega.nz/#F!8OQxgYgY!xxxg0kyQ9wibextVq5FZbQ";

    //When
    final ExportInfo exportInfo = ExportInfo.parseExportInfo(validExportInfo);

    //Then
    assertNotNull(exportInfo);
    assertEquals("/megacmd4j/level2", exportInfo.getRemotePath());
    assertEquals("https://mega.nz/#F!8OQxgYgY!xxxg0kyQ9wibextVq5FZbQ",
        exportInfo.getPublicLink());
  }

  @DisplayName("Given invalid remotePath, when parse export info then fail")
  @Test
  public void failWhenParseInvalidExportInfo() {
    String entryWithInvalidRemotePath = "megacmd4j/level2(folder, shared as exported " +
        " folder link: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)";

    assertThrows(MegaInvalidResponseException.class,
        () -> ExportInfo.parseExportListInfo(entryWithInvalidRemotePath));
    ;
  }

  @DisplayName("Given invalid public link prefix, when parse export list info then fail")
  @Test
  public void failWhenParseLinkWithInvalidPrefix() {
    String entryWithInvalidRemotePath = "megacmd4j/level2 (folder, shared as exported " +
        " folder content: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)";

    assertThrows(MegaInvalidResponseException.class,
        () -> ExportInfo.parseExportListInfo(entryWithInvalidRemotePath));
  }

  @DisplayName("Given invalid public link ending, when parse export list info then fail")
  @Test
  public void failWhenParseExportListInfoWithInvalidPublicLinkEnding() {
    String entryWithInvalidRemotePath = "megacmd4j/level2 (folder, shared as exported " +
        " folder content: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA";

    assertThrows(MegaInvalidResponseException.class,
        () -> ExportInfo.parseExportListInfo(entryWithInvalidRemotePath));
  }

  @DisplayName("Given public link with invalid MEGA.nz url, when parse export list info then fail")
  @Test
  public void failWhenParseExportListInfoWithInvalidMegaPublicLink() {
    String responseWithInvalidMegaUrl =
        "megacmd4j (folder, shared as exported permanent " +
            "folder link: http://mega.com/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)";

    assertThrows(MegaInvalidResponseException.class,
        () -> ExportInfo.parseExportListInfo(responseWithInvalidMegaUrl));
  }

  @DisplayName("Given remote path with sub-paths, when parse export list info then success")
  @Test
  public void succeedWhenParseExportListInfoWithValidRemotePathWithSubPaths() {
    //Given
    String responseWithRemotePathWithSubPaths =
        "megacmd4j/level2/level3 (folder, shared as exported permanent " +
            "folder link: https://mega.nz/folder/xSx1ybja#cBos0Ly_71GXu6v-rKZXzg)";

    //When
    final ExportInfo exportInfo =
        ExportInfo.parseExportListInfo(responseWithRemotePathWithSubPaths);

    //Then
    assertEquals("megacmd4j/level2/level3", exportInfo.getRemotePath());
    assertEquals(
        "https://mega.nz/folder/xSx1ybja#cBos0Ly_71GXu6v-rKZXzg",
        exportInfo.getPublicLink()
    );
  }

  @DisplayName("Given remote path with single folder, when parse export list info then success")
  @Test
  public void succeedWhenParseExportListInfoWithSingleFolder() {
    //Given
    String responseWithRemotePathWithSingleFolder =
        "megacmd4j (folder, shared as exported permanent " +
            "folder link: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)";

    //When
    final ExportInfo exportInfo =
        ExportInfo.parseExportListInfo(responseWithRemotePathWithSingleFolder);

    //Then
    assertEquals("megacmd4j", exportInfo.getRemotePath());
    assertEquals(
        "https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA",
        exportInfo.getPublicLink()
    );
  }

  @DisplayName("Given public non-HTTPS link, when parse export list info then success")
  @Test
  public void succeedWhenParseListInfoWithNonSecureLink() {
    //Given
    String responseWithHttp =
        "megacmd4j (folder, shared as exported permanent " +
            "folder link: http://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)";

    //When
    final ExportInfo exportInfo =
        ExportInfo.parseExportListInfo(responseWithHttp);

    //Then
    assertEquals("megacmd4j", exportInfo.getRemotePath());
    assertEquals(
        "http://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA",
        exportInfo.getPublicLink()
    );
  }
}
