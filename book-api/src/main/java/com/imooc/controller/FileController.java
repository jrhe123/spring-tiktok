package com.imooc.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.MinIOConfig;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.MinIOUtils;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "File controller")
@RestController
@RequestMapping("file")
public class FileController {

	@Autowired
	private MinIOConfig minIOConfig;
	
	@PostMapping("upload")
	public GraceJSONResult upload(
			MultipartFile file
			) throws IOException, Exception {
		String fileName = file.getOriginalFilename();
		MinIOUtils.uploadFile(
				minIOConfig.getBucketName(),
				fileName,
				file.getInputStream()
				);
		
		String imageUrl = minIOConfig.getFileHost()
						+ "/" + minIOConfig.getBucketName()
						+ "/" + fileName;
		return GraceJSONResult.ok(imageUrl);
	}
}
