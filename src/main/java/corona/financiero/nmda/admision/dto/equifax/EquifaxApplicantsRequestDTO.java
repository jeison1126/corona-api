package corona.financiero.nmda.admision.dto.equifax;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquifaxApplicantsRequestDTO {
    @JsonAlias("primaryConsumer")
    private EquifaxPrimaryConsumerRequestDTO primaryConsumer;
}
