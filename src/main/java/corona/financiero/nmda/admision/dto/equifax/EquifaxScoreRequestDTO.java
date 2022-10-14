package corona.financiero.nmda.admision.dto.equifax;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxScoreRequestDTO {
    @JsonAlias("applicants")
    private EquifaxApplicantsRequestDTO applicants;
}
