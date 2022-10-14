package corona.financiero.nmda.admision.dto.equifax;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquifaxPeronalInformationRequestDTO {
    @JsonAlias("name")
    private List<EquifaxIdentifierRequestDTO> name;
    @JsonAlias("chileanRut")
    private String chileanRut;
    @JsonAlias("addresses")
    private List<EquifaxIdentifierRequestDTO> addresses;
    @JsonAlias("chileanSerialNumber")
    private String chileanSerialNumber;
    @JsonAlias("customerReferenceIdentifier")
    private String customerReferenceIdentifier = "CORONA";
    @JsonAlias("additionalAttribute")
    private EquifaxAdditionalAttributeRequestDTO additionalAttribute;
}
