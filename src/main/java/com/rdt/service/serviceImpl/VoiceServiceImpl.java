package com.rdt.service.serviceImpl;

import com.rdt.service.VoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class VoiceServiceImpl implements VoiceService {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public VoiceServiceImpl(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    @Override
    public String transcribe(MultipartFile audioFile) {
        log.info("Inside transcribe method :{}",audioFile);
        try {
            Resource audioFileResource = audioFile.getResource();
            AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioFileResource);
            AudioTranscriptionResponse response = transcriptionModel.call(prompt);
            
            return response.getResult().getOutput();
        } catch (Exception e) {
            log.error("Error occurred in transcribe method :{}",e.getMessage());
            throw new RuntimeException("Voice transcription failed: " + e.getMessage());
        }
    }
}