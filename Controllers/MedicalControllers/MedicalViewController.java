package Controllers.MedicalControllers;

import DAO.MedicalRecordDAO;
import Models.MedicalRecord;

import java.util.List;

public class MedicalViewController {
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    public StudentMedicalController.MedicalRecordsResult loadMedicalRecords(String studentId) {
        try {
            List<MedicalRecord> records = (studentId == null || studentId.isBlank())
                    ? medicalRecordDAO.getAllMedicalRecords()
                    : medicalRecordDAO.getMedicalRecordsByStudent(studentId);
            return new StudentMedicalController.MedicalRecordsResult(records, null);
        } catch (Exception ex) {
            return new StudentMedicalController.MedicalRecordsResult(null, "Unable to load medical records: " + ex.getMessage());
        }
    }
}
