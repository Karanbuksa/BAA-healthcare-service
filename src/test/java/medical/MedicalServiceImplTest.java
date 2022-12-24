package medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;


import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @Test
    public void checkBloodPressure_message_test() {
        String id = "88005553535";
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(id))
                .thenReturn(new PatientInfo(id, "1", "2",
                        LocalDate.of(1990, 2, 3),
                        new HealthInfo(BigDecimal.valueOf(36),
                                new BloodPressure(180, 60))));

        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);


        medicalService.checkBloodPressure(id, new BloodPressure(200, 70));


        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(String.format("Warning, patient with id: %s, need help", id), argumentCaptor.getValue());
    }

    @Test
    public void checkTemperature_message_test() {
        String id = "88005553535";
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(id))
                .thenReturn(new PatientInfo(id, "1", "2",
                        LocalDate.of(1990, 2, 3),
                        new HealthInfo(BigDecimal.valueOf(36),
                                new BloodPressure(180, 60))));

        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);


        medicalService.checkTemperature(id, BigDecimal.valueOf(32));


        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(String.format("Warning, patient with id: %s, need help", id), argumentCaptor.getValue());
    }

    @Test
    public void normalStats_no_message_test() {
        String id = "88005553535";
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(id))
                .thenReturn(new PatientInfo(id, "1", "2",
                        LocalDate.of(1990, 2, 3),
                        new HealthInfo(BigDecimal.valueOf(36),
                                new BloodPressure(180, 60))));

        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);


        medicalService.checkTemperature(id, BigDecimal.valueOf(37));
        medicalService.checkBloodPressure(id, new BloodPressure(180, 60));


        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }
}
