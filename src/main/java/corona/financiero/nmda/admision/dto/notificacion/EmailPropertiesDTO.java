package corona.financiero.nmda.admision.dto.notificacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailPropertiesDTO {

    private List<EmailToDTO> to;
    private List<EmailToDTO> cc;
    private String subject;
    private String templateId;
    private List<EmailParametersDTO> params;
    private List<EmailAttachmentDTO> attachments;
}
