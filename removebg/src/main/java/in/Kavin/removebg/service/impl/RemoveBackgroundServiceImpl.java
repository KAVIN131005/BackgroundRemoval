package in.Kavin.removebg.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.Kavin.removebg.client.ClipdropClient;
import in.Kavin.removebg.service.RemoveBackgroundService;

@Service
@RequiredArgsConstructor
public class RemoveBackgroundServiceImpl implements RemoveBackgroundService {

    @Value("${clipdrop.apikey}")
    private String apiKey;

    private final ClipdropClient clipdropClient;


    @Override
    public byte[] removeBackground(MultipartFile file) {
        return clipdropClient.removeBackground(file,apiKey);
    }
}
