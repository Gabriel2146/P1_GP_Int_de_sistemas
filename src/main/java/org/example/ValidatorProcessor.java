package org.example;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ValidatorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        boolean isValid = validateCsv(body);
        exchange.getIn().setHeader("isValidCsv", isValid);
    }

    private boolean validateCsv(String content) {
        if (content == null || content.trim().isEmpty()) return false;
        String[] lines = content.split("\\r?\\n");
        if (lines.length == 0) return false;
        String header = lines[0].trim();
        if (!header.equals("patient_id,full_name,appointment_date,insurance_code")) {
            return false;
        }

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) return false; // Fila vacía
            String[] columns = line.split(",", -1);
            if (columns.length != 4) return false;

            String patientId = columns[0].trim();
            String fullName = columns[1].trim();
            String appointmentDate = columns[2].trim();
            String insuranceCode = columns[3].trim();

            if (patientId.isEmpty() || fullName.isEmpty() || appointmentDate.isEmpty() || insuranceCode.isEmpty()) {
                return false;
            }

            if (!appointmentDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return false;
            }

            if (!insuranceCode.equals("IESS") && !insuranceCode.equals("PRIVADO") && !insuranceCode.equals("NINGUNO")) {
                return false;
            }
        }

        return true;
    }
}
