package Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public abstract class FileStorageSupport {
    protected abstract File uploadDirectory();

    public String saveFile(File sourceFile, String ownerId, LocalDate date) {
        try {
            if (sourceFile == null || !sourceFile.exists()) {
                return null;
            }

            File directory = uploadDirectory();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = sourceFile.getName();
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = fileName.substring(dotIndex);
            }

            String safeOwnerId = ownerId.replaceAll("[^a-zA-Z0-9]", "_");
            String newFileName = safeOwnerId + "_" + date + "_" + System.currentTimeMillis() + extension;
            File destination = new File(directory, newFileName);
            Files.copy(sourceFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destination.getPath().replace("\\", "/");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
