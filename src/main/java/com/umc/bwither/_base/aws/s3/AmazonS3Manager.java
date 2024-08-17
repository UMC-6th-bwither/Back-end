package com.umc.bwither._base.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.umc.bwither._base.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final S3Config s3Config;

    private final UuidRepository uuidRepository;

    public String uploadFile(String keyName, MultipartFile file){ return null;}
}