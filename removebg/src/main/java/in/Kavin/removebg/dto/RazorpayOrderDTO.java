package in.Kavin.removebg.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class RazorpayOrderDTO {
    private String id;
    private String amount;
    private String currency;
    private String status;
    private String created_at;
    private String receipt;

}