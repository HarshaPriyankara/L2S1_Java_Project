package Controllers.CourseMaterialControllers;

import DAO.CourseMaterialDAO;
import Utils.FileStorageSupport;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseMaterialController {
    private static final Logger LOGGER = Logger.getLogger(CourseMaterialController.class.getName());
    private final CourseMaterialDAO courseMaterialDAO = new CourseMaterialDAO();
    private final FileStorageSupport fileStorageSupport = new FileStorageSupport() {
        @Override
        protected File uploadDirectory() {
            return new File("uploads/course_materials");
        }
    };

    public List<CourseMaterialRow> loadMaterialsByLecturer(String lecturerId) {
        return courseMaterialDAO.getMaterialsByLecturer(lecturerId);
    }

    public List<String> getCourseCodesByLecturer(String lecturerId) {
        return courseMaterialDAO.getCourseCodesByLecturer(lecturerId);
    }

    public CourseMaterialOperationResult addMaterial(CourseMaterialFormData formData) {
        String validationMessage = validate(formData, true);
        if (validationMessage != null) {
            return new CourseMaterialOperationResult(false, validationMessage);
        }

        String savedPath = saveMaterialFile(formData);
        if (savedPath == null) {
            return new CourseMaterialOperationResult(false, "File upload failed.");
        }

        boolean saved = courseMaterialDAO.addMaterial(
                formData.getTitle(),
                formData.getCourseCode(),
                formData.getUploadedBy(),
                savedPath
        );
        if (!saved) {
            deleteStoredFile(savedPath);
            return new CourseMaterialOperationResult(false, "Error! Check if Course Code or Lecturer ID is valid.");
        }

        return new CourseMaterialOperationResult(true, "Material uploaded.");
    }

    public CourseMaterialOperationResult updateMaterial(CourseMaterialFormData formData) {
        if (formData.getMaterialId() < 0) {
            return new CourseMaterialOperationResult(false, "Select a material from the table to update.");
        }

        String validationMessage = validate(formData, false);
        if (validationMessage != null) {
            return new CourseMaterialOperationResult(false, validationMessage);
        }

        String finalPath = isStoredCourseMaterialPath(formData.getFilePath())
                ? formData.getFilePath()
                : saveMaterialFile(formData);
        if (finalPath == null) {
            return new CourseMaterialOperationResult(false, "Unable to save updated file.");
        }

        boolean updated = courseMaterialDAO.updateMaterial(
                formData.getMaterialId(),
                formData.getTitle(),
                formData.getCourseCode(),
                finalPath
        );

        if (!updated) {
            return new CourseMaterialOperationResult(false, "Material update failed.");
        }

        return new CourseMaterialOperationResult(true, "Material updated.");
    }

    public CourseMaterialOperationResult deleteMaterial(int materialId, String lecturerId, String filePath) {
        if (materialId < 0) {
            return new CourseMaterialOperationResult(false, "Select a material from the table to delete.");
        }

        boolean deleted = courseMaterialDAO.deleteMaterial(materialId, lecturerId);
        if (!deleted) {
            return new CourseMaterialOperationResult(false, "Material delete failed.");
        }

        deleteStoredFile(filePath);
        return new CourseMaterialOperationResult(true, "Material deleted.");
    }

    private String validate(CourseMaterialFormData formData, boolean includeUploadedBy) {
        if (formData.getTitle().isEmpty() || formData.getCourseCode().isEmpty() || formData.getFilePath().isEmpty()) {
            return includeUploadedBy
                    ? "All fields are required!"
                    : "Title, course code, and file path are required.";
        }

        if (includeUploadedBy && formData.getUploadedBy().isEmpty()) {
            return "All fields are required!";
        }

        return null;
    }

    private String saveMaterialFile(CourseMaterialFormData formData) {
        String ownerKey = formData.getUploadedBy() + "_" + formData.getCourseCode() + "_" + formData.getTitle();
        return fileStorageSupport.saveFile(new File(formData.getFilePath()), ownerKey, LocalDate.now());
    }

    private boolean isStoredCourseMaterialPath(String filePath) {
        String normalizedPath = filePath.replace("\\", "/");
        return normalizedPath.startsWith("uploads/course_materials/")
                || normalizedPath.contains("/uploads/course_materials/");
    }

    private void deleteStoredFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.log(Level.WARNING, "Unable to delete stored course material file: {0}", filePath);
            }
        }
    }
}
