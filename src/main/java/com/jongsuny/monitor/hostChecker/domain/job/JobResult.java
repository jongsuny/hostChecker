package com.jongsuny.monitor.hostChecker.domain.job;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jongsuny on 18/1/13.
 */
@Data
public class JobResult {
    private int total = 0;
    private int down = 0;
    private int alive = 0;
    private int invalid = 0;
    private Date lastRunDate;
    private JobStatus status;

    public static JobResult getRegistered() {
        JobResult result = new JobResult();
        result.setStatus(JobStatus.REGISTER);
        return result;
    }
    public static JobResult getError() {
        JobResult result = new JobResult();
        result.setStatus(JobStatus.ERROR);
        return result;
    }
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(status.getCode()).append("\n")
                .append("(").append(total).append(",")
                .append(alive).append(",")
                .append(invalid).append(",")
                .append(down).append(")\n");
        if(lastRunDate != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            stringBuilder.append(simpleDateFormat.format(lastRunDate));
        }
        return stringBuilder.toString();
    }
}
