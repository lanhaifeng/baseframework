package com.feng.baseframework.aerospike;


import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;

import java.util.Optional;

public class AerospikeStatement {

    private AerospikeClient aerospikeClient;

    public static void main(String[] args) {
        AerospikeClient aerospikeClient = new AerospikeClient("10.100.1.116",3000);
        Statement statement = new Statement();
        statement.setNamespace("dfp");
        statement.setSetName("dfp");
        RecordSet recordSet = aerospikeClient.query(null, statement);

        Record record = aerospikeClient.get(null, new Key("dfp", "dfp", "webSmartID=2bef5fccfad4020ff2c7857396433a35"));
        Optional.ofNullable(record).ifPresent(System.out::println);

        recordSet.forEach(keyRecord -> {
            if(keyRecord.key.userKey.toString().startsWith("webSmartID=")){
                System.out.println(keyRecord.key);
                System.out.println(keyRecord.record);
            }else {
                System.out.println(keyRecord.key);
                System.out.println(keyRecord.key.userKey);
                System.out.println(keyRecord.record.toString());
            }
        });
    }
}
