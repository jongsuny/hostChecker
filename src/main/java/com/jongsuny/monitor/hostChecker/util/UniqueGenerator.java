package com.jongsuny.monitor.hostChecker.util;

import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.job.Job;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * Created by jongsuny on 18/1/4.
 */
public class UniqueGenerator {

    public static String makeId() {
        return DigestUtils.sha1Hex(String.valueOf(RandomUtils.nextLong(0, Long.MAX_VALUE)));
    }

    public static String makeId(String value) {
        return DigestUtils.sha1Hex(value);
    }

    public static String makeServiceId(String domain) {
        return DigestUtils.sha1Hex(domain);
    }

    public static String makeJobId(Job job) {
        return DigestUtils.sha1Hex(job.getDomain() + "_" + job.getJobName() + System.currentTimeMillis());
    }

    public static String makeGroupId(Group group) {
        return DigestUtils.sha1Hex(group.getDomain() + "_" + group.getGroupName());
    }
}
