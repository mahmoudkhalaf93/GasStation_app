package com.example.gasstation;

public class GetRateGasStation {
    String GasStationNo ,ServiceNo ,Value;

    public String getGasStationNo() {
        return GasStationNo;
    }

    public void setGasStationNo(String gasStationNo) {
        GasStationNo = gasStationNo;
    }

    public String getServiceNo() {
        return ServiceNo;
    }

    public void setServiceNo(String serviceNo) {
        ServiceNo = serviceNo;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

//    select GasStation.GasStationNo ,Services.ServiceNo , RatingServcReview.Value
//    from ((GasStation left join Services on GasStation.GasStationNo=Services.GasStationNo)
//    inner join RatingServcReview on Services.ServiceNO=RatingServcReview.ServiceNo)


}
