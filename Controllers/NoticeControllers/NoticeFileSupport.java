package Controllers.NoticeControllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class NoticeFileSupport {
    private static final String PROJECT_MARKER = "L2S1_Java_Project.iml";

    private NoticeFileSupport() {
    }

    static String saveContent(String title, String content) throws IOException {
        String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "_").trim();
        if (sanitizedTitle.isEmpty()) {
            sanitizedTitle = "notice";
        }

        Path folderPath = getProjectRoot().resolve(Paths.get("notices", sanitizedTitle));
        Files.createDirectories(folderPath);

        Path filePath = folderPath.resolve(sanitizedTitle + ".txt");
        Files.writeString(filePath, content, StandardCharsets.UTF_8);

        return getProjectRoot().relativize(filePath).toString().replace('\\', '/');
    }

    static NoticeContentResult readContent(String pathText) {
        if (pathText == null || pathText.isBlank()) {
            return new NoticeContentResult(false, null, "Notice content is not available.");
        }

        Path path = resolvePath(pathText);
        if (!Files.exists(path)) {
            return new NoticeContentResult(false, null, "Notice file is missing.");
        }

        try {
            return new NoticeContentResult(true, Files.readString(path, StandardCharsets.UTF_8), null);
        } catch (IOException ex) {
            return new NoticeContentResult(false, null, "Unable to read the selected notice.");
        }
    }

    private static Path resolvePath(String pathText) {
        Path rawPath = Paths.get(pathText);
        if (rawPath.isAbsolute()) {
            return rawPath.normalize();
        }

        Path directPath = rawPath.normalize();
        if (Files.exists(directPath)) {
            return directPath;
        }

        return getProjectRoot().resolve(rawPath).normalize();
    }

    private static Path getProjectRoot() {
        Path current = Paths.get("").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve(PROJECT_MARKER))) {
                return current;
            }
            current = current.getParent();
        }

        return Paths.get("").toAbsolutePath().normalize();
    }
}
