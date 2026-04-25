package Controllers.MedicalControllers;

import DAO.MedicalRecordDAO;
import Models.MedicalRecord;

import java.time.LocalDate;
import java.util.List;

public class MedicalManagementController {
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    public StudentMedicalController.MedicalRecordsResult loadAllMedicalRecords() {
        try {
            List<MedicalRecord> records = medicalRecordDAO.getAllMedicalRecords();
            return new StudentMedicalController.MedicalRecordsResult(records, null);
        } catch (Exception ex) {
            return new StudentMedicalController.MedicalRecordsResult(null, "Unable to load medical records: " + ex.getMessage());
        }
    }

    public StudentMedicalController.MedicalActionResult addMedical(MedicalManagementFormData formData) {
        String validationMessage = validateCommonFields(formData);
        if (validationMessage != null) {
            return new StudentMedicalController.MedicalActionResult(false, validationMessage);
        }

        try {
            medicalRecordDAO.addMedical(
                    formData.getRegNo(),
                    LocalDate.parse(formData.getSessionDate()),
                    formData.getSessionType(),
                    formData.getExamCourse(),
                    formData.getReason()
            );
            return new StudentMedicalController.MedicalActionResult(true, "Medical record added successfully.");
        } catch (Exception ex) {
            return new StudentMedicalController.MedicalActionResult(false, "Unable to add medical record: " + ex.getMessage());
        }
    }

    public StudentMedicalController.MedicalActionResult updateMedical(MedicalManagementFormData formData) {
        if (formData.getMedicalId().isBlank()) {
            return new StudentMedicalController.MedicalActionResult(false, "Please select a medical record to update.");
        }

        String validationMessage = validateCommonFields(formData);
        if (validationMessage != null) {
            return new StudentMedicalController.MedicalActionResult(false, validationMessage);
        }

        try {
            medicalRecordDAO.updateMedical(
                    Integer.parseInt(formData.getMedicalId()),
                    formData.getRegNo(),
                    LocalDate.parse(formData.getSessionDate()),
                    formData.getSessionType(),
                    formData.getExamCourse(),
                    formData.getReason(),
                    formData.isApproved()
            );
            return new StudentMedicalController.MedicalActionResult(true, "Medical record updated successfully.");
        } catch (Exception ex) {
            return new StudentMedicalController.MedicalActionResult(false, "Unable to update medical record: " + ex.getMessage());
        }
    }

    public StudentMedicalController.MedicalActionResult approveMedical(MedicalManagementFormData formData) {
        if (formData.getMedicalId().isBlank()) {
            return new StudentMedicalController.MedicalActionResult(false, "Please select a medical record to approve.");
        }

        MedicalManagementFormData approvedForm = new MedicalManagementFormData(
                formData.getMedicalId(),
                formData.getRegNo(),
                formData.getSessionDate(),
                formData.getSessionType(),
                formData.getExamCourse(),
                formData.getReason(),
                true
        );
        return updateMedical(approvedForm);
    }

    public StudentMedicalController.MedicalActionResult deleteMedical(String medicalId) {
        String normalizedId = medicalId == null ? "" : medicalId.trim();
        if (normalizedId.isBlank()) {
            return new StudentMedicalController.MedicalActionResult(false, "Please select a medical record to delete.");
        }

        try {
            medicalRecordDAO.deleteMedical(Integer.parseInt(normalizedId));
            return new StudentMedicalController.MedicalActionResult(true, "Medical record deleted successfully.");
        } catch (Exception ex) {
            return new StudentMedicalController.MedicalActionResult(false, "Unable to delete medical record: " + ex.getMessage());
        }
    }

    private String validateCommonFields(MedicalManagementFormData formData) {
        if (formData.getRegNo().isBlank()) {
            return "Registration number is required.";
        }

        if (formData.getReason().isBlank()) {
            return "Reason is required.";
        }

        try {
            LocalDate.parse(formData.getSessionDate());
        } catch (Exception ex) {
            return "Session date must be in yyyy-mm-dd format.";
        }

        if ("Exam".equals(formData.getSessionType()) && formData.getExamCourse().isBlank()) {
            return "Exam course is required for exam medicals.";
        }

        return null;
    }
}
