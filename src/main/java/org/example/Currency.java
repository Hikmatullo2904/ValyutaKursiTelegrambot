package org.example;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {
    @SerializedName("id")
    private int id;

    @SerializedName("Code")
    private String code;

    @SerializedName("Ccy")
    private String currency;

    @SerializedName("CcyNm_RU")
    private String currencyNameRU;

    @SerializedName("CcyNm_UZ")
    private String currencyNameUZ;

    @SerializedName("CcyNm_UZC")
    private String currencyNameUZC;

    @SerializedName("CcyNm_EN")
    private String currencyNameEN;

    @SerializedName("Nominal")
    private String nominal;

    @SerializedName("Rate")
    private String rate;

    @SerializedName("Diff")
    private String diff;

    @SerializedName("Date")
    private String date;
}
