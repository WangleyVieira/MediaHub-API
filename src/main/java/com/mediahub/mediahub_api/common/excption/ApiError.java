package com.mediahub.mediahub_api.common.excption;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String message,
        LocalDateTime timestamp

) {}
