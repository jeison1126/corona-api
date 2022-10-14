package corona.financiero.nmda.admision.dto.notificacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAttachmentDTO {
    private String document;
    private String type;
    private String fileName;
}
